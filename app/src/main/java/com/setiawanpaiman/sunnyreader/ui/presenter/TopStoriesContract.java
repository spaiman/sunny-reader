package com.setiawanpaiman.sunnyreader.ui.presenter;

import com.setiawanpaiman.sunnyreader.data.model.Story;

public interface TopStoriesContract {

    interface View {

        void setProgressVisibility(boolean show, boolean refresh);

        void showTopStory(Story story, boolean refresh);
    }

    interface Presenter {

        int getCurrentPage();

        void onRestoreInstanceState(int page);

        boolean isLoadIsInProgress();

        void loadTopStories(boolean refresh);

        void onDestroy();
    }
}
