package com.setiawanpaiman.sunnyreader.ui.listener;

import com.setiawanpaiman.sunnyreader.data.model.Story;
import com.setiawanpaiman.sunnyreader.ui.adapter.StoryAdapter;

public interface OnStoryClickListener {

    void onOpenStoryDetail(Story story, StoryAdapter.ViewHolder vh);

    void onOpenStoryInBrowser(Story story);
}
