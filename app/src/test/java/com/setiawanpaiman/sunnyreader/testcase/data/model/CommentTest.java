package com.setiawanpaiman.sunnyreader.testcase.data.model;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import java.util.Arrays;

@RunWith(RobolectricGradleTestRunner.class)
public class CommentTest extends BaseTest {

    @Test
    public void testConstructComment() throws Exception {
        Comment comment = Comment.newBuilder(10L)
                .setCommentIds(Arrays.asList(20L, 30L, 40L))
                .setAuthor("author comment")
                .setDeleted(true)
                .setParentId(109L)
                .setText("text comment")
                .setTimestamp(120398123L)
                .build();
        comment.setDepth(3);

        Assert.assertEquals(10L, comment.getId());
        Assert.assertEquals(Arrays.asList(20L, 30L, 40L), comment.getCommentIds());
        Assert.assertEquals("author comment", comment.getAuthor());
        Assert.assertEquals(true, comment.isDeleted());
        Assert.assertEquals(109L, comment.getParentId());
        Assert.assertEquals("text comment", comment.getText());
        Assert.assertEquals(120398123L, comment.getTimestamp());
        Assert.assertEquals(3, comment.getDepth());
    }
}
