package com.setiawanpaiman.sunnyreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.util.AndroidUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends EndlessListAdapter<Comment, RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER_STORY = -1;

    private Story mStory;

    public CommentAdapter(Context context, Story story) {
        super(context);
        mStory = story;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderItem(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == ITEM_VIEW_TYPE_HEADER_STORY) {
            final View view = inflater.inflate(R.layout.item_story, parent, false);
            return new StoryAdapter.ViewHolder(view);
        } else {
            final View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    protected void onBindViewHolderItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoryAdapter.ViewHolder) {
            ((StoryAdapter.ViewHolder) holder).bind(mContext, mStory);
        } else if (holder instanceof ViewHolder) {
            Comment comment = mData.get(position - 1);
            ViewHolder vh = (ViewHolder) holder;
            vh.txtAuthor.setText(comment.getAuthor());
            vh.txtTime.setText(DateUtils.getRelativeTimeSpanString(
                    TimeUnit.SECONDS.toMillis(comment.getTimestamp()), System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE));
            vh.txtContent.setText(AndroidUtils.trim(Html.fromHtml(comment.getText())));
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) vh.itemView.getLayoutParams();
            marginLayoutParams.leftMargin = comment.getDepth() *
                    mContext.getResources().getDimensionPixelSize(R.dimen.spacing_xsmall);
        }
    }

    @Override
    public void add(Comment result, boolean refresh) {
        if (refresh) {
            mData.clear();
            notifyDataSetChanged();
        }
        mData.add(result);
        notifyItemInserted(mData.size());
    }

    @Override
    public void addAll(List<Comment> results, boolean refresh) {
        if (refresh) {
            mData.clear();
            notifyDataSetChanged();
        }
        int lastSize = mData.size();
        int newCount = results.size();
        mData.addAll(results);
        notifyItemRangeInserted(lastSize + 1, newCount);
    }

    @Override
    public int getItemCount() {
        // 1 for Story
        return 1 + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return ITEM_VIEW_TYPE_HEADER_STORY;
        else return super.getItemViewType(position);
    }

    @Override
    protected boolean isViewTypeItem(int viewType) {
        return viewType == ITEM_VIEW_TYPE_HEADER_STORY || super.isViewTypeItem(viewType);
    }

    @Override
    protected boolean isFooter(int position) {
        return position == mData.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_author)
        TextView txtAuthor;

        @BindView(R.id.txt_time)
        TextView txtTime;

        @BindView(R.id.txt_content)
        TextView txtContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
