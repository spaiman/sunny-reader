package com.setiawanpaiman.sunnyreader.mockdata;

import com.setiawanpaiman.sunnyreader.data.model.Story;

import java.util.ArrayList;
import java.util.List;

public class MockAndroidStory {

    public static Story create(long id, long timestamp, String author, String title,
                               String url, long score, List<Long> commentIds, int totalComments) {
        return Story.newBuilder(id)
                .setTimestamp(timestamp)
                .setAuthor(author)
                .setTitle(title)
                .setUrl(url)
                .setScore(score)
                .setCommentIds(commentIds)
                .setTotalComments(totalComments)
                .build();
    }

    public static Story generateMockStory(long id) {
        return create(id, 1462133885L - (id * 1000), "Author " + id, "Title " + id,
                "http://www.domain" + id + ".com/url", id,
                new ArrayList<Long>(), (int) id * 10);
    }

    public static Story generateMockStoryNoUrl(long id) {
        return create(id, 1462133885L - (id * 1000), "Author " + id, "Title " + id,
                null, id, new ArrayList<Long>(), (int) id * 10);
    }

    public static List<Story> generateMockStories(long start, long count) {
        List<Story> stories = new ArrayList<>();
        for (long i = start; i < start + count; i++) {
            stories.add(generateMockStory(i));
        }
        return stories;
    }

    public static List<Story> generateMockStoriesNoUrl(long start, long count) {
        List<Story> stories = new ArrayList<>();
        for (long i = start; i < start + count; i++) {
            stories.add(generateMockStoryNoUrl(i));
        }
        return stories;
    }
}
