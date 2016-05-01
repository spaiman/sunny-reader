package com.setiawanpaiman.sunnyreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.setiawanpaiman.sunnyreader.R;

import java.util.ArrayList;
import java.util.List;

public abstract class EndlessListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends AdapterWithFooter<VH> implements ListAdapter<T> {

    protected List<T> mData;

    public EndlessListAdapter(Context context) {
        super(context);
        mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderFooter(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_footer, parent, false);
        return new FooterViewHolder(v);
    }

    @Override
    public void setFooterVisible(boolean footerVisible) {
        if (isFooterVisible() != footerVisible) {
            super.setFooterVisible(footerVisible);
            if (footerVisible) {
                notifyItemInserted(mData.size());
            } else {
                notifyItemRemoved(mData.size());
            }
        }
    }

    @Override
    protected boolean isFooter(int position) {
        return position == mData.size();
    }

    @Override
    public int getItemCount() {
        // Additional row for footer
        return mData.size() + (isFooterVisible() ? 1 : 0);
    }

    @Override
    public boolean hasData() {
        return !mData.isEmpty();
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(mData);
    }

    @Override
    public void addAll(List<T> results, boolean refresh) {
        if (refresh) {
            mData.clear();
            notifyDataSetChanged();
        }
        int lastSize = mData.size();
        int newCount = results.size();
        mData.addAll(results);
        notifyItemRangeInserted(lastSize, newCount);
    }

    @Override
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
