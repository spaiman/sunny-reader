package com.setiawanpaiman.sunnyreader.testcase.data.model;

import android.os.Parcel;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class StoryTest extends BaseTest {

    private Story mStory;

    @Before
    public void setUp() throws Exception {
        mStory = Story.newBuilder(1L)
                .setAuthor("author")
                .setCommentIds(Arrays.asList(2L, 3L, 4L))
                .setScore(100L)
                .setTimestamp(10293819023L)
                .setTitle("title")
                .setUrl("url")
                .setTotalComments(3)
                .build();

    }

    @Test
    public void testConstructStory() throws Exception {
        assertEquals(1L, mStory.getId());
        assertEquals("author", mStory.getAuthor());
        assertEquals(Arrays.asList(2L, 3L, 4L), mStory.getCommentIds());
        assertEquals(100L, mStory.getScore());
        assertEquals(10293819023L, mStory.getTimestamp());
        assertEquals("title", mStory.getTitle());
        assertEquals("url", mStory.getUrl());
        assertEquals(3, mStory.getTotalComments());
        assertEquals(0, mStory.describeContents());
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();
        mStory.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Story parceledStory = Story.CREATOR.createFromParcel(parcel);
        assertEquals(mStory.getId(), parceledStory.getId());
        assertEquals(mStory.getAuthor(), parceledStory.getAuthor());
        assertEquals(mStory.getCommentIds(), parceledStory.getCommentIds());
        assertEquals(mStory.getScore(), parceledStory.getScore());
        assertEquals(mStory.getTimestamp(), parceledStory.getTimestamp());
        assertEquals(mStory.getTitle(), parceledStory.getTitle());
        assertEquals(mStory.getUrl(), parceledStory.getUrl());
        assertEquals(mStory.getTotalComments(), parceledStory.getTotalComments());

        Story[] arrays = Story.CREATOR.newArray(10);
        assertEquals(10, arrays.length);
    }

    @Test
    public void testSetCommentsIdsNull() throws Exception {
        Story story = Story.newBuilder(1L)
                .setCommentIds(null)
                .build();
        assertEquals(new ArrayList<>(), story.getCommentIds());
    }

    @Test
    public void testCommentsIdsMemberVariableNull() throws Exception {
        // Need to use reflection to set mCommentIds to null
        //  because mCommentIds can become null when GSON deserialize it by using reflection
        Field field = Story.class.getDeclaredField("mCommentIds");
        field.setAccessible(true);
        field.set(mStory, null);

        assertEquals(new ArrayList<>(), mStory.getCommentIds());
    }
}
