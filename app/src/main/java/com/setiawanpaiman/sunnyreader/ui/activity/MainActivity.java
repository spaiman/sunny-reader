package com.setiawanpaiman.sunnyreader.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;

import butterknife.ButterKnife;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.ui.adapter.StoryAdapter;
import com.setiawanpaiman.sunnyreader.ui.fragment.StoryDetailFragment;
import com.setiawanpaiman.sunnyreader.ui.fragment.TopStoriesFragment;
import com.setiawanpaiman.sunnyreader.ui.listener.OnStoryClickListener;

public class MainActivity extends BaseActivity
        implements OnStoryClickListener,
                   FragmentManager.OnBackStackChangedListener {

    private static final String TAG_TOP_STORIES = "top_stories_fragment";
    private static final String TAG_STORY_DETAIL = "story_detail_fragment";

    private Fragment mTopStoriesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        mTopStoriesFragment = getSupportFragmentManager().findFragmentByTag(TAG_TOP_STORIES);
        if (savedInstanceState == null) {
            openTopStories();
        } else {
            onBackStackChanged();
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
    public void onOpenStoryInBrowser(Story story) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(story.getUrl()));
        startActivity(intent);
    }

    @Override
    public void onOpenStoryDetail(Story story, StoryAdapter.ViewHolder vh) {
        Fragment storyDetailFragment = StoryDetailFragment.newInstance(story);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(this)
                    .inflateTransition(R.transition.transition_story);
            mTopStoriesFragment.setExitTransition(new Fade());
            storyDetailFragment.setEnterTransition(new Fade());
            storyDetailFragment.setSharedElementEnterTransition(changeTransform);
            storyDetailFragment.setSharedElementReturnTransition(changeTransform);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, storyDetailFragment, TAG_STORY_DETAIL)
                .addToBackStack(null)
                .addSharedElement(vh.itemView, getString(R.string.story_transition_name, story.getId()))
                .commit();
    }

    private void openTopStories() {
        mTopStoriesFragment = TopStoriesFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mTopStoriesFragment, TAG_TOP_STORIES)
                .addToBackStack(null)
                .commit();
    }

    private void goBack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            fragmentManager.popBackStack();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
