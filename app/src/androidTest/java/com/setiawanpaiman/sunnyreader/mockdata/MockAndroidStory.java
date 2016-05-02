package com.setiawanpaiman.sunnyreader.mockdata;

import com.setiawanpaiman.sunnyreader.data.model.Story;

import java.util.ArrayList;
import java.util.List;

public class MockAndroidStory {

    public static Story create(long id, long timestamp, String author, String title, String text,
                               String url, long score, List<Long> commentIds, int totalComments) {
        return Story.newBuilder(id)
                .setTimestamp(timestamp)
                .setAuthor(author)
                .setTitle(title)
                .setText(text)
                .setUrl(url)
                .setScore(score)
                .setCommentIds(commentIds)
                .setTotalComments(totalComments)
                .build();
    }

    public static Story generateMockStory(long id) {
        return create(id, 1462133885L - (id * 1000), "Author " + id, "Title " + id,
                "Text " + id, "http://www.domain" + id + ".com/url", id,
                new ArrayList<Long>(), (int) id * 10);
    }

    public static Story generateMockStoryNoUrl(long id) {
        return create(id, 1462133885L - (id * 1000), "Author " + id, "Title " + id,
                "Text " + id, null, id, new ArrayList<Long>(), (int) id * 10);
    }

    public static Story generateMockStoryNoText(long id) {
        return create(id, 1462133885L - (id * 1000), "Author " + id, "Title " + id,
                null, "http://www.domain" + id + ".com/url", id, new ArrayList<Long>(), (int) id * 10);
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

    public static List<Story> generateMockStoriesNoText(long start, long count) {
        List<Story> stories = new ArrayList<>();
        for (long i = start; i < start + count; i++) {
            stories.add(generateMockStoryNoText(i));
        }
        return stories;
    }
}
