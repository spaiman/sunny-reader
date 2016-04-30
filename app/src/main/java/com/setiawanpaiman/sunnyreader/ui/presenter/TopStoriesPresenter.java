package com.setiawanpaiman.sunnyreader.ui.presenter;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import rx.Observable;
import rx.Scheduler;

import java.util.List;

public class TopStoriesPresenter extends EndlessListPresenter<List<Story>> {

    private IHackerNewsService mHackerNewsService;

    public TopStoriesPresenter(EndlessListContract.View<List<Story>> view,
                               Scheduler scheduler,
                               IHackerNewsService hackerNewsService) {
        super(view, scheduler);
        mHackerNewsService = hackerNewsService;
    }

    @Override
    public Observable<List<Story>> createRequestObservable(int page) {
        return mHackerNewsService.getTopStories(page);
    }
}
