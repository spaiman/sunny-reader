package com.setiawanpaiman.sunnyreader.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Story implements Parcelable {

    @SerializedName("id")
    private long mId;

    @SerializedName("time")
    private long mTimestamp;

    @SerializedName("by")
    private String mAuthor;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("text")
    private String mText;

    @SerializedName("url")
    private String mUrl;

    @SerializedName("score")
    private long mScore;

    @SerializedName("kids")
    private List<Long> mCommentIds;

    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    protected Story(Builder builder) {
        mId = builder.mId;
        mTimestamp = builder.mTimestamp;
        mAuthor = builder.mAuthor;
        mTitle = builder.mTitle;
        mText = builder.mText;
        mUrl = builder.mUrl;
        mScore = builder.mScore;
        mCommentIds = builder.mCommentIds;
    }

    protected Story(Parcel in) {
        this.mId = in.readLong();
        this.mTimestamp = in.readLong();
        this.mAuthor = in.readString();
        this.mTitle = in.readString();
        this.mText = in.readString();
        this.mUrl = in.readString();
        this.mScore = in.readLong();
        this.mCommentIds = new ArrayList<>();
        in.readList(this.mCommentIds, Long.class.getClassLoader());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeLong(this.mTimestamp);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mTitle);
        dest.writeString(this.mText);
        dest.writeString(this.mUrl);
        dest.writeLong(this.mScore);
        dest.writeList(this.mCommentIds);
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

    public String getText() {
        return mText;
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

    public int getTotalComments() {
        if (mCommentIds == null) return 0;
        else return mCommentIds.size();
    }

    public static Builder newBuilder(long id) {
        return new Builder(id);
    }

    public static class Builder {
        private long mId;
        private long mTimestamp;
        private String mAuthor;
        private String mTitle;
        private String mText;
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

        public Builder setText(String text) {
            mText = text;
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