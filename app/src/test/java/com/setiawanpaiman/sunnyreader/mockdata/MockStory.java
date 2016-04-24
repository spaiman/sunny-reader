package com.setiawanpaiman.sunnyreader.mockdata;

import com.setiawanpaiman.sunnyreader.data.model.Story;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockStory {

    public static Story STORY1 = create(1L, 1023923011L, "Author 1", "Title 1", "http://www.url.com/url1", 10L, new ArrayList<Long>());
    public static Story STORY2 = create(2L, 1023923012L, "Author 2", "Title 2", "http://www.url.com/url2", 20L, new ArrayList<Long>());
    public static Story STORY3 = create(3L, 1023923013L, "Author 3", "Title 3", "http://www.url.com/url3", 30L, new ArrayList<Long>());
    public static Story STORY4 = create(4L, 1023923014L, "Author 4", "Title 4", "http://www.url.com/url4", 40L, new ArrayList<Long>());
    public static Story STORY5 = create(5L, 1023923015L, "Author 5", "Title 5", "http://www.url.com/url5", 50L, new ArrayList<Long>());
    public static Story STORY6 = create(6L, 1023923016L, "Author 6", "Title 6", "http://www.url.com/url6", 60L, new ArrayList<Long>());

    // Stories with comments
    public static Story STORY_COMMENT2 = create(7L, 1023923017L, "Author 7", "Title 7", "http://www.url.com/url7", 70L, Arrays.asList(2L));
    public static Story STORY_COMMENT3 = create(8L, 1023923018L, "Author 8", "Title 8", "http://www.url.com/url8", 80L, Arrays.asList(3L));

    public static Map<Long, Story> MAP_STORY;

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

    public static String STORY_UNKNOWN_JSON = "{\n"
            + "  \"id\" : 1,\n"
            + "  \"invalid\" : \"Invalid value\",\n"
            + "}";

    public static String STORY_INVALID_JSON = "{\n"
            + "  []\n"
            + "}";

    public static Story create(long id, long timestamp, String author, String title,
                               String url, long score, List<Long> commentIds) {
        Story story = Story.newBuilder(id)
                .setTimestamp(timestamp)
                .setAuthor(author)
                .setTitle(title)
                .setUrl(url)
                .setScore(score)
                .setCommentIds(commentIds)
                .build();

        if (MAP_STORY == null) MAP_STORY = new HashMap<>();
        MAP_STORY.put(id, story);

        return story;
    }
}
