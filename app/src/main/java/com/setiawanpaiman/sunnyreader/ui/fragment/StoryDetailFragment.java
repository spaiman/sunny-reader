package com.setiawanpaiman.sunnyreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.ui.adapter.CommentAdapter;
import com.setiawanpaiman.sunnyreader.ui.adapter.EndlessListAdapter;
import com.setiawanpaiman.sunnyreader.ui.presenter.EndlessListContract;
import com.setiawanpaiman.sunnyreader.ui.presenter.StoryDetailPresenter;
import com.setiawanpaiman.sunnyreader.util.AndroidUtils;

import rx.schedulers.Schedulers;

public class StoryDetailFragment extends EndlessListFragment<Comment> {

    private static final String BUNDLE_STORY = "story";

    private Story mStory;

    public static StoryDetailFragment newInstance(@NonNull Story story) {
        StoryDetailFragment fragment = new StoryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_STORY, story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mStory = getArguments().getParcelable(BUNDLE_STORY);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(R.string.title_story_detail);
    }

    @Override
    public void setProgressVisibility(boolean show, boolean refresh) {
        if (show && refresh) {
            getAdapter().clear();
        }
        AndroidUtils.setSwipeRefreshing(mSwipeRefresh, false);
        getAdapter().setFooterVisible(show);
    }

    @Override
    public EndlessListContract.Presenter<Comment> createPresenter() {
        return new StoryDetailPresenter(this, Schedulers.io(),
                getApplicationComponent().provideHackerNewsService(), mStory);
    }

    @Override
    public EndlessListAdapter<Comment, ?> createAdapter() {
        return new CommentAdapter(getContext(), mStory);
    }
}
