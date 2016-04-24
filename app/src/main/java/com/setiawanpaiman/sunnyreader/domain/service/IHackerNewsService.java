package com.setiawanpaiman.sunnyreader.domain.service;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;

import rx.Observable;

public interface IHackerNewsService {

    /**
     * Get the first page of top stories
     * @return Top stories emitted in order as Observable
     */
    Observable<Story> getTopStories();

    /**
     * Get the top stories in page specified by {@code page} parameter
     * @param page The page where top stories will be retrieved
     * @return Top stories emitted in order as Observable
     */
    Observable<Story> getTopStories(int page);

    /**
     * Get Story {@code story} comments in the first page
     * @param story The story which comments will be retrieved
     * @return Comments emitted in order as Observable
     */
    Observable<Comment> getComments(Story story);

    /**
     * Get Story {@code story} comments in page specified by {@code page} parameter
     * @param story The story which comments will be retrieved
     * @param page The page where comments will be retrieved
     * @return Comments emitted in order as Observable
     */
    Observable<Comment> getComments(Story story, int page);
}
