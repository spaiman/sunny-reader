package com.setiawanpaiman.sunnyreader.testcase.domain.service;

import com.setiawanpaiman.sunnyreader.Constants;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.api.HackerNewsApi;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsPersistent;
import com.setiawanpaiman.sunnyreader.domain.service.HackerNewsService;
import com.setiawanpaiman.sunnyreader.mockdata.MockStory;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;

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

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
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

    public static List<Long> initTopStories(long from, long to) {
        List<Long> list = new ArrayList<>();
        for (long i = from; i <= to; i++) {
            list.add(i);
        }
        return list;
    }

    @Test
    public void testGetTopStoriesFirstPage() throws Exception {
        when(mHackerNewsApi.getTopStories()).thenReturn(Observable.just(initTopStories(1, 5)));
        when(mHackerNewsPersistent.getTopStories(anyInt(), anyInt())).thenReturn(Observable.just(initTopStories(2, 5)));
        when(mHackerNewsApi.getStory(anyLong())).thenAnswer(storiesAnswer);

        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories().subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyList());
        verify(mHackerNewsPersistent, times(1)).getTopStories(0, Constants.PER_PAGE);
        verify(mHackerNewsApi, times(4)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(4)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents();
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

        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(2).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(0)).getTopStories();
        verify(mHackerNewsPersistent, times(0)).saveTopStories(anyList());
        verify(mHackerNewsPersistent, times(1)).getTopStories(10, Constants.PER_PAGE);
        verify(mHackerNewsApi, times(2)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(2)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents();
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

        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(2).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyList());
        verify(mHackerNewsPersistent, times(2)).getTopStories(10, Constants.PER_PAGE);
        verify(mHackerNewsApi, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(3)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents();
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
                .thenReturn(Observable.just(MockStory.STORY1));
        when(mHackerNewsPersistent.getStory(anyLong())).thenAnswer(storiesAnswer);

        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(1).observeOn(AndroidSchedulers.mainThread(), true).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyList());
        verify(mHackerNewsPersistent, times(1)).getTopStories(0, Constants.PER_PAGE);
        verify(mHackerNewsApi, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(1)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents();
        assertEquals(1, emittedStories.size());
        assertEquals(MockStory.STORY1, emittedStories.get(0));
    }

    @Test
    public void testGetTopStoriesFirstPageWithGetStoryApiErrorOnce() throws Exception {
        when(mHackerNewsApi.getTopStories()).thenReturn(Observable.just(initTopStories(1, 5)));
        when(mHackerNewsPersistent.getTopStories(anyInt(), anyInt())).thenReturn(Observable.just(initTopStories(2, 4)));
        when(mHackerNewsApi.getStory(anyLong()))
                .thenReturn(Observable.<Story> error(new Exception(""))).thenAnswer(storiesAnswer);
        when(mHackerNewsPersistent.getStory(anyLong())).thenReturn(Observable.just(MockStory.STORY6));

        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsService.getTopStories(1).observeOn(AndroidSchedulers.mainThread(), true).subscribe(testSubscriber);
        verify(mHackerNewsApi, times(1)).getTopStories();
        verify(mHackerNewsPersistent, times(1)).saveTopStories(anyList());
        verify(mHackerNewsPersistent, times(1)).getTopStories(0, Constants.PER_PAGE);
        verify(mHackerNewsApi, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(3)).getStory(anyLong());
        verify(mHackerNewsPersistent, times(2)).saveStory(any(Story.class));
        testSubscriber.assertNoErrors();
        List<Story> emittedStories = testSubscriber.getOnNextEvents();
        assertEquals(3, emittedStories.size());
        assertEquals(MockStory.STORY6, emittedStories.get(0));
        assertEquals(MockStory.STORY3, emittedStories.get(1));
        assertEquals(MockStory.STORY4, emittedStories.get(2));
    }
}
