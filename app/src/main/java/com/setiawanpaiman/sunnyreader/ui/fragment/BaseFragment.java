package com.setiawanpaiman.sunnyreader.ui.fragment;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
