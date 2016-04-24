package com.setiawanpaiman.sunnyreader.domain.service;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;

import rx.Observable;

public class HackerNewsService implements IHackerNewsService {

    @Override
    public Observable<Story> getTopStories() {
        return null;
    }

    @Override
    public Observable<Story> getTopStories(int page) {
        return null;
    }

    @Override
    public Observable<Comment> getComments(Story story) {
        return null;
    }

    @Override
    public Observable<Comment> getComments(Story story, int page) {
        return null;
    }
}
