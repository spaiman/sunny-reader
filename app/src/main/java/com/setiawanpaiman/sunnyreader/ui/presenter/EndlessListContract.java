package com.setiawanpaiman.sunnyreader.ui.presenter;

public interface EndlessListContract {

    interface View<T> {

        void setProgressVisibility(boolean show, boolean refresh);

        void showData(T data, boolean refresh);
    }

    interface Presenter<T> {

        int getCurrentPage();

        void onRestoreInstanceState(int page);

        boolean isLoadIsInProgress();

        void loadData(boolean refresh);

        void onDestroy();
    }
}
