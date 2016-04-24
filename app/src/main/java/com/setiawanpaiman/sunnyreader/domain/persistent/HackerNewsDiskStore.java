package com.setiawanpaiman.sunnyreader.domain.persistent;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.setiawanpaiman.sunnyreader.data.db.ItemCache;
import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.util.JsonParser;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

public class HackerNewsDiskStore implements HackerNewsPersistent {

    private static final String KEY_TOP_STORIES_ID = "top_stories_id";
    private static final String ID_DELIMITER = ",";

    private final JsonParser mJsonParser;
    private final SharedPreferences mSharedPreferences;

    public HackerNewsDiskStore(JsonParser jsonParser, SharedPreferences sharedPreferences) {
        mJsonParser = jsonParser;
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public Observable<List<Long>> getTopStories(int start, int limit) {
        String[] ids = TextUtils.split(
                mSharedPreferences.getString(KEY_TOP_STORIES_ID, ""), ID_DELIMITER);
        List<Long> longIds = new ArrayList<>();
        int lBound = Math.min(ids.length, start);
        int uBound = Math.min(ids.length, lBound + limit);
        for (int i = lBound; i < uBound; i++) {
            try {
                long longId = Long.parseLong(ids[i]);
                longIds.add(longId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return Observable.just(longIds);
    }

    @Override
    public Observable<Story> getStory(final long storyId) {
        return getItemCache(storyId, Story.class);
    }

    @Override
    public Observable<Comment> getComment(long commentId) {
        return getItemCache(commentId, Comment.class);
    }

    @Override
    public void saveTopStories(List<Long> topStoriesId) {
        if (topStoriesId != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(KEY_TOP_STORIES_ID, TextUtils.join(ID_DELIMITER, topStoriesId));
            editor.apply();
        }
    }

    @Override
    public void saveStory(Story story) {
        if (story != null) {
            String json = mJsonParser.toJson(story, Story.class);
            if (json != null) {
                ItemCache itemCache = new ItemCache(story.getId(), json);
                itemCache.save();
            }
        }
    }

    @Override
    public void saveComment(Comment comment) {
        if (comment != null) {
            String json = mJsonParser.toJson(comment, Comment.class);
            if (json != null) {
                ItemCache itemCache = new ItemCache(comment.getId(), json);
                itemCache.save();
            }
        }
    }

    public <T> Observable<T> getItemCache(final long id, final Class<T> clazz) {
        return Observable.defer(new Func0<Observable<ItemCache>>() {
            @Override
            public Observable<ItemCache> call() {
                ItemCache itemCache = ItemCache.get(id);
                if (itemCache == null) return Observable.empty();
                else return Observable.just(itemCache);
            }
        }).map(new Func1<ItemCache, T>() {
            @Override
            public T call(ItemCache itemCache) {
                if (itemCache != null) {
                    return mJsonParser.fromJson(itemCache.getResponse(), clazz);
                }

                return null;
            }
        });
    }
}
