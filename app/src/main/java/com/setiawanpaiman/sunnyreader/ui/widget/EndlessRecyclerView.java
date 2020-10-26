package com.setiawanpaiman.sunnyreader.ui.widget;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

public class EndlessRecyclerView extends RecyclerView {

    private static final int LOAD_MORE_ITEM_THRESHOLD = 2;

    private int mPreviousTotal = 0;
    private boolean mLoading = false;

    private OnLoadMoreListener mOnLoadMoreListener = null;

    private OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager == null || mOnLoadMoreListener == null) {
                return;
            }

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

            if (mLoading && mPreviousTotal != totalItemCount) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }

            if (!mLoading && (visibleItemCount + pastVisibleItems + LOAD_MORE_ITEM_THRESHOLD) >= totalItemCount) {
                mLoading = true;
                mPreviousTotal = totalItemCount;
                mOnLoadMoreListener.onLoadMore();
            }
        }
    };

    public EndlessRecyclerView(Context context) {
        super(context);
        init();
    }

    public EndlessRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EndlessRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private void setLoadMoreScrollListener() {
        removeOnScrollListener(mOnScrollListener);
        addOnScrollListener(mOnScrollListener);
    }

    private void init() {
        setLoadMoreScrollListener();
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }
}