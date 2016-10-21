package com.setiawanpaiman.sunnyreader.domain.persistent;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;

import java.util.List;

import rx.Observable;

public interface HackerNewsPersistent {

    Observable<List<Long>> getTopStories(int start, int count);

    Observable<Story> getStory(long storyId);

    Observable<Comment> getComment(long commentId);

    void saveTopStories(List<Long> topStoriesId);

    void saveStory(Story story);

    void saveComment(Comment comment);
}
