package com.setiawanpaiman.sunnyreader.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Comment {

    @SerializedName("id")
    private long mId;

    @SerializedName("time")
    private long mTimestamp;

    @SerializedName("by")
    private String mAuthor;

    @SerializedName("text")
    private String mText;

    @SerializedName("parent")
    private long mParentId;

    @SerializedName("kids")
    private List<Long> mCommentIds;

    @SerializedName("deleted")
    private boolean mDeleted;

    private int mDepth;

    protected Comment(Builder builder) {
        mId = builder.mId;
        mTimestamp = builder.mTimestamp;
        mAuthor = builder.mAuthor;
        mText = builder.mText;
        mParentId = builder.mParentId;
        mCommentIds = builder.mCommentIds;
        mDeleted = builder.mDeleted;
    }

    public long getId() {
        return mId;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public long getParentId() {
        return mParentId;
    }

    @NonNull
    public List<Long> getCommentIds() {
        // defensive copy
        if (mCommentIds == null) return new ArrayList<>();
        else return new ArrayList<>(mCommentIds);
    }

    public String getText() {
        return mText;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public int getDepth() {
        return mDepth;
    }

    public void setDepth(int depth) {
        mDepth = depth;
    }

    public static Builder newBuilder(long id) {
        return new Builder(id);
    }

    public static class Builder {
        private long mId;
        private long mTimestamp;
        private String mAuthor;
        private long mParentId;
        private List<Long> mCommentIds;
        private String mText;
        private boolean mDeleted;

        public Builder(long id) {
            mId = id;
            mCommentIds = new ArrayList<>();
        }

        public Builder setTimestamp(long timestamp) {
            mTimestamp = timestamp;
            return this;
        }

        public Builder setAuthor(String author) {
            mAuthor = author;
            return this;
        }

        public Builder setParentId(long parentId) {
            mParentId = parentId;
            return this;
        }

        public Builder setCommentIds(List<Long> commentIds) {
            mCommentIds.clear();
            if (commentIds != null) mCommentIds.addAll(commentIds);
            return this;
        }

        public Builder setText(String text) {
            mText = text;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            mDeleted = deleted;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}
