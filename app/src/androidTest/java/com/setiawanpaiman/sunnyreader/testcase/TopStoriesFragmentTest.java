package com.setiawanpaiman.sunnyreader.testcase;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.SmallTest;

import com.setiawanpaiman.sunnyreader.Constants;
import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;
import com.setiawanpaiman.sunnyreader.mockdata.MockAndroidStory;
import com.setiawanpaiman.sunnyreader.ui.activity.MainActivity;
import com.setiawanpaiman.sunnyreader.utils.ViewAssertionUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.setiawanpaiman.sunnyreader.utils.MatcherUtils.withRecyclerView;
import static com.setiawanpaiman.sunnyreader.utils.MatcherUtils.withToolbarTitle;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class TopStoriesFragmentTest extends BaseAndroidTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class, false, false);

    private Context mApplicationContext;
    private IHackerNewsService mHackerNewsService;

    @Before
    public void setUp() throws Exception {
        mApplicationContext = InstrumentationRegistry.getTargetContext();
        mHackerNewsService = getApplicationComponent().provideHackerNewsService();
    }

    @Test
    public void loadDataAndLoadMore_showTopStories() throws Exception {
        when(mHackerNewsService.getTopStories(anyInt(), anyInt()))
                .thenAnswer(new Answer<Observable<List<Story>>>() {
                    @Override
                    public Observable<List<Story>> answer(InvocationOnMock invocation) throws Throwable {
                        int page = (int) invocation.getArguments()[0];
                        int count = (int) invocation.getArguments()[1];
                        int start = (page - 1) * Constants.PER_PAGE + 1;
                        if (page <= 2) {
                            return Observable.just(
                                    MockAndroidStory.generateMockStories(start, count))
                                    .delay(2, TimeUnit.SECONDS);
                        } else {
                            List<Story> emptyStories = new ArrayList<>();
                            return Observable.just(emptyStories);
                        }
                    }
                });
        launchActivity();

        CharSequence expectedTitle = mApplicationContext.getString(R.string.app_name);
        onView(isAssignableFrom(Toolbar.class)).check(matches(withToolbarTitle(is(expectedTitle))));
        for (int i = 1; i <= 20; i++) {
            onView(withId(R.id.recycler_view)).perform(scrollToPosition(i - 1));
            ViewAssertionUtils.assertStoryViewHolder(mApplicationContext, i - 1, i, true);
        }
        // scroll to item 21, the item should not be found
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(20));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(20, R.id.txt_title))
                .check(doesNotExist());
    }

    @Test
    public void clickOpenStoryInBrowser_startIntent() throws Exception {
        when(mHackerNewsService.getTopStories(anyInt(), anyInt()))
                .thenReturn(Observable.just(MockAndroidStory.generateMockStories(1, 10)));
        launchActivity();

        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(0, R.id.btn_open))
                .perform(click());

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData("http://www.domain1.com/url")));
    }

    @Test
    public void loadDataNoUrl_showTopStoriesWithoutOpenButton() throws Exception {
        when(mHackerNewsService.getTopStories(anyInt(), anyInt()))
                .thenReturn(Observable.just(MockAndroidStory.generateMockStoriesNoUrl(1, 10))
                        .delay(2, TimeUnit.SECONDS));
        launchActivity();

        for (int i = 1; i <= 10; i++) {
            onView(withId(R.id.recycler_view)).perform(scrollToPosition(i - 1));
            ViewAssertionUtils.assertStoryViewHolder(mApplicationContext, i - 1, i, false);
        }
    }

    private void launchActivity() {
        mActivityRule.launchActivity(
                new Intent(mApplicationContext, MainActivity.class));
    }
}
