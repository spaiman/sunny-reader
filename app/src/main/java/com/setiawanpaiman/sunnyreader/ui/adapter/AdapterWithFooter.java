package com.setiawanpaiman.sunnyreader.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class AdapterWithFooter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int ITEM_VIEW_TYPE_ITEM = 0;
    protected static final int ITEM_VIEW_TYPE_FOOTER = Integer.MAX_VALUE;

    protected Context mContext;
    private boolean mFooterVisible;

    public AdapterWithFooter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isViewTypeItem(viewType)) {
            return onCreateViewHolderItem(parent, viewType);
        } else {
            return onCreateViewHolderFooter(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isFooter(position)) {
            @SuppressWarnings("unchecked") VH castedHolder = (VH) holder;
            onBindViewHolderItem(castedHolder, position);
        } else {
            onBindViewHolderFooter(holder);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isFooter(position) ? ITEM_VIEW_TYPE_FOOTER : ITEM_VIEW_TYPE_ITEM;
    }

    protected boolean isViewTypeItem(int viewType) {
        return viewType == ITEM_VIEW_TYPE_ITEM;
    }

    public abstract VH onCreateViewHolderItem(ViewGroup parent, int viewType);

    public abstract RecyclerView.ViewHolder onCreateViewHolderFooter(ViewGroup parent, int viewType);

    public boolean isFooterVisible() {
        return mFooterVisible;
    }

    public void setFooterVisible(boolean footerVisible) {
        this.mFooterVisible = footerVisible;
    }

    protected abstract void onBindViewHolderItem(VH holder, int position);

    protected void onBindViewHolderFooter(RecyclerView.ViewHolder holder) { }

    protected abstract boolean isFooter(int position);
}