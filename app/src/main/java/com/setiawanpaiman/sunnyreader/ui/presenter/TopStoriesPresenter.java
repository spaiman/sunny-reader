package com.setiawanpaiman.sunnyreader.ui.presenter;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import rx.Observable;
import rx.Scheduler;

public class TopStoriesPresenter extends EndlessListPresenter<Story> {

    private IHackerNewsService mHackerNewsService;

    public TopStoriesPresenter(EndlessListContract.View<Story> view,
                               Scheduler scheduler,
                               IHackerNewsService hackerNewsService) {
        super(view, scheduler);
        mHackerNewsService = hackerNewsService;
    }

    @Override
    public Observable<Story> createRequestObservable(int page) {
        return mHackerNewsService.getTopStories(page);
    }
}
