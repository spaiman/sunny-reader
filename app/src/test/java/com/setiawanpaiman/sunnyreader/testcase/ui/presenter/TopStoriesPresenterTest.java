package com.setiawanpaiman.sunnyreader.testcase.ui.presenter;

import com.setiawanpaiman.sunnyreader.Constants;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;
import com.setiawanpaiman.sunnyreader.mockdata.MockStory;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;
import com.setiawanpaiman.sunnyreader.ui.presenter.EndlessListContract;
import com.setiawanpaiman.sunnyreader.ui.presenter.TopStoriesPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
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
    EndlessListContract.View<List<Story>> mView;

    TestScheduler mTestScheduler = new TestScheduler();

    TopStoriesPresenter mPresenter;

    List<Story> mMockStories = Arrays.asList(MockStory.STORY1, MockStory.STORY2, MockStory.STORY3);

    @Before
    public void setUp() throws Exception {
        mPresenter = new TopStoriesPresenter(mView, Schedulers.immediate(), mHackerNewsService);
    }

    @Test
    public void testRestoreInstanceState() throws Exception {
        mPresenter.onRestoreInstanceState(10);
        assertEquals(10, mPresenter.getCurrentPage());
    }

    @Test
    public void testLoadDataRefresh() throws Exception {
        arrangePreLoadData();
        mPresenter.loadData(true);
        assertPostSuccessLoadData(1, true);
    }

    @Test
    public void testLoadDataNotRefresh() throws Exception {
        arrangePreLoadData();
        mPresenter.loadData(false);
        assertPostSuccessLoadData(2, false);
    }

    @Test
    public void testLoadDataRefreshAfterOnRestoreInstanceState() throws Exception {
        arrangePreLoadData();
        mPresenter.onRestoreInstanceState(5);
        mPresenter.loadData(true);
        assertPostSuccessLoadData(1, true);
    }

    @Test
    public void testLoadDataNotRefreshAfterOnRestoreInstanceState() throws Exception {
        arrangePreLoadData();
        mPresenter.onRestoreInstanceState(5);
        mPresenter.loadData(false);
        assertPostSuccessLoadData(6, false);
    }

    @Test
    public void testErrorLoadDataRefresh() throws Exception {
        when(mHackerNewsService.getTopStories(anyInt(), anyInt()))
                .thenReturn(Observable.<List<Story>> error(new Exception()));
        mPresenter.loadData(true);
        assertPostErrorLoadData(1, 1, true);
    }

    @Test
    public void testErrorLoadDataNotRefresh() throws Exception {
        when(mHackerNewsService.getTopStories(anyInt(), anyInt()))
                .thenReturn(Observable.<List<Story>> error(new Exception()));
        mPresenter.loadData(false);
        assertPostErrorLoadData(1, 2, false);
    }

    @Test
    public void testLoadDataSuccessShouldTriggerLoadMore() throws Exception {
        arrangePreLoadData();
        mPresenter.loadData(true);

        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mPresenter.loadData(false);
        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);

        assertEquals(2, mPresenter.getCurrentPage());
        verify(mHackerNewsService, times(1)).getTopStories(1, Constants.PER_PAGE);
        verify(mHackerNewsService, times(1)).getTopStories(2, Constants.PER_PAGE);
        verify(mView, times(1)).setProgressVisibility(true, false);
        verify(mView, times(1)).setProgressVisibility(false, false);
        verify(mView, times(1)).setProgressVisibility(true, true);
        verify(mView, times(1)).setProgressVisibility(false, true);
        verify(mView, times(1)).showData(mMockStories, true);
        verify(mView, times(1)).showData(mMockStories, false);
    }

    @Test
    public void testOnDestroy() throws Exception {
        arrangePreLoadData();
        mPresenter.loadData(true);

        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mPresenter.onDestroy();
        inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);

        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        verify(mHackerNewsService, times(1)).getTopStories(1, Constants.PER_PAGE);
        verify(mView, times(1)).setProgressVisibility(true, true);
        verify(mView, times(0)).setProgressVisibility(false, true);
        verify(mView, times(0)).showData(anyListOf(Story.class), anyBoolean());
    }

    private void arrangePreLoadData() {
        when(mHackerNewsService.getTopStories(anyInt(), anyInt()))
                .thenReturn(Observable.just(mMockStories).delay(2, TimeUnit.SECONDS, mTestScheduler));

        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);
    }

    private void assertPostSuccessLoadData(int expectedPage, boolean refresh) {
        boolean inProgress = mPresenter.isLoadIsInProgress();
        assertTrue(inProgress);

        mTestScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        inProgress = mPresenter.isLoadIsInProgress();
        assertFalse(inProgress);

        assertEquals(expectedPage, mPresenter.getCurrentPage());
        verify(mHackerNewsService, times(1)).getTopStories(expectedPage, Constants.PER_PAGE);
        verify(mView, times(1)).setProgressVisibility(true, refresh);
        verify(mView, times(1)).setProgressVisibility(false, refresh);
        verify(mView, times(1)).showData(mMockStories, refresh);
    }

    private void assertPostErrorLoadData(int expectedCurrentPage, int pageLoaded, boolean refresh) {
        assertEquals(expectedCurrentPage, mPresenter.getCurrentPage());
        verify(mHackerNewsService, times(1)).getTopStories(pageLoaded, Constants.PER_PAGE);
        verify(mView, times(1)).setProgressVisibility(true, refresh);
        verify(mView, times(1)).setProgressVisibility(false, refresh);
    }
}
