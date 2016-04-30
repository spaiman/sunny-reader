package com.setiawanpaiman.sunnyreader.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.ui.adapter.EndlessListAdapter;
import com.setiawanpaiman.sunnyreader.ui.presenter.EndlessListContract;
import com.setiawanpaiman.sunnyreader.ui.widget.EndlessRecyclerView;
import com.setiawanpaiman.sunnyreader.util.AndroidUtils;

import java.util.ArrayList;

public abstract class EndlessListFragment<Model extends Parcelable> extends BaseFragment
        implements EndlessListContract.View<Model>,
                   SwipeRefreshLayout.OnRefreshListener,
                   EndlessRecyclerView.OnLoadMoreListener {

    private static final String BUNDLE_CURRENT_PAGE = "current_page";
    private static final String BUNDLE_DATA = "data";

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.recycler_view)
    EndlessRecyclerView mRecyclerView;

    private View.OnClickListener mOnRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.loadData(mIsLastRequestRefresh);
        }
    };

    private EndlessListAdapter<Model, ?> mAdapter;
    private boolean mIsLastRequestRefresh;
    private EndlessListContract.Presenter mPresenter;

    public EndlessListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mAdapter = createAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_endless_list, container, false);
        ButterKnife.bind(this, view);
        initViews();
        if (savedInstanceState == null) {
            if (!mAdapter.hasData()) onRefresh();
        } else {
            restoreInstanceState(savedInstanceState);
        }
        return view;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CURRENT_PAGE, mPresenter.getCurrentPage());
        outState.putParcelableArrayList(BUNDLE_DATA, new ArrayList<>(mAdapter.getAll()));
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
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

        if (show && !AndroidUtils.isNetworkAvailable(getContext())) {
            Snackbar.make(mSwipeRefresh, R.string.error_no_internet_connection, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, mOnRetryClickListener)
                    .show();
        }
    }

    @Override
    public void showData(Model data, boolean refresh) {
        mAdapter.add(data, refresh);
    }

    @Override
    public void onRefresh() {
        mIsLastRequestRefresh = true;
        mPresenter.loadData(true);
    }

    @Override
    public void onLoadMore() {
        if (!mPresenter.isLoadIsInProgress()) mIsLastRequestRefresh = false;
        mPresenter.loadData(false);
    }

    public abstract EndlessListContract.Presenter<Model> createPresenter();

    public abstract EndlessListAdapter<Model, ?> createAdapter();

    protected EndlessListAdapter<Model, ?> getAdapter() {
        return mAdapter;
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        mPresenter.onRestoreInstanceState(savedInstanceState.getInt(BUNDLE_CURRENT_PAGE));
        ArrayList<Model> data = savedInstanceState.getParcelableArrayList(BUNDLE_DATA);
        mAdapter.addAll(data, true);
    }

    private void initViews() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadMoreListener(this);

        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(this);
    }
}
