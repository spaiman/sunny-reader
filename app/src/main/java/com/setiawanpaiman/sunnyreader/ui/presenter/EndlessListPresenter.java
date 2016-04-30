package com.setiawanpaiman.sunnyreader.ui.presenter;

import com.setiawanpaiman.sunnyreader.Constants;
import com.setiawanpaiman.sunnyreader.util.RxUtils;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public abstract class EndlessListPresenter<Model> implements EndlessListContract.Presenter<Model> {

    private EndlessListContract.View<Model> mView;
    private Scheduler mScheduler;

    private Subscription mSubscription;
    private boolean mLoadMoreTriggered;
    private int mCurrentPage = Constants.FIRST_PAGE;

    public EndlessListPresenter(EndlessListContract.View<Model> view,
                                Scheduler scheduler) {
        mView = view;
        mScheduler = scheduler;
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
    public void loadData(final boolean refresh) {
        if (refresh) RxUtils.unsubscribeAll(mSubscription);
        else if (isLoadIsInProgress()) {
            mLoadMoreTriggered = true;
            return;
        }

        mCurrentPage = refresh ? Constants.FIRST_PAGE : mCurrentPage + 1;
        mView.setProgressVisibility(true, refresh);
        mSubscription = createRequestObservable(mCurrentPage)
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mLoadMoreTriggered) {
                            mLoadMoreTriggered = false;
                            loadData(false);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribeOn(mScheduler)
                .subscribe(new Subscriber<Model>() {
                    boolean firstOnNext = true;

                    @Override
                    public void onCompleted() {
                        mView.setProgressVisibility(false, refresh);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // reset current page if error occurred
                        if (!refresh) mCurrentPage--;
                        mView.setProgressVisibility(false, refresh);
                    }

                    @Override
                    public void onNext(Model story) {
                        if (refresh && firstOnNext) {
                            mView.showData(story, true);
                        } else {
                            mView.showData(story, false);
                        }
                        firstOnNext = false;
                    }
                });
    }

    @Override
    public void onDestroy() {
        RxUtils.unsubscribeAll(mSubscription);
    }

    public abstract Observable<Model> createRequestObservable(int page);
}
