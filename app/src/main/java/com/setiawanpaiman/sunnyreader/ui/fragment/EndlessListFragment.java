package com.setiawanpaiman.sunnyreader.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.List;

public abstract class EndlessListFragment<Model extends Parcelable> extends BaseFragment
        implements EndlessListContract.View<List<Model>>,
                   SwipeRefreshLayout.OnRefreshListener,
                   EndlessRecyclerView.OnLoadMoreListener {

    private static final String BUNDLE_PAGE = "page";
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
    private EndlessListContract.Presenter<List<Model>> mPresenter;

    public EndlessListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setRetainInstance(true);
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
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState.getInt(BUNDLE_PAGE));
            List<Model> data = savedInstanceState.getParcelableArrayList(BUNDLE_DATA);
            mAdapter.addAll(data, true);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_PAGE, mPresenter.getCurrentPage());
        outState.putParcelableArrayList(BUNDLE_DATA, (ArrayList<Model>) mAdapter.getAll());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mAdapter.hasData() && savedInstanceState == null) onRefresh();
        else if (!mAdapter.hasData()) onRefresh();
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
    public void showData(List<Model> data, boolean refresh) {
        mAdapter.addAll(data, refresh);
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

    public abstract EndlessListContract.Presenter<List<Model>> createPresenter();

    public abstract EndlessListAdapter<Model, ?> createAdapter();

    protected EndlessListAdapter<Model, ?> getAdapter() {
        return mAdapter;
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
