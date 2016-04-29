package com.setiawanpaiman.sunnyreader.ui.activity;

import android.os.Bundle;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.ui.fragment.TopStoriesFragment;

public class TopStoriesActivity extends BaseActivity {

    private static final String TAG_TOP_STORIES = "top_stories_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stories);
        if (savedInstanceState == null) {
            openTopStories();
        } else {

        }
    }

    private void openTopStories() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, TopStoriesFragment.newInstance(), TAG_TOP_STORIES)
                .commit();
    }
}
