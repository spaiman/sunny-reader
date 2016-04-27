package com.setiawanpaiman.sunnyreader.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.ui.adapter.StoryAdapter;
import com.setiawanpaiman.sunnyreader.ui.topstories.TopStoriesContract;
import com.setiawanpaiman.sunnyreader.ui.topstories.TopStoriesPresenter;
import com.setiawanpaiman.sunnyreader.ui.widget.EndlessRecyclerView;
import com.setiawanpaiman.sunnyreader.util.AndroidUtils;

import java.util.ArrayList;

public class TopStoriesActivity extends BaseActivity
        implements TopStoriesContract.View,
                   SwipeRefreshLayout.OnRefreshListener,
                   EndlessRecyclerView.OnLoadMoreListener,
                   StoryAdapter.OnClickListener {

    private static final String BUNDLE_CURRENT_PAGE = "current_page";
    private static final String BUNDLE_TOP_STORIES= "top_stories";

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.recycler_view)
    EndlessRecyclerView mRecyclerView;

    private View.OnClickListener mOnRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.loadTopStories(mIsLastRequestRefresh);
        }
    };

    private StoryAdapter mAdapter;
    private boolean mIsLastRequestRefresh;

    private TopStoriesContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stories);
        ButterKnife.bind(this);

        mPresenter = new TopStoriesPresenter(
                getApplicationComponent().provideHackerNewsService(), this);

        initViews();
        if (savedInstanceState == null) {
            onRefresh();
        } else {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CURRENT_PAGE, mPresenter.getCurrentPage());
        outState.putParcelableArrayList(BUNDLE_TOP_STORIES, new ArrayList<>(mAdapter.getAll()));
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setProgressVisibility(boolean show, boolean refresh) {
        if (refresh) {
            AndroidUtils.setSwipeRefreshing(mSwipeRefresh, show);
            mAdapter.setFooterVisible(false);
        } else {
            AndroidUtils.setSwipeRefreshing(mSwipeRefresh, false);
            mAdapter.setFooterVisible(show);
        }

        if (show && !AndroidUtils.isNetworkAvailable(this)) {
            Snackbar.make(mSwipeRefresh, R.string.error_no_internet_connection, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, mOnRetryClickListener)
                    .show();
        }
    }

    @Override
    public void showTopStory(Story story, boolean refresh) {
        mAdapter.add(story, refresh);
    }

    @Override
    public void onRefresh() {
        mIsLastRequestRefresh = true;
        mPresenter.loadTopStories(true);
    }

    @Override
    public void onLoadMore() {
        if (!mPresenter.isLoadIsInProgress()) mIsLastRequestRefresh = false;
        mPresenter.loadTopStories(false);
    }

    @Override
    public void onStoryClicked(Story story) {
        // TODO: Open detail activity after it has been implemented
    }

    @Override
    public void onOpenInBrowserClicked(Story story) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(story.getUrl()));
        startActivity(intent);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        mPresenter.onRestoreInstanceState(savedInstanceState.getInt(BUNDLE_CURRENT_PAGE));
        ArrayList<Story> topStories = savedInstanceState.getParcelableArrayList(BUNDLE_TOP_STORIES);
        mAdapter.addAll(topStories, true);
    }

    private void initViews() {
        mAdapter = new StoryAdapter(this);
        mAdapter.setOnClickListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadMoreListener(this);

        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(this);
    }
}
