package com.setiawanpaiman.sunnyreader.domain.persistent;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.room.Room;

import com.setiawanpaiman.sunnyreader.data.db.ApplicationDatabase;
import com.setiawanpaiman.sunnyreader.data.db.Item;
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
    private static final int MAX_ITEMS = 5000;

    private final JsonParser mJsonParser;
    private final SharedPreferences mSharedPreferences;
    private final ApplicationDatabase mDb;

    public HackerNewsDiskStore(Context context, JsonParser jsonParser, SharedPreferences sharedPreferences) {
        mJsonParser = jsonParser;
        mSharedPreferences = sharedPreferences;
        mDb = Room.databaseBuilder(context, ApplicationDatabase.class, "app-db").build();
    }

    @Override
    public Observable<List<Long>> getTopStories(final int start, final int limit) {
        return Observable.defer(new Func0<Observable<List<Long>>>() {
            @Override
            public Observable<List<Long>> call() {
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
        });
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
                Item item = new Item(story.getId(), json, System.currentTimeMillis());
                mDb.itemDao().insertAll(item);
                cleanOldData();
            }
        }
    }

    @Override
    public void saveComment(Comment comment) {
        if (comment != null) {
            String json = mJsonParser.toJson(comment, Comment.class);
            if (json != null) {
                Item item = new Item(comment.getId(), json, System.currentTimeMillis());
                mDb.itemDao().insertAll(item);
                cleanOldData();
            }
        }
    }

    private void cleanOldData() {
        if (mDb.itemDao().getCount() > MAX_ITEMS) {
            mDb.itemDao().removeOldestNData(MAX_ITEMS);
        }
    }

    public <T> Observable<T> getItemCache(final long id, final Class<T> clazz) {
        return Observable.defer(new Func0<Observable<Item>>() {
            @Override
            public Observable<Item> call() {
                Item itemCache = mDb.itemDao().findById(id);
                if (itemCache == null) return Observable.empty();
                else return Observable.just(itemCache);
            }
        }).map(new Func1<Item, T>() {
            @Override
            public T call(Item itemCache) {
                return mJsonParser.fromJson(itemCache.getResponse(), clazz);
            }
        });
    }
}
