package com.setiawanpaiman.sunnyreader.testcase.domain.persistent;

import android.content.SharedPreferences;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.setiawanpaiman.sunnyreader.data.db.AppDatabase;
import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsDiskStore;
import com.setiawanpaiman.sunnyreader.mockdata.MockComment;
import com.setiawanpaiman.sunnyreader.mockdata.MockStory;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;
import com.setiawanpaiman.sunnyreader.util.JsonParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import rx.observers.TestSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class HackerNewsDiskStoreTest extends BaseTest {

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    SharedPreferences mSharedPreferences;

    @Mock
    JsonParser mJsonParser;

    @InjectMocks
    HackerNewsDiskStore mHackerNewsDiskStore;

    @Before
    public void setUp() throws Exception {
        FlowManager.init(RuntimeEnvironment.application);
    }

    @After
    public void tearDown() throws Exception {
        FlowManager.getDatabase(AppDatabase.NAME).reset(RuntimeEnvironment.application);
        FlowManager.destroy();
    }

    // BEGIN: GET TOP STORIES
    @Test
    public void testGetTopStoriesEmpty() throws Exception {
        when(mSharedPreferences.getString(anyString(), anyString())).thenReturn("");

        TestSubscriber<List<Long>> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getTopStories(0, 10).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(new ArrayList<Long>());
    }

    @Test
    public void testGetTopStoriesPartial() throws Exception {
        when(mSharedPreferences.getString(anyString(), anyString())).thenReturn("10,90,20,30,50");

        TestSubscriber<List<Long>> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getTopStories(1, 3).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(Arrays.asList(90L, 20L, 30L));
    }

    @Test
    public void testGetTopStoriesInvalidParseLong() throws Exception {
        when(mSharedPreferences.getString(anyString(), anyString())).thenReturn("10,90,20a,30,50");

        TestSubscriber<List<Long>> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getTopStories(1, 3).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(Arrays.asList(90L, 30L));
    }

    @Test
    public void testGetTopStoriesStartPositionExceeded() throws Exception {
        when(mSharedPreferences.getString(anyString(), anyString())).thenReturn("10,90,20,30,50");

        TestSubscriber<List<Long>> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getTopStories(10, 2).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(new ArrayList<Long>());
    }

    @Test
    public void testGetTopStoriesCountExceeded() throws Exception {
        when(mSharedPreferences.getString(anyString(), anyString())).thenReturn("10,90,20,30,50");

        TestSubscriber<List<Long>> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getTopStories(3, 100).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(Arrays.asList(30L, 50L));
    }
    // END: GET TOP STORIES

    // BEGIN: GET STORY
    @Test
    public void testGetStoryEmpty() throws Exception {
        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getStory(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
    }

    @Test
    public void testGetStoryParseSuccess() throws Exception {
        when(mJsonParser.toJson(anyObject(), eq(Story.class))).thenReturn(MockStory.STORY1_JSON);
        when(mJsonParser.fromJson(anyString(), eq(Story.class))).thenReturn(MockStory.STORY1);
        // trigger JsonParser#toJson
        mHackerNewsDiskStore.saveStory(MockStory.STORY1);

        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getStory(MockStory.STORY1.getId()).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(MockStory.STORY1);
    }

    @Test
    public void testGetStoryParseError() throws Exception {
        when(mJsonParser.toJson(any(), eq(Story.class))).thenReturn(MockStory.STORY_UNKNOWN_JSON);
        when(mJsonParser.fromJson(anyString(), eq(Story.class))).thenReturn(null);
        // trigger JsonParser#toJson
        mHackerNewsDiskStore.saveStory(MockStory.STORY1);

        TestSubscriber<Story> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getStory(MockStory.STORY1.getId()).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(null);
    }
    // END: GET STORY

    // BEGIN: GET COMMENT
    @Test
    public void testGetCommentEmpty() throws Exception {
        TestSubscriber<Comment> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getComment(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
    }

    @Test
    public void testGetCommentParseSuccess() throws Exception {
        when(mJsonParser.toJson(anyObject(), eq(Comment.class))).thenReturn(MockComment.COMMENT1_JSON);
        when(mJsonParser.fromJson(anyString(), eq(Comment.class))).thenReturn(MockComment.COMMENT1);
        // trigger JsonParser#toJson
        mHackerNewsDiskStore.saveComment(MockComment.COMMENT1);

        TestSubscriber<Comment> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getComment(MockComment.COMMENT1.getId()).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(MockComment.COMMENT1);
    }

    @Test
    public void testGetCommentParseError() throws Exception {
        when(mJsonParser.toJson(any(), eq(Comment.class))).thenReturn(MockComment.COMMENT_INVALID_JSON);
        when(mJsonParser.fromJson(anyString(), eq(Comment.class))).thenReturn(null);
        // trigger JsonParser#toJson
        mHackerNewsDiskStore.saveComment(MockComment.COMMENT1);

        TestSubscriber<Comment> testSubscriber = new TestSubscriber<>();
        mHackerNewsDiskStore.getComment(MockComment.COMMENT1.getId()).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(null);
    }
    // END: GET COMMENT

    // BEGIN: SAVE TOP STORIES
    @Test
    public void testSaveTopStories() throws Exception {
        List<Long> topStories = Arrays.asList(10L, 20L, 30L, 40L, 50L, 60L);
        HackerNewsDiskStore hackerNewsDiskStore = new HackerNewsDiskStore(mJsonParser,
                getApplicationComponent().providesSharedPreferences());
        hackerNewsDiskStore.saveTopStories(topStories);

        TestSubscriber<List<Long>> testSubscriber1 = new TestSubscriber<>();
        hackerNewsDiskStore.getTopStories(1, 3).subscribe(testSubscriber1);
        testSubscriber1.assertNoErrors();
        testSubscriber1.assertValue(topStories.subList(1, 4));

        TestSubscriber<List<Long>> testSubscriber2 = new TestSubscriber<>();
        hackerNewsDiskStore.getTopStories(3, 3).subscribe(testSubscriber2);
        testSubscriber2.assertNoErrors();
        testSubscriber2.assertValue(topStories.subList(3, 6));
    }

    @Test
    public void testSaveTopStoriesNull() throws Exception {
        HackerNewsDiskStore hackerNewsDiskStore = new HackerNewsDiskStore(mJsonParser,
                getApplicationComponent().providesSharedPreferences());
        hackerNewsDiskStore.saveTopStories(null);

        TestSubscriber<List<Long>> testSubscriber1 = new TestSubscriber<>();
        hackerNewsDiskStore.getTopStories(1, 3).subscribe(testSubscriber1);
        testSubscriber1.assertNoErrors();
        testSubscriber1.assertValue(new ArrayList<Long>());
    }
    // END: SAVE TOP STORIES
}
