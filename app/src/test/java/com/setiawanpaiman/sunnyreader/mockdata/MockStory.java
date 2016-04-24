package com.setiawanpaiman.sunnyreader.mockdata;

import com.setiawanpaiman.sunnyreader.data.model.Story;

import java.util.ArrayList;
import java.util.List;

public class MockStory {

    public static Story STORY1 = create(1L, 1023923011L, "Author 1", "Title 1", "http://www.url.com/url1", 10L, new ArrayList<Long>());
    public static Story STORY2 = create(2L, 1023923012L, "Author 2", "Title 2", "http://www.url.com/url2", 20L, new ArrayList<Long>());
    public static Story STORY3 = create(3L, 1023923013L, "Author 3", "Title 3", "http://www.url.com/url3", 30L, new ArrayList<Long>());

    public static String STORY1_JSON = "{\n"
            + "  \"by\" : \"Author 1\",\n"
            + "  \"descendants\" : 0,\n"
            + "  \"id\" : 1,\n"
            + "  \"kids\" : [ ],\n"
            + "  \"score\" : 10,\n"
            + "  \"time\" : 1023923011,\n"
            + "  \"title\" : \"Title 1\",\n"
            + "  \"type\" : \"story\",\n"
            + "  \"url\" : \"http://www.url.com/url1\"\n"
            + "}";

    public static String STORY2_JSON = "{\n"
            + "  \"by\" : \"Author 2\",\n"
            + "  \"descendants\" : 0,\n"
            + "  \"id\" : 2,\n"
            + "  \"kids\" : [ ],\n"
            + "  \"score\" : 20,\n"
            + "  \"time\" : 1023923012,\n"
            + "  \"title\" : \"Title 2\",\n"
            + "  \"type\" : \"story\",\n"
            + "  \"url\" : \"http://www.url.com/url2\"\n"
            + "}";

    public static String STORY3_JSON = "{\n"
            + "  \"by\" : \"Author 3\",\n"
            + "  \"descendants\" : 0,\n"
            + "  \"id\" : 3,\n"
            + "  \"kids\" : [ ],\n"
            + "  \"score\" : 30,\n"
            + "  \"time\" : 1023923013,\n"
            + "  \"title\" : \"Title 3\",\n"
            + "  \"type\" : \"story\",\n"
            + "  \"url\" : \"http://www.url.com/url3\"\n"
            + "}";

    public static String STORY_UNKNOWN_JSON = "{\n"
            + "  \"id\" : 1,\n"
            + "  \"invalid\" : \"Invalid value\",\n"
            + "}";

    public static String STORY_INVALID_JSON = "{\n"
            + "  []\n"
            + "}";

    public static Story create(long id, long timestamp, String author, String title,
                               String url, long score, List<Long> commentIds) {
        return Story.newBuilder(id)
                .setTimestamp(timestamp)
                .setAuthor(author)
                .setTitle(title)
                .setUrl(url)
                .setScore(score)
                .setCommentIds(commentIds)
                .build();
    }
}
