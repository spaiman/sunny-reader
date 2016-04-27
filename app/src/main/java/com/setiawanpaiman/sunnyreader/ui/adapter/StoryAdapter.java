package com.setiawanpaiman.sunnyreader.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Story;

import java.util.concurrent.TimeUnit;

public class StoryAdapter extends EndlessListAdapter<Story, StoryAdapter.ViewHolder> {

    private OnClickListener mOnClickListener;

    public StoryAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolderItem(ViewGroup parent,
                                             int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_story, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    int pos = vh.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mOnClickListener.onStoryClicked(mData.get(pos));
                    }
                }
            }
        });
        vh.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    int pos = vh.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mOnClickListener.onOpenInBrowserClicked(mData.get(pos));
                    }
                }
            }
        });
        return vh;
    }

    @Override
    protected void onBindViewHolderItem(ViewHolder holder, int position) {
        Story story = mData.get(position);
        String host = TextUtils.isEmpty(story.getUrl()) ? "" : Uri.parse(story.getUrl()).getHost();

        holder.txtTitle.setText(story.getTitle());
        holder.txtTotalPoints.setText(
                mContext.getString(R.string.points_format, story.getScore()));
        holder.txtAuthor.setText(story.getAuthor());
        holder.txtTime.setText(DateUtils.getRelativeTimeSpanString(
                TimeUnit.SECONDS.toMillis(story.getTimestamp()), System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE));
        holder.txtTotalComments.setText(
                mContext.getResources()
                        .getQuantityString(R.plurals.total_comments,
                                story.getTotalComments(), story.getTotalComments()));
        holder.txtHostUrl.setText(host);
        holder.btnOpen.setVisibility(TextUtils.isEmpty(host) ? View.GONE : View.VISIBLE);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_title)
        TextView txtTitle;

        @BindView(R.id.txt_total_points)
        TextView txtTotalPoints;

        @BindView(R.id.txt_author)
        TextView txtAuthor;

        @BindView(R.id.txt_time)
        TextView txtTime;

        @BindView(R.id.txt_total_comments)
        TextView txtTotalComments;

        @BindView(R.id.txt_host_url)
        TextView txtHostUrl;

        @BindView(R.id.btn_open)
        ImageView btnOpen;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {

        void onStoryClicked(Story story);

        void onOpenInBrowserClicked(Story story);
    }
}