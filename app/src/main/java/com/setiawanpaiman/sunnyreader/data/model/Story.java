package com.setiawanpaiman.sunnyreader.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Story {

    @SerializedName("id")
    private long mId;

    @SerializedName("time")
    private long mTimestamp;

    @SerializedName("by")
    private String mAuthor;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("url")
    private String mUrl;

    @SerializedName("score")
    private long mScore;

    @SerializedName("kids")
    private List<Long> mCommentIds;

    protected Story(Builder builder) {
        mId = builder.mId;
        mTimestamp = builder.mTimestamp;
        mAuthor = builder.mAuthor;
        mTitle = builder.mTitle;
        mUrl = builder.mUrl;
        mScore = builder.mScore;
        mCommentIds = builder.mCommentIds;
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

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public long getScore() {
        return mScore;
    }

    @NonNull
    public List<Long> getCommentIds() {
        // defensive copy
        if (mCommentIds == null) return new ArrayList<>();
        else return new ArrayList<>(mCommentIds);
    }

    public static Builder newBuilder(long id) {
        return new Builder(id);
    }

    public static class Builder {
        private long mId;
        private long mTimestamp;
        private String mAuthor;
        private String mTitle;
        private String mUrl;
        private long mScore;
        private List<Long> mCommentIds;

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

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setScore(long score) {
            mScore = score;
            return this;
        }

        public Builder setCommentIds(List<Long> commentIds) {
            mCommentIds.clear();
            if (commentIds != null) mCommentIds.addAll(commentIds);
            return this;
        }

        public Story build() {
            return new Story(this);
        }
    }
}