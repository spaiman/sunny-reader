package com.setiawanpaiman.sunnyreader.testcase.util;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.mockdata.MockComment;
import com.setiawanpaiman.sunnyreader.mockdata.MockStory;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;
import com.setiawanpaiman.sunnyreader.util.GsonParser;
import com.setiawanpaiman.sunnyreader.util.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
public class GsonParserTest extends BaseTest {

    JsonParser mJsonParser;

    @Before
    public void setUp() throws Exception {
        mJsonParser = new GsonParser(getApplicationComponent().providesGson());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testToJsonSuccess() throws Exception {
        String json = mJsonParser.toJson(MockStory.STORY1, Story.class);
        JSONObject jsonObject = new JSONObject(json);
        assertEquals(MockStory.STORY1.getId(), jsonObject.getLong("id"));
        assertEquals(MockStory.STORY1.getAuthor(), jsonObject.getString("by"));
        assertEquals(MockStory.STORY1.getScore(), jsonObject.getLong("score"));
        assertEquals(MockStory.STORY1.getTimestamp(), jsonObject.getLong("time"));
        assertEquals(MockStory.STORY1.getTitle(), jsonObject.getString("title"));
        assertEquals(MockStory.STORY1.getUrl(), jsonObject.getString("url"));

        JSONArray jsonArray = jsonObject.getJSONArray("kids");
        assertEquals(MockStory.STORY1.getCommentIds().size(), jsonArray.length());
    }

    @Test(expected = Exception.class)
    public void testToJsonError() throws Exception {
        mJsonParser.toJson(MockComment.COMMENT1, Story.class);
    }

    @Test
    public void testFromJsonSuccess() throws Exception {
        Story story = mJsonParser.fromJson(MockStory.STORY1_JSON, Story.class);

        assertEquals(MockStory.STORY1.getId(), story.getId());
        assertEquals(MockStory.STORY1.getAuthor(), story.getAuthor());
        assertEquals(MockStory.STORY1.getScore(), story.getScore());
        assertEquals(MockStory.STORY1.getTimestamp(), story.getTimestamp());
        assertEquals(MockStory.STORY1.getTitle(), story.getTitle());
        assertEquals(MockStory.STORY1.getUrl(), story.getUrl());
        assertEquals(MockStory.STORY1.getCommentIds(), story.getCommentIds());
    }

    @Test(expected = Exception.class)
    public void testFromJsonError() throws Exception {
        Story story = mJsonParser.fromJson(MockStory.STORY_INVALID_JSON, Story.class);
    }
}
