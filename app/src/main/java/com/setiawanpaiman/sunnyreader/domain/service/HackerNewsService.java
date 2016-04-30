package com.setiawanpaiman.sunnyreader.domain.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.setiawanpaiman.sunnyreader.Constants;
import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.api.HackerNewsApi;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsPersistent;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HackerNewsService implements IHackerNewsService {

    private static final String TAG = HackerNewsService.class.getName();

    private final HackerNewsApi mHackerNewsApi;
    private final HackerNewsPersistent mHackerNewsPersistent;

    public HackerNewsService(HackerNewsApi hackerNewsApi,
                             HackerNewsPersistent hackerNewsPersistent) {
        mHackerNewsApi = hackerNewsApi;
        mHackerNewsPersistent = hackerNewsPersistent;
    }

    @Override
    public Observable<List<Story>> getTopStories() {
        return getTopStories(Constants.FIRST_PAGE);
    }

    @Override
    public Observable<List<Story>> getTopStories(int page) {
        Log.i(TAG, "getTopStories " + page);
        final List<Long> topStoriesId = new ArrayList<>();
        return getTopStoriesId(page)
                /**
                 * concatMap can be used here to maintain the order of Stories, but flatMap was used instead.
                 * This is because: in my personal benchmark result, the performance of concatMap is terrible.
                 * Using concatMap is far more slower than ordering the Stories manually by using
                 * combination of Map and List (see the last operator applied here).
                 */
                .flatMap(new Func1<List<Long>, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(List<Long> storyIds) {
                        topStoriesId.addAll(storyIds);
                        return Observable.from(storyIds);
                    }
                }).flatMap(new Func1<Long, Observable<Story>>() {
                    @Override
                    public Observable<Story> call(final Long storyId) {
                        return mHackerNewsApi.getStory(storyId)
                                .doOnNext(new Action1<Story>() {
                                    @Override
                                    public void call(Story story) {
                                        if (story != null) mHackerNewsPersistent.saveStory(story);
                                    }
                                })
                                .onErrorResumeNext(mHackerNewsPersistent.getStory(storyId));
                    }
                }).filter(new Func1<Story, Boolean>() {
                    @Override
                    public Boolean call(Story story) {
                        return story != null;
                    }
                }).toMap(new Func1<Story, Long>() {
                    @Override
                    public Long call(Story story) {
                        return story.getId();
                    }
                }).map(new Func1<Map<Long, Story>, List<Story>>() {
                    @Override
                    public List<Story> call(Map<Long, Story> storiesMap) {
                        List<Story> stories = new ArrayList<>();
                        for (Long id : topStoriesId) {
                            if (storiesMap.containsKey(id)) stories.add(storiesMap.get(id));
                        }
                        return stories;
                    }
                });
    }

    @Override
    public Observable<List<Comment>> getComments(@NonNull Story story) {
        return getComments(story, Constants.FIRST_PAGE);
    }

    @Override
    public Observable<List<Comment>> getComments(@NonNull Story story, int page) {
        Log.i(TAG, "getComments " + page);
        List<Long> subCommentIds = story.getCommentIds();
        int lBound = Math.min(subCommentIds.size(), (page - 1) * Constants.PER_PAGE);
        int uBound = Math.min(subCommentIds.size(), lBound + Constants.PER_PAGE);
        subCommentIds = subCommentIds.subList(lBound, uBound);
        return getComments(subCommentIds, Constants.FIRST_DEPTH);
    }

    private Observable<List<Comment>> getComments(final List<Long> commentIds, final int depth) {
        return Observable.from(commentIds)
                /**
                 * concatMap can be used here to maintain the order of Comments, but flatMap was used instead.
                 * This is because: in my personal benchmark result, the performance of concatMap is terrible.
                 * Using concatMap is far more slower than ordering the Comments manually by using
                 * combination of Map and List (see the last operator applied here).
                 */
                .flatMap(new Func1<Long, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Long commentId) {
                        return getCommentAndReplies(commentId, depth);
                    }
                }).filter(new Func1<Comment, Boolean>() {
                    @Override
                    public Boolean call(Comment comment) {
                        return comment != null && !comment.isDeleted();
                    }
                }).toMap(new Func1<Comment, Long>() {
                    @Override
                    public Long call(Comment comment) {
                        return comment.getId();
                    }
                }).map(new Func1<Map<Long, Comment>, List<Comment>>() {
                    @Override
                    public List<Comment> call(Map<Long, Comment> commentsMap) {
                        return mapIdsToComments(commentIds, commentsMap);
                    }
                });
    }

    private Observable<Comment> getCommentAndReplies(final long commentId, final int depth) {
        return mHackerNewsApi.getComment(commentId)
                .doOnNext(new Action1<Comment>() {
                    @Override
                    public void call(Comment comment) {
                        if (comment != null) {
                            mHackerNewsPersistent.saveComment(comment);
                        }
                    }
                })
                .onErrorResumeNext(mHackerNewsPersistent.getComment(commentId))
                .flatMap(new Func1<Comment, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(final Comment comment) {
                        return getReplies(comment, depth);
                    }
                });
    }

    private Observable<Comment> getReplies(Comment comment, int depth) {
        List<Long> replyIds = comment != null ? comment.getCommentIds() : new ArrayList<Long>();
        if (comment != null) comment.setDepth(depth);
        if (replyIds.size() > 0) {
            return Observable.concat(
                    Observable.just(comment),
                    getComments(replyIds, depth + 1)
                            .flatMap(new Func1<List<Comment>, Observable<Comment>>() {
                                @Override
                                public Observable<Comment> call(List<Comment> comments) {
                                    return Observable.from(comments);
                                }
                            }));
        } else {
            return Observable.just(comment);
        }
    }

    private List<Comment> mapIdsToComments(List<Long> commentIds, Map<Long, Comment> commentsMap) {
        List<Comment> comments = new ArrayList<>();
        for (Long id : commentIds) {
            if (commentsMap.containsKey(id)) {
                Comment comment = commentsMap.get(id);
                comments.add(comment);
                comments.addAll(mapIdsToComments(comment.getCommentIds(), commentsMap));
            }
        }
        return comments;
    }

    private Observable<List<Long>> retrieveTopStoriesId(final int start, final int count) {
        return Observable.defer(new Func0<Observable<List<Long>>>() {
            @Override
            public Observable<List<Long>> call() {
                return mHackerNewsApi.getTopStories()
                        .flatMap(new Func1<List<Long>, Observable<List<Long>>>() {
                            @Override
                            public Observable<List<Long>> call(List<Long> longs) {
                                mHackerNewsPersistent.saveTopStories(longs);
                                return mHackerNewsPersistent.getTopStories(start, count);
                            }
                        }).onErrorResumeNext(mHackerNewsPersistent.getTopStories(start, count));
            }
        });
    }

    private Observable<List<Long>> getTopStoriesId(int page) {
        int start = (page - 1) * Constants.PER_PAGE;
        int count = Constants.PER_PAGE;
        if (page == Constants.FIRST_PAGE) {
            return retrieveTopStoriesId(start, count);
        } else {
            return mHackerNewsPersistent.getTopStories(start, count)
                    .switchIfEmpty(retrieveTopStoriesId(start, count));
        }
    }
}
