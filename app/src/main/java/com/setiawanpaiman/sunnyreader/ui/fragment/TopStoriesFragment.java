package com.setiawanpaiman.sunnyreader.ui.fragment;

import android.content.Intent;
import android.net.Uri;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.ui.adapter.EndlessListAdapter;
import com.setiawanpaiman.sunnyreader.ui.adapter.StoryAdapter;
import com.setiawanpaiman.sunnyreader.ui.presenter.EndlessListContract;
import com.setiawanpaiman.sunnyreader.ui.presenter.TopStoriesPresenter;

import rx.schedulers.Schedulers;

public class TopStoriesFragment extends EndlessListFragment<Story>
        implements StoryAdapter.OnClickListener {

    public static TopStoriesFragment newInstance() {
        return new TopStoriesFragment();
    }

    public TopStoriesFragment() {
    }

    @Override
    public EndlessListContract.Presenter<Story> createPresenter() {
        return new TopStoriesPresenter(this, Schedulers.io(),
                getApplicationComponent().provideHackerNewsService());
    }

    @Override
    public EndlessListAdapter<Story, ?> createAdapter() {
        StoryAdapter adapter = new StoryAdapter(getContext());
        adapter.setOnClickListener(this);
        return adapter;
    }

    @Override
    public void onStoryClicked(Story story) {
        // TODO: Open detail activity after it has been implemented
    }

    @Override
    public void onOpenInBrowserClicked(Story story) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(story.getUrl()));
        startActivity(intent);
    }
}
