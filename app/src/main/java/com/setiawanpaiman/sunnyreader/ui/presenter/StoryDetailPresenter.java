package com.setiawanpaiman.sunnyreader.ui.presenter;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import rx.Observable;
import rx.Scheduler;

import java.util.List;

public class StoryDetailPresenter extends EndlessListPresenter<List<Comment>> {

    private IHackerNewsService mHackerNewsService;
    private Story mStory;

    public StoryDetailPresenter(EndlessListContract.View<List<Comment>> view,
                                Scheduler scheduler,
                                IHackerNewsService hackerNewsService,
                                Story story) {
        super(view, scheduler);
        mHackerNewsService = hackerNewsService;
        mStory = story;
    }

    @Override
    public Observable<List<Comment>> createRequestObservable(int page, int count) {
        return mHackerNewsService.getComments(mStory, page, count);
    }
}
