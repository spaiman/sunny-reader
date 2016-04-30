package com.setiawanpaiman.sunnyreader.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.setiawanpaiman.sunnyreader.R;
import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.ui.adapter.EndlessListAdapter;
import com.setiawanpaiman.sunnyreader.ui.adapter.StoryAdapter;
import com.setiawanpaiman.sunnyreader.ui.presenter.EndlessListContract;
import com.setiawanpaiman.sunnyreader.ui.presenter.TopStoriesPresenter;
import com.setiawanpaiman.sunnyreader.ui.widget.DividerItemDecoration;

import rx.schedulers.Schedulers;

import java.util.List;

public class TopStoriesFragment extends EndlessListFragment<Story>
        implements StoryAdapter.OnClickListener {

    private OnInteractionListener mOnInteractionListener;

    public static TopStoriesFragment newInstance() {
        return new TopStoriesFragment();
    }

    public TopStoriesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInteractionListener) {
            mOnInteractionListener = (OnInteractionListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(R.string.app_name);
    }

    @Override
    public EndlessListContract.Presenter<List<Story>> createPresenter() {
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
        if (mOnInteractionListener != null) {
            mOnInteractionListener.onStoryClicked(story);
        }
    }

    @Override
    public void onOpenInBrowserClicked(Story story) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(story.getUrl()));
        startActivity(intent);
    }

    public interface OnInteractionListener {

        void onStoryClicked(Story story);
    }
}
