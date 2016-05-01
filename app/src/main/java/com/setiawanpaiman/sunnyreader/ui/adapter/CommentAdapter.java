package com.setiawanpaiman.sunnyreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.SparseBooleanArray;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends EndlessListAdapter<Comment, RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER_STORY = -1;
    private static final int MAX_DP_ELEVATION = 6;

    private Story mStory;
    private List<Comment> mAllComments;
    private SparseBooleanArray mExpanded;

    public CommentAdapter(Context context, Story story) {
        super(context);
        mStory = story;
        mAllComments = new ArrayList<>();
        mExpanded = new SparseBooleanArray();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderItem(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == ITEM_VIEW_TYPE_HEADER_STORY) {
            final View view = inflater.inflate(R.layout.item_story_detail, parent, false);
            return new StoryDetailViewHolder(view);
        } else {
            final View view = inflater.inflate(R.layout.item_comment, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = vh.getAdapterPosition();
                    if (pos >= 0) {
                        if (isExpanded(pos)) {
                            collapseComments(pos);
                        } else {
                            expandComments(pos);
                        }
                    }
                }
            };
            vh.contentContainer.setOnClickListener(onClickListener);
            vh.txtContent.setOnClickListener(onClickListener);
            return vh;
        }
    }

    @Override
    protected void onBindViewHolderItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoryDetailViewHolder) {
            ((StoryDetailViewHolder) holder).bind(mContext, mStory);
        } else if (holder instanceof ViewHolder) {
            Comment comment = mData.get(position - 1);
            ViewHolder vh = (ViewHolder) holder;
            vh.txtAuthor.setText(comment.getAuthor());
            if (comment.getTotalReplies() > 0 && !isExpanded(position)) {
                vh.txtTotalReplies.setText(mContext.getResources().getQuantityString(
                        R.plurals.total_replies, comment.getTotalReplies(),
                        comment.getTotalReplies()));
                vh.txtTotalReplies.setVisibility(View.VISIBLE);
            } else {
                vh.txtTotalReplies.setVisibility(View.GONE);
            }
            vh.txtTime.setText(DateUtils.getRelativeTimeSpanString(
                    TimeUnit.SECONDS.toMillis(comment.getTimestamp()), System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE));
            vh.txtContent.setText(AndroidUtils.trim(Html.fromHtml(comment.getText())));
            vh.txtContent.setMovementMethod(LinkMovementMethod.getInstance());
            vh.cardView.setCardElevation(
                    AndroidUtils.dpToPx(mContext, MAX_DP_ELEVATION - comment.getDepth()));
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) vh.itemView.getLayoutParams();
            marginLayoutParams.leftMargin = comment.getDepth() *
                    mContext.getResources().getDimensionPixelSize(R.dimen.spacing_xsmall);
        }
    }

    @Override
    public void addAll(List<Comment> results, boolean refresh) {
        if (refresh) {
            clear();
        }
        int oldSize = mData.size();
        mAllComments.addAll(results);
        int addedCount = 0;
        for (Comment newComment : results) {
            if (newComment.getDepth() <= 0) {
                mData.add(newComment);
                addedCount++;
            }
        }

        notifyItemRangeInserted(oldSize + 1, addedCount);
    }

    private boolean isExpanded(int adapterPos) {
        Comment comment = mData.get(adapterPos - 1);
        int allPos = mAllComments.indexOf(comment);
        return mExpanded.get(allPos, false);
    }

    private void expandComments(int adapterPos) {
        int actualPos = adapterPos - 1;
        Comment parentComment = mData.get(actualPos);
        int parentDepth = parentComment.getDepth();
        int parentPos = mAllComments.indexOf(parentComment);
        int addedCount = 0;
        int maxDepth = parentDepth + 1;
        for (int i = parentPos + 1, len = mAllComments.size(); i < len; i++) {
            Comment childComment = mAllComments.get(i);
            if (childComment.getDepth() < maxDepth) maxDepth--;

            if (childComment.getDepth() <= parentDepth) break;
            else if (childComment.getDepth() <= maxDepth) {
                if (mExpanded.get(i, false)) maxDepth = childComment.getDepth() + 1;
                mData.add(actualPos + addedCount + 1, childComment);
                addedCount++;
            }
        }
        mExpanded.put(parentPos, true);

        notifyItemChanged(actualPos + 1);
        notifyItemRangeInserted(actualPos + 2, addedCount);
    }

    private void collapseComments(int adapterPos) {
        int actualPos = adapterPos - 1;
        Comment parentComment = mData.get(actualPos);
        int parentDepth = parentComment.getDepth();
        int parentPos = mAllComments.indexOf(parentComment);
        int removedCount = 0;
        Iterator<Comment> iterator = mData.listIterator(actualPos + 1);
        while (iterator.hasNext()) {
            Comment childComment = iterator.next();
            if (childComment.getDepth() <= parentDepth) break;

            iterator.remove();
            removedCount++;
        }
        mExpanded.put(parentPos, false);

        notifyItemChanged(actualPos + 1);
        notifyItemRangeRemoved(actualPos + 2, removedCount);
    }

    @Override
    public void clear() {
        super.clear();
        mAllComments.clear();
        mExpanded.clear();
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
    public int getFooterPosition() {
        return super.getFooterPosition() + 1;
    }

    static class StoryDetailViewHolder extends StoryAdapter.ViewHolder {

        @BindView(R.id.txt_content)
        TextView txtContent;

        public StoryDetailViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void bind(Context context, Story story) {
            super.bind(context, story);
            if (!TextUtils.isEmpty(story.getText())) {
                txtContent.setText(AndroidUtils.trim(Html.fromHtml(story.getText())));
                txtContent.setMovementMethod(LinkMovementMethod.getInstance());
                txtContent.setVisibility(View.VISIBLE);
            } else {
                txtContent.setVisibility(View.GONE);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.content_container)
        View contentContainer;

        @BindView(R.id.txt_author)
        TextView txtAuthor;

        @BindView(R.id.txt_total_replies)
        TextView txtTotalReplies;

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
