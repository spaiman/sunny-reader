package com.setiawanpaiman.sunnyreader.ui.topstories;

import com.setiawanpaiman.sunnyreader.Constants;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;
import com.setiawanpaiman.sunnyreader.util.RxUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class TopStoriesPresenter implements TopStoriesContract.Presenter {

    private IHackerNewsService mHackerNewsService;
    private TopStoriesContract.View mView;

    private Subscription mSubscription;
    private boolean mLoadMoreTriggered;
    private int mCurrentPage = Constants.FIRST_PAGE;

    public TopStoriesPresenter(IHackerNewsService hackerNewsService,
                               TopStoriesContract.View view) {
        mHackerNewsService = hackerNewsService;
        mView = view;
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public void onRestoreInstanceState(int page) {
        mCurrentPage = page;
    }

    @Override
    public boolean isLoadIsInProgress() {
        return RxUtils.isSubscriptionActive(mSubscription);
    }

    @Override
    public void loadTopStories(final boolean refresh) {
        if (refresh) RxUtils.unsubscribeAll(mSubscription);
        else if (isLoadIsInProgress()) {
            mLoadMoreTriggered = true;
            return;
        }

        mCurrentPage = refresh ? Constants.FIRST_PAGE : mCurrentPage + 1;
        mView.setProgressVisibility(true, refresh);
        mSubscription = mHackerNewsService.getTopStories(mCurrentPage)
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mLoadMoreTriggered) {
                            mLoadMoreTriggered = false;
                            loadTopStories(false);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Story>() {
                    boolean firstOnNext = true;

                    @Override
                    public void onCompleted() {
                        mView.setProgressVisibility(false, refresh);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setProgressVisibility(false, refresh);
                    }

                    @Override
                    public void onNext(Story story) {
                        if (refresh && firstOnNext) {
                            mView.showTopStory(story, true);
                        } else {
                            mView.showTopStory(story, false);
                        }
                        firstOnNext = false;
                    }
                });
    }

    @Override
    public void onDestroy() {
        RxUtils.unsubscribeAll(mSubscription);
    }
}
