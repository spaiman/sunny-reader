package com.setiawanpaiman.sunnyreader.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.ui.fragment.StoryDetailFragment;
import com.setiawanpaiman.sunnyreader.ui.fragment.TopStoriesFragment;

public class TopStoriesActivity extends BaseActivity
        implements TopStoriesFragment.OnInteractionListener,
                   FragmentManager.OnBackStackChangedListener {

    private static final String TAG_TOP_STORIES = "top_stories_fragment";
    private static final String TAG_STORY_DETAIL = "story_detail_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stories);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            openTopStories();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportActionBar() != null) {
            boolean canBack = getSupportFragmentManager().getBackStackEntryCount() > 1;
            getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
        }
    }

    @Override
    public void onStoryClicked(Story story) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, StoryDetailFragment.newInstance(story), TAG_STORY_DETAIL)
                .addToBackStack(null)
                .commit();
    }

    private void openTopStories() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, TopStoriesFragment.newInstance(), TAG_TOP_STORIES)
                .addToBackStack(null)
                .commit();
    }

    private void goBack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            fragmentManager.popBackStackImmediate();
        }
    }
}
