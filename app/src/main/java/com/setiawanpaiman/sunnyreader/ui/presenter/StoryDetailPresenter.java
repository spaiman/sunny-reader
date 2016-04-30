package com.setiawanpaiman.sunnyreader.ui.presenter;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import rx.Observable;
import rx.Scheduler;

public class StoryDetailPresenter extends EndlessListPresenter<Comment> {

    private IHackerNewsService mHackerNewsService;
    private Story mStory;

    public StoryDetailPresenter(EndlessListContract.View<Comment> view,
                                Scheduler scheduler,
                                IHackerNewsService hackerNewsService,
                                Story story) {
        super(view, scheduler);
        mHackerNewsService = hackerNewsService;
        mStory = story;
    }

    @Override
    public Observable<Comment> createRequestObservable(int page) {
        return mHackerNewsService.getComments(mStory, page);
    }
}
