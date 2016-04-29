package com.setiawanpaiman.sunnyreader.ui.fragment;

import android.support.v4.app.Fragment;

import com.setiawanpaiman.sunnyreader.HackerNewsApplication;
import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;

public class BaseFragment extends Fragment {

    public final ApplicationComponent getApplicationComponent() {
        return ((HackerNewsApplication) getActivity().getApplication()).getApplicationComponent();
    }
}
