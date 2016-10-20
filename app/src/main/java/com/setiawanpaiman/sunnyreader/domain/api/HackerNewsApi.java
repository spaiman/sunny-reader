package com.setiawanpaiman.sunnyreader.domain.api;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface HackerNewsApi {

    @GET("v0/topstories.json")
    Observable<List<Long>> getTopStories();

    @GET("v0/item/{storyId}.json")
    Observable<Story> getStory(@Path("storyId") long storyId);

    @GET("v0/item/{commentId}.json")
    Observable<Comment> getComment(@Path("commentId") long commentId);
}