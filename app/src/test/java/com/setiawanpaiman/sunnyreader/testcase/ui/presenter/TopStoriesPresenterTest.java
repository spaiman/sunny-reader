package com.setiawanpaiman.sunnyreader.testcase.ui.presenter;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;
import com.setiawanpaiman.sunnyreader.mockdata.MockStory;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;
import com.setiawanpaiman.sunnyreader.ui.presenter.TopStoriesContract;
import com.setiawanpaiman.sunnyreader.ui.presenter.TopStoriesPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class TopStoriesPresenterTest extends BaseTest {

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    IHackerNewsService mHackerNewsService;

    @Mock
    TopStoriesContract.View mView;

    TestScheduler mTestScheduler = new TestScheduler();

    TopStoriesPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        mPresenter = new TopStoriesPresenter(mHackerNewsService, mView, Schedulers.immediate());

    }

    @Test
    public void testRestoreInstanceState() throws Exception {
        mPresenter.onRestoreInstanceState(10);
        assertEquals(10, mPresenter.getCurrentPage());
    }

    @Test
    public void testLoadTopStoriesRefresh() throws Exception {
        arrangePreLoadTopStories();
        mPresenter.loadTopStories(true);
        assertPostSuccessLoadTopStories(1, true);
    }

    @Test
    public void testLoadTopStoriesNotRefresh() throws Exception {
        arrangePreLoadTopStories();
        mPresenter.loadTopStories(false);
        assertPostSuccessLoadTopStories(2, false);
    }

    @Test
    public void testLoadTopStoriesRefreshAfterOnRestoreInstanceState() throws Exception {
        arrangePreLoadTopStories();
        mPresenter.onRestoreInstanceState(5);
        mPresenter.loadTopStories(true);
        assertPostSuccessLoadTopStories(1, true);
    }

    @Test
    public void testLoadTopStoriesNotRefreshAfterOnRestoreInstanceState() throws Exception {
        arrangePreLoadTopStories();
        mPresenter.onRestoreInstanceState(5);
        mPresenter.loadTopStories(false);
        assertPostSuccessLoadTopStories(6, false);
    }

    @Test
    public void testErrorLoadTopStoriesRefresh() throws Exception {
        when(mHackerNewsService.getTopStories(anyInt()))
                .thenReturn(Observable.<Story> error(new Exception()));
        mPresenter.loadTopStories(true);
        assertPostErrorLoadTopStories(1, 1, true);
    }

    @Test
    public void testErrorLoadTopStoriesNotRefresh() throws Exception {
        when(mHackerNewsService.getTopStories(anyInt()))
                .thenReturn(Observable.<Story> error(new Exception()));
        mPresenter.loadTopStories(false);
        assertPostErrorLoadTopStories(1, 2, false);
    }

    @Test
    public void testLoadTopStoriesSuccessShouldTriggerLoadMore() throws Exception {
        arrangePreLoadTopStories();
        mPresenter.loadTopStories(true);

        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mPresenter.loadTopStories(false);
        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);

        assertEquals(2, mPresenter.getCurrentPage());
        verify(mHackerNewsService, times(1)).getTopStories(1);
        verify(mHackerNewsService, times(1)).getTopStories(2);
        verify(mView, times(1)).setProgressVisibility(true, false);
        verify(mView, times(1)).setProgressVisibility(false, false);
        verify(mView, times(1)).setProgressVisibility(true, true);
        verify(mView, times(1)).setProgressVisibility(false, true);
        verify(mView, times(1)).showTopStory(MockStory.STORY1, true);
        verify(mView, times(1)).showTopStory(MockStory.STORY1, false);
        verify(mView, times(2)).showTopStory(MockStory.STORY2, false);
        verify(mView, times(2)).showTopStory(MockStory.STORY3, false);
    }

    @Test
    public void testOnDestroy() throws Exception {
        arrangePreLoadTopStories();
        mPresenter.loadTopStories(true);

        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mPresenter.onDestroy();
        inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);

        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        verify(mHackerNewsService, times(1)).getTopStories(1);
        verify(mView, times(1)).setProgressVisibility(true, true);
        verify(mView, times(0)).setProgressVisibility(false, true);
        verify(mView, times(0)).showTopStory(any(Story.class), anyBoolean());
    }

    private void arrangePreLoadTopStories() {
        Story[] stories = new Story[] { MockStory.STORY1, MockStory.STORY2, MockStory.STORY3 };
        when(mHackerNewsService.getTopStories(anyInt()))
                .thenReturn(Observable.from(stories).delay(2, TimeUnit.SECONDS, mTestScheduler));

        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);
    }

    private void assertPostSuccessLoadTopStories(int expectedPage, boolean refresh) {
        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);

        assertEquals(expectedPage, mPresenter.getCurrentPage());
        verify(mHackerNewsService, times(1)).getTopStories(expectedPage);
        verify(mView, times(1)).setProgressVisibility(true, refresh);
        verify(mView, times(1)).setProgressVisibility(false, refresh);
        verify(mView, times(1)).showTopStory(MockStory.STORY1, refresh);
        verify(mView, times(1)).showTopStory(MockStory.STORY2, false);
        verify(mView, times(1)).showTopStory(MockStory.STORY3, false);
    }

    private void assertPostErrorLoadTopStories(int expectedCurrentPage, int pageLoaded, boolean refresh) {
        assertEquals(expectedCurrentPage, mPresenter.getCurrentPage());
        verify(mHackerNewsService, times(1)).getTopStories(pageLoaded);
        verify(mView, times(1)).setProgressVisibility(true, refresh);
        verify(mView, times(1)).setProgressVisibility(false, refresh);
    }
}
