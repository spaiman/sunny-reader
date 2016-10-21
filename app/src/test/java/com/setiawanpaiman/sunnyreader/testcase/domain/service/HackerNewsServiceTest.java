package com.setiawanpaiman.sunnyreader.testcase.domain.service;

import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.api.HackerNewsApi;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsPersistent;
import com.setiawanpaiman.sunnyreader.domain.service.HackerNewsService;
import com.setiawanpaiman.sunnyreader.mockdata.MockComment;
import com.setiawanpaiman.sunnyreader.mockdata.MockStory;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class HackerNewsServiceTest extends BaseTest {

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    HackerNewsApi mHackerNewsApi;

    @Mock
    HackerNewsPersistent mHackerNewsPersistent;

    @InjectMocks
    HackerNewsService mHackerNewsService;

    private Answer<Observable<Story>> storiesAnswer = new Answer<Observable<Story>>() {
        @Override
        public Observable<Story> answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            Long storyId = (Long) args[0];
            return Observable.just(MockStory.MAP_STORY.get(storyId));
        }
    };

    private Answer<Observable<Comment>> commentsAnswer = new Answer<Observable<Comment>>() {
        @Override
        public Observable<Comment> answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            Long commentId = (Long) args[0];
            return Observable.just(MockComment.MAP_COMMENT.get(commentId));
        }
    };

    public static List<Long> initTopStories(long from, long to) {
        List<Long> list = new ArrayList<>();
        for (long i = from; i <= to; i++) {
            list.add(i);
        }
        return list;
    }

    @Before
    public void setUp() throws Exception {
        MockComment.resetDepth();
    }

    @Test
    public void testGetTopStoriesFirstPage() throws Exception {
        when(mHackerNewsApi.getTopStories()).thenReturn(Observable.just(initTopStories(1, 5)));
        when(mHackerNewsPersistent.getTopStories(anyInt(), anyInt())).thenReturn(Observable.just(initTopStories(2, 5)));
        when(mHackerNewsApi.getStory(anyLong())).thenAnswer(storiesAnswer);

        TestSubscriber<List<Story>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(10).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyListOf(Long.class));
        verify(mHackerNewsPersistent, times(2)).getTopStories(0, 10);
        verify(mHackerNewsApi, times(4)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(4)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents().get(0);
        assertEquals(4, emittedStories.size());
        assertEquals(MockStory.STORY2, emittedStories.get(0));
        assertEquals(MockStory.STORY3, emittedStories.get(1));
        assertEquals(MockStory.STORY4, emittedStories.get(2));
        assertEquals(MockStory.STORY5, emittedStories.get(3));
    }

    @Test
    public void testGetTopStoriesOtherPage() throws Exception {
        when(mHackerNewsApi.getTopStories()).thenReturn(Observable.just(initTopStories(1, 5)));
        when(mHackerNewsPersistent.getTopStories(anyInt(), anyInt())).thenReturn(Observable.just(initTopStories(3, 4)));
        when(mHackerNewsApi.getStory(anyLong())).thenAnswer(storiesAnswer);

        TestSubscriber<List<Story>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(2, 10).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(0)).getTopStories();
        verify(mHackerNewsPersistent, times(0)).saveTopStories(anyListOf(Long.class));
        verify(mHackerNewsPersistent, times(1)).getTopStories(10, 10);
        verify(mHackerNewsApi, times(2)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(2)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents().get(0);
        assertEquals(2, emittedStories.size());
        assertEquals(MockStory.STORY3, emittedStories.get(0));
        assertEquals(MockStory.STORY4, emittedStories.get(1));
    }

    @Test
    public void testGetTopStoriesOtherPageWithEmptyPersistent() throws Exception {
        when(mHackerNewsApi.getTopStories()).thenReturn(Observable.just(initTopStories(1, 5)));
        when(mHackerNewsPersistent.getTopStories(anyInt(), anyInt()))
                .thenReturn(Observable.<List<Long>>empty()).thenReturn(Observable.just(initTopStories(2, 4)));
        when(mHackerNewsApi.getStory(anyLong())).thenAnswer(storiesAnswer);

        TestSubscriber<List<Story>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(2, 10).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyListOf(Long.class));
        verify(mHackerNewsPersistent, times(3)).getTopStories(10, 10);
        verify(mHackerNewsApi, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(3)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents().get(0);
        assertEquals(3, emittedStories.size());
        assertEquals(MockStory.STORY2, emittedStories.get(0));
        assertEquals(MockStory.STORY3, emittedStories.get(1));
        assertEquals(MockStory.STORY4, emittedStories.get(2));
    }

    @Test
    public void testGetTopStoriesFirstPageWithGetStoryApiNullTwice() throws Exception {
        when(mHackerNewsApi.getTopStories()).thenReturn(Observable.just(initTopStories(1, 5)));
        when(mHackerNewsPersistent.getTopStories(anyInt(), anyInt())).thenReturn(Observable.just(initTopStories(2, 4)));
        when(mHackerNewsApi.getStory(anyLong()))
                .thenReturn(Observable.<Story> just(null))
                .thenReturn(Observable.<Story> just(null))
                .thenReturn(Observable.just(MockStory.STORY4));
        when(mHackerNewsPersistent.getStory(anyLong())).thenAnswer(storiesAnswer);

        TestSubscriber<List<Story>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(10).observeOn(AndroidSchedulers.mainThread(), true).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyListOf(Long.class));
        verify(mHackerNewsPersistent, times(2)).getTopStories(0, 10);
        verify(mHackerNewsApi, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(1)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents().get(0);
        assertEquals(1, emittedStories.size());
        assertEquals(MockStory.STORY4, emittedStories.get(0));
    }

    @Test
    public void testGetTopStoriesFirstPageWithGetStoryApiErrorOnce() throws Exception {
        when(mHackerNewsApi.getTopStories()).thenReturn(Observable.just(initTopStories(1, 5)));
        when(mHackerNewsPersistent.getTopStories(anyInt(), anyInt())).thenReturn(Observable.just(initTopStories(2, 4)));
        when(mHackerNewsApi.getStory(anyLong()))
                .thenReturn(Observable.<Story> error(new Exception())).thenAnswer(storiesAnswer);
        when(mHackerNewsPersistent.getStory(anyLong())).thenReturn(Observable.just(MockStory.STORY2));

        TestSubscriber<List<Story>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(10).observeOn(AndroidSchedulers.mainThread(), true).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyListOf(Long.class));
        verify(mHackerNewsPersistent, times(2)).getTopStories(0, 10);
        verify(mHackerNewsApi, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(2)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents().get(0);
        assertEquals(3, emittedStories.size());
        assertEquals(MockStory.STORY2, emittedStories.get(0));
        assertEquals(MockStory.STORY3, emittedStories.get(1));
        assertEquals(MockStory.STORY4, emittedStories.get(2));
    }

    @Test
    public void testGetComments() throws Exception {
        when(mHackerNewsApi.getComment(anyLong())).thenAnswer(commentsAnswer);
        when(mHackerNewsPersistent.getComment(anyLong())).thenAnswer(commentsAnswer);

        TestSubscriber<List<Comment>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getComments(MockStory.STORY_COMMENT2, 10).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(6)).getComment(anyLong());
        verify(mHackerNewsPersistent, times(6)).saveComment(any(Comment.class));
        testSubscriber.assertNoErrors();
        List<Comment> emittedComments = testSubscriber.getOnNextEvents().get(0);
        assertEquals(6, emittedComments.size());
        assertEquals(MockComment.COMMENT2, emittedComments.get(0));
        assertEquals(MockComment.COMMENT21, emittedComments.get(1));
        assertEquals(MockComment.COMMENT211, emittedComments.get(2));
        assertEquals(MockComment.COMMENT22, emittedComments.get(3));
        assertEquals(MockComment.COMMENT221, emittedComments.get(4));
        assertEquals(MockComment.COMMENT222, emittedComments.get(5));

        assertEquals(0, emittedComments.get(0).getDepth());
        assertEquals(1, emittedComments.get(1).getDepth());
        assertEquals(2, emittedComments.get(2).getDepth());
        assertEquals(1, emittedComments.get(3).getDepth());
        assertEquals(2, emittedComments.get(4).getDepth());
        assertEquals(2, emittedComments.get(5).getDepth());
    }

    @Test
    public void testGetCommentsWithADeletedComment() throws Exception {
        when(mHackerNewsApi.getComment(anyLong())).thenAnswer(commentsAnswer);
        when(mHackerNewsPersistent.getComment(anyLong())).thenAnswer(commentsAnswer);

        TestSubscriber<List<Comment>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getComments(MockStory.STORY_COMMENT3, 10).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(6)).getComment(anyLong());
        verify(mHackerNewsPersistent, times(6)).saveComment(any(Comment.class));
        testSubscriber.assertNoErrors();
        List<Comment> emittedComments = testSubscriber.getOnNextEvents().get(0);
        assertEquals(3, emittedComments.size());
        assertEquals(MockComment.COMMENT3, emittedComments.get(0));
        assertEquals(MockComment.COMMENT32, emittedComments.get(1));
        assertEquals(MockComment.COMMENT321, emittedComments.get(2));

        assertEquals(0, emittedComments.get(0).getDepth());
        assertEquals(1, emittedComments.get(1).getDepth());
        assertEquals(2, emittedComments.get(2).getDepth());
    }

    @Test
    public void testGetCommentsWithGetCommentApiNullOnce() throws Exception {
        when(mHackerNewsApi.getComment(anyLong()))
                .thenAnswer(commentsAnswer)
                .thenReturn(Observable.<Comment> just(null)).thenAnswer(commentsAnswer);
        when(mHackerNewsPersistent.getComment(anyLong())).thenAnswer(commentsAnswer);

        TestSubscriber<List<Comment>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getComments(MockStory.STORY_COMMENT3, 10).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(4)).getComment(anyLong());
        verify(mHackerNewsPersistent, times(3)).saveComment(any(Comment.class));
        testSubscriber.assertNoErrors();
        List<Comment> emittedComments = testSubscriber.getOnNextEvents().get(0);
        assertEquals(3, emittedComments.size());
        assertEquals(MockComment.COMMENT3, emittedComments.get(0));
        assertEquals(MockComment.COMMENT32, emittedComments.get(1));
        assertEquals(MockComment.COMMENT321, emittedComments.get(2));

        assertEquals(0, emittedComments.get(0).getDepth());
        assertEquals(1, emittedComments.get(1).getDepth());
        assertEquals(2, emittedComments.get(2).getDepth());
    }

    @Test
    public void testGetCommentsWithGetCommentApiErrorOnce() throws Exception {
        when(mHackerNewsApi.getComment(anyLong()))
                .thenAnswer(commentsAnswer)
                .thenReturn(Observable.<Comment> error(new Exception())).thenAnswer(commentsAnswer);
        when(mHackerNewsPersistent.getComment(anyLong()))
                .thenAnswer(commentsAnswer)
                .thenReturn(Observable.just(MockComment.COMMENT22)).thenAnswer(commentsAnswer);

        TestSubscriber<List<Comment>> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getComments(MockStory.STORY_COMMENT3, 10).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(6)).getComment(anyLong());
        verify(mHackerNewsPersistent, times(5)).saveComment(any(Comment.class));
        testSubscriber.assertNoErrors();
        List<Comment> emittedComments = testSubscriber.getOnNextEvents().get(0);
        assertEquals(3, emittedComments.size());
        assertEquals(MockComment.COMMENT3, emittedComments.get(0));
        assertEquals(MockComment.COMMENT32, emittedComments.get(1));
        assertEquals(MockComment.COMMENT321, emittedComments.get(2));

        assertEquals(0, emittedComments.get(0).getDepth());
        assertEquals(1, emittedComments.get(1).getDepth());
        assertEquals(2, emittedComments.get(2).getDepth());
    }
}
