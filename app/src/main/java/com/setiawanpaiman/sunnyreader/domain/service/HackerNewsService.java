package com.setiawanpaiman.sunnyreader.domain.service;

import android.support.annotation.NonNull;

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

public class HackerNewsService implements IHackerNewsService {

    private final HackerNewsApi mHackerNewsApi;
    private final HackerNewsPersistent mHackerNewsPersistent;

    public HackerNewsService(HackerNewsApi hackerNewsApi,
                             HackerNewsPersistent hackerNewsPersistent) {
        mHackerNewsApi = hackerNewsApi;
        mHackerNewsPersistent = hackerNewsPersistent;
    }

    @Override
    public Observable<Story> getTopStories() {
        return getTopStories(Constants.FIRST_PAGE);
    }

    @Override
    public Observable<Story> getTopStories(int page) {
        return getTopStoriesId(page)
                .concatMap(new Func1<List<Long>, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(List<Long> storyIds) {
                        return Observable.from(storyIds);
                    }
                })
                .concatMap(new Func1<Long, Observable<Story>>() {
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
                })
                .filter(new Func1<Story, Boolean>() {
                    @Override
                    public Boolean call(Story story) {
                        return story != null;
                    }
                });
    }

    @Override
    public Observable<Comment> getComments(@NonNull Story story) {
        return getComments(story, Constants.FIRST_PAGE);
    }

    @Override
    public Observable<Comment> getComments(@NonNull Story story, int page) {
        List<Long> subCommentIds = story.getCommentIds();
        int lBound = Math.min(subCommentIds.size(), (page - 1) * Constants.PER_PAGE);
        int uBound = Math.min(subCommentIds.size(), lBound + Constants.PER_PAGE);
        subCommentIds = subCommentIds.subList(lBound, uBound);
        return getComments(subCommentIds, Constants.FIRST_DEPTH);
    }

    private Observable<Comment> getComments(List<Long> commentIds, final int depth) {
        return Observable.from(commentIds)
                .concatMap(new Func1<Long, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Long commentId) {
                        return getCommentAndReplies(commentId, depth);
                    }
                }).filter(new Func1<Comment, Boolean>() {
                    @Override
                    public Boolean call(Comment comment) {
                        return comment != null && !comment.isDeleted();
                    }
                });
    }

    private Observable<Comment> getCommentAndReplies(final long commentId, final int depth) {
        return mHackerNewsApi.getComment(commentId)
                .doOnNext(new Action1<Comment>() {
                    @Override
                    public void call(Comment comment) {
                        if (comment != null) {
                            comment.setDepth(depth);
                            mHackerNewsPersistent.saveComment(comment);
                        }
                    }
                })
                .onErrorResumeNext(mHackerNewsPersistent.getComment(commentId))
                .flatMap(new Func1<Comment, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Comment comment) {
                        List<Long> commentReplyIds = comment != null ?
                                comment.getCommentIds() : new ArrayList<Long>();
                        if (commentReplyIds.size() > 0) {
                            return Observable.concat(Observable.just(comment),
                                    getComments(commentReplyIds, depth + 1));
                        } else {
                            return Observable.just(comment);
                        }
                    }
                });
    }

    private Observable<List<Long>> retrieveTopStoriesId(final int start, final int count) {
        return Observable.defer(new Func0<Observable<List<Long>>>() {
            @Override
            public Observable<List<Long>> call() {
                return mHackerNewsApi.getTopStories()
                        .doOnNext(new Action1<List<Long>>() {
                            @Override
                            public void call(List<Long> topStoriesId) {
                                mHackerNewsPersistent.saveTopStories(topStoriesId);
                            }
                        }).flatMap(new Func1<List<Long>, Observable<List<Long>>>() {
                            @Override
                            public Observable<List<Long>> call(List<Long> longs) {
                                return mHackerNewsPersistent.getTopStories(start, count);
                            }
                        });
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
