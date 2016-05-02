package com.setiawanpaiman.sunnyreader.testcase;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.SmallTest;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;
import com.setiawanpaiman.sunnyreader.mockdata.MockAndroidComment;
import com.setiawanpaiman.sunnyreader.mockdata.MockAndroidStory;
import com.setiawanpaiman.sunnyreader.ui.activity.MainActivity;
import com.setiawanpaiman.sunnyreader.utils.ViewActionUtils;
import com.setiawanpaiman.sunnyreader.utils.ViewAssertionUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.setiawanpaiman.sunnyreader.utils.MatcherUtils.withRecyclerView;
import static com.setiawanpaiman.sunnyreader.utils.MatcherUtils.withToolbarTitle;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class StoryDetailFragmentTest extends BaseAndroidTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class, false, false);

    private Context mApplicationContext;
    private IHackerNewsService mHackerNewsService;

    @Before
    public void setUp() throws Exception {
        mApplicationContext = InstrumentationRegistry.getTargetContext();
        mHackerNewsService = getApplicationComponent().provideHackerNewsService();
        when(mHackerNewsService.getTopStories(anyInt(), anyInt()))
                .thenReturn(Observable.just(MockAndroidStory.generateMockStories(1, 10)));
        when(mHackerNewsService.getComments(Matchers.any(Story.class), anyInt(), anyInt()))
                .thenAnswer(new Answer<Observable<List<Comment>>>() {
                    @Override
                    public Observable<List<Comment>> answer(InvocationOnMock invocation) throws Throwable {
                        int page = (int) invocation.getArguments()[1];
                        if (page == 1) return Observable.just(MockAndroidComment.generateMockComments(1, 3, 2));
                        else {
                            List<Comment> emptyComments = new ArrayList<>();
                            return Observable.just(emptyComments);
                        }
                    }
                });
    }

    @Test
    public void clickStoryShouldShowStoryDetailCorrectly() throws Exception {
        launchActivityAndMoveToStoryDetail();

        CharSequence expectedTitle = mApplicationContext.getString(R.string.title_story_detail);
        onView(isAssignableFrom(Toolbar.class)).check(matches(withToolbarTitle(is(expectedTitle))));

        ViewAssertionUtils.assertStoryViewHolder(mApplicationContext, 0, 2, true);
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(0, R.id.txt_content))
                .check(matches(withText("Text 2")));
    }

    @Test
    public void clickButtonOpenInStoryDetailShouldOpenUrlInBrowser() throws Exception {
        launchActivityAndMoveToStoryDetail();

        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(0, R.id.btn_open))
                .perform(click());
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData("http://www.domain2.com/url")));
    }

    @Test
    public void collapseExpandCommentsShouldWorkCorrectly() throws Exception {
        launchActivityAndMoveToStoryDetail();

        // initial comments shown
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 1, 1, 2, 0);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 2, 2, 3, 0);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 3, 3, 4, 0);

        // expand first comment (id = 1) should open 2 replies at position 2 and 3
        ViewActionUtils.clickComment(1);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 2, 11, 3, 1);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 3, 12, 3, 1);

        // expand comment (id = 12) should open 3 replies at position 4, 5 and 6
        ViewActionUtils.clickComment(3);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 4, 121, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 5, 122, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 6, 123, 0, 2);

        // expand and collapse comment (id = 123) should not do anything
        ViewActionUtils.clickComment(6);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 7, 2, 3, 0);
        ViewActionUtils.clickComment(6);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 7, 2, 3, 0);

        // expand comment (id = 2) should open 3 replies at position 8, 9 and 10
        ViewActionUtils.clickComment(7);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 8, 21, 4, 1);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 9, 22, 4, 1);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 10, 23, 4, 1);

        // expand comment (id = 21) should open 4 replies at position 9, 10, 11 and 12
        ViewActionUtils.clickComment(8);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 9, 211, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 10, 212, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 11, 213, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 12, 214, 0, 2);

        // expand comment (id = 23) should open 4 replies at position 15, 16, 17 and 18
        ViewActionUtils.clickComment(14);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 15, 231, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 16, 232, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 17, 233, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 18, 234, 0, 2);

        // collapse comment (id = 2) should close all replies
        ViewActionUtils.clickComment(7);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 8, 3, 4, 0);

        // expand comment (id = 2) again should restore all opened replies
        ViewActionUtils.clickComment(7);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 8, 21, 4, 1, true);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 9, 211, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 10, 212, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 11, 213, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 12, 214, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 13, 22, 4, 1);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 14, 23, 4, 1, true);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 15, 231, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 16, 232, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 17, 233, 0, 2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 18, 234, 0, 2);

        // swipe refresh comments should reset comments expanded status
        ViewActionUtils.swipeRefresh();
        // initial comments shown
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 1, 1, 2, 0);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 2, 2, 3, 0);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 3, 3, 4, 0);
        // check expanded status for comment (id = 2), should only open 3 replies
        ViewActionUtils.clickComment(2);
        ViewAssertionUtils.assertCommentViewHolder(mApplicationContext, 6, 3, 4, 0);
    }

    private void launchActivityAndMoveToStoryDetail() {
        mActivityRule.launchActivity(
                new Intent(mApplicationContext, MainActivity.class));

        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(1, R.id.txt_title))
                .perform(click());
    }
}
