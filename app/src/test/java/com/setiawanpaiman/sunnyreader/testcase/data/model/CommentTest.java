package com.setiawanpaiman.sunnyreader.testcase.data.model;

import android.os.Parcel;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
public class CommentTest extends BaseTest {

    private Comment mComment;

    @Before
    public void setUp() throws Exception {
        mComment = Comment.newBuilder(10L)
                .setCommentIds(Arrays.asList(20L, 30L, 40L))
                .setAuthor("author comment")
                .setDeleted(false)
                .setParentId(109L)
                .setText("text comment")
                .setTimestamp(120398123L)
                .build();
        mComment.setDepth(3);
    }

    @Test
    public void testConstructComment() throws Exception {
        Assert.assertEquals(10L, mComment.getId());
        Assert.assertEquals(Arrays.asList(20L, 30L, 40L), mComment.getCommentIds());
        Assert.assertEquals("author comment", mComment.getAuthor());
        Assert.assertEquals(false, mComment.isDeleted());
        Assert.assertEquals(109L, mComment.getParentId());
        Assert.assertEquals("text comment", mComment.getText());
        Assert.assertEquals(120398123L, mComment.getTimestamp());
        Assert.assertEquals(3, mComment.getDepth());
        assertEquals(0, mComment.describeContents());
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();
        mComment.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Comment parceledComment = Comment.CREATOR.createFromParcel(parcel);
        assertEquals(mComment.getId(), parceledComment.getId());
        assertEquals(mComment.getTimestamp(), parceledComment.getTimestamp());
        assertEquals(mComment.getAuthor(), parceledComment.getAuthor());
        assertEquals(mComment.getText(), parceledComment.getText());
        assertEquals(mComment.getParentId(), parceledComment.getParentId());
        assertEquals(mComment.getCommentIds(), parceledComment.getCommentIds());
        assertEquals(mComment.isDeleted(), parceledComment.isDeleted());
        assertEquals(mComment.getDepth(), parceledComment.getDepth());

        Comment[] arrays = Comment.CREATOR.newArray(10);
        assertEquals(10, arrays.length);
    }

    @Test
    public void testParcelableDeleted() throws Exception {
        Comment comment = Comment.newBuilder(10L)
                .setCommentIds(Arrays.asList(20L, 30L, 40L))
                .setAuthor("author comment")
                .setDeleted(true)
                .setParentId(109L)
                .setText("text comment")
                .setTimestamp(120398123L)
                .build();
        comment.setDepth(3);
        Parcel parcel = Parcel.obtain();
        comment.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Comment parceledComment = Comment.CREATOR.createFromParcel(parcel);
        assertEquals(comment.getId(), parceledComment.getId());
        assertEquals(comment.getTimestamp(), parceledComment.getTimestamp());
        assertEquals(comment.getAuthor(), parceledComment.getAuthor());
        assertEquals(comment.getText(), parceledComment.getText());
        assertEquals(comment.getParentId(), parceledComment.getParentId());
        assertEquals(comment.getCommentIds(), parceledComment.getCommentIds());
        assertEquals(comment.isDeleted(), parceledComment.isDeleted());
        assertEquals(comment.getDepth(), parceledComment.getDepth());
    }

    @Test
    public void testSetCommentsIdsNull() throws Exception {
        Comment comment = Comment.newBuilder(1L)
                .setCommentIds(null)
                .build();
        assertEquals(new ArrayList<>(), comment.getCommentIds());
    }

    @Test
    public void testCommentsIdsMemberVariableNull() throws Exception {
        // Need to use reflection to set mCommentIds to null
        //  because mCommentIds can become null when GSON deserialize it by using reflection
        Field field = Comment.class.getDeclaredField("mCommentIds");
        field.setAccessible(true);
        field.set(mComment, null);

        assertEquals(new ArrayList<>(), mComment.getCommentIds());
    }
}
