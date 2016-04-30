package com.setiawanpaiman.sunnyreader.ui.fragment;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.setiawanpaiman.sunnyreader.HackerNewsApplication;
import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;

public class BaseFragment extends Fragment {

    public void setTitle(@StringRes int resId) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resId);
        }
    }

    public final ApplicationComponent getApplicationComponent() {
        return ((HackerNewsApplication) getActivity().getApplication()).getApplicationComponent();
    }
}
