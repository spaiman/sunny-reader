package com.setiawanpaiman.sunnyreader.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;

import com.setiawanpaiman.sunnyreader.R;

import org.hamcrest.Matcher;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.setiawanpaiman.sunnyreader.util.MatcherUtils.withRecyclerView;

public class ViewActionUtils {

    public static void pressUpButton() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    }

    public static void clickComment(int position) {
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(position));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(position, R.id.txt_content))
                .perform(click());
    }

    public static void swipeRefresh() {
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(0));
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
    }

    public static void rotateScreen(Activity activity) {
        Context context = ApplicationProvider.getApplicationContext();
        int orientation = context.getResources().getConfiguration().orientation;

        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }
}
