package com.setiawanpaiman.sunnyreader.testcase.data.model;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
public class StoryTest extends BaseTest {

    @Test
    public void testConstructStory() throws Exception {
        Story story = Story.newBuilder(1L)
                .setAuthor("author")
                .setCommentIds(Arrays.asList(2L, 3L, 4L))
                .setScore(100L)
                .setTimestamp(10293819023L)
                .setTitle("title")
                .setUrl("url")
                .build();

        assertEquals(1L, story.getId());
        assertEquals("author", story.getAuthor());
        assertEquals(Arrays.asList(2L, 3L, 4L), story.getCommentIds());
        assertEquals(100L, story.getScore());
        assertEquals(10293819023L, story.getTimestamp());
        assertEquals("title", story.getTitle());
        assertEquals("url", story.getUrl());
    }
}
