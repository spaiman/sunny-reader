package com.setiawanpaiman.sunnyreader.utils;

import android.content.Context;
import android.net.Uri;
import android.support.test.espresso.matcher.ViewMatchers;
import android.text.Html;
import android.text.format.DateUtils;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Comment;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.mockdata.MockAndroidComment;
import com.setiawanpaiman.sunnyreader.mockdata.MockAndroidStory;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.setiawanpaiman.sunnyreader.utils.MatcherUtils.withRecyclerView;

public class ViewAssertionUtils {

    public static void assertStoryViewHolder(Context applicationContext, int posInRecyclerView,
                                             int id, boolean hasUrl) {
        Story story = MockAndroidStory.generateMockStory(id);
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_title))
                .check(matches(withText(story.getTitle())));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_total_points))
                .check(matches(withText(applicationContext
                        .getString(R.string.points_format, story.getScore()))));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_author))
                .check(matches(withText(story.getAuthor())));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_time))
                .check(matches(withText(DateUtils.getRelativeTimeSpanString(
                        TimeUnit.SECONDS.toMillis(story.getTimestamp()),
                        System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE).toString()
                )));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_total_comments))
                .check(matches(withText(applicationContext
                        .getResources().getQuantityString(R.plurals.total_comments,
                                story.getTotalComments(), story.getTotalComments()))));
        if (hasUrl) {
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_host_url))
                    .check(matches(withText(Uri.parse(story.getUrl()).getHost())));
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.btn_open))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        } else {
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_host_url))
                    .check(matches(withText("")));
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.btn_open))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
    }

    public static void assertStoryDetailViewHolder(Context applicationContext, int posInRecyclerView,
                                                   int id, boolean hasUrl, boolean hasText) {
        Story story = MockAndroidStory.generateMockStory(id);
        assertStoryViewHolder(applicationContext, posInRecyclerView, id, hasUrl);
        if (hasText) {
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_content))
                    .check(matches(withText(Html.fromHtml(story.getText()).toString())));
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_content))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        } else {
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_content))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
    }

    public static void assertCommentViewHolder(Context applicationContext, int posInRecyclerView,
                                               int id, int totalReplies, int depth) {
        assertCommentViewHolder(applicationContext, posInRecyclerView, id, totalReplies, depth, false);
    }

    public static void assertCommentViewHolder(Context applicationContext, int posInRecyclerView,
                                               int id, int totalReplies, int depth, boolean expanded) {
        Comment comment = MockAndroidComment.generateMockComment(id, totalReplies, depth);
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(posInRecyclerView));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_author))
                .check(matches(withText(comment.getAuthor())));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_time))
                .check(matches(withText(DateUtils.getRelativeTimeSpanString(
                        TimeUnit.SECONDS.toMillis(comment.getTimestamp()), System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE).toString())));
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_content))
                .check(matches(withText(Html.fromHtml(comment.getText()).toString())));
        if (comment.getTotalReplies() > 0 && !expanded) {
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_total_replies))
                    .check(matches(withText(applicationContext.getResources().getQuantityString(
                            R.plurals.total_replies, comment.getTotalReplies(),
                            comment.getTotalReplies()))));
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_total_replies))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        } else {
            onView(withRecyclerView(R.id.recycler_view).atPositionOnView(posInRecyclerView, R.id.txt_total_replies))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
    }
}
