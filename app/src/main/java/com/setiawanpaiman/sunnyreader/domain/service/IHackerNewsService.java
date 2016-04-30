package com.setiawanpaiman.sunnyreader.domain.service;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;

import rx.Observable;

import java.util.List;

public interface IHackerNewsService {

    /**
     * Get the first page of top stories
     * @return List of Top stories emitted in order as Observable
     */
    Observable<List<Story>> getTopStories(int count);

    /**
     * Get the top stories in page specified by {@code page} parameter
     * @param page The page where top stories will be retrieved
     * @return List of Top stories emitted in order as Observable
     */
    Observable<List<Story>> getTopStories(int page, int count);

    /**
     * Get Story {@code story} comments in the first page
     * @param story The story which comments will be retrieved
     * @return List of Comments emitted in order as Observable
     */
    Observable<List<Comment>> getComments(Story story, int count);

    /**
     * Get Story {@code story} comments in page specified by {@code page} parameter
     * @param story The story which comments will be retrieved
     * @param page The page where comments will be retrieved
     * @return List of Comments emitted in order as Observable
     */
    Observable<List<Comment>> getComments(Story story, int page, int count);
}
