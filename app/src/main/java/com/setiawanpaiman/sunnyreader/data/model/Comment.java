package com.setiawanpaiman.sunnyreader.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Comment implements Parcelable {

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {return new Comment(source);}

        @Override
        public Comment[] newArray(int size) {return new Comment[size];}
    };

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

    private int mTotalReplies;
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

    protected Comment(Parcel in) {
        this.mId = in.readLong();
        this.mTimestamp = in.readLong();
        this.mAuthor = in.readString();
        this.mText = in.readString();
        this.mParentId = in.readLong();
        this.mCommentIds = new ArrayList<Long>();
        in.readList(this.mCommentIds, Long.class.getClassLoader());
        this.mDeleted = in.readByte() != 0;
        this.mTotalReplies = in.readInt();
        this.mDepth = in.readInt();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeLong(this.mTimestamp);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mText);
        dest.writeLong(this.mParentId);
        dest.writeList(this.mCommentIds);
        dest.writeByte(mDeleted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mTotalReplies);
        dest.writeInt(this.mDepth);
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

    public int getTotalReplies() {
        return mTotalReplies;
    }

    public void setTotalReplies(int totalReplies) {
        mTotalReplies = totalReplies;
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
