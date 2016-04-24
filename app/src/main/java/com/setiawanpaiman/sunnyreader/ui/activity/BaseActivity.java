package com.setiawanpaiman.sunnyreader.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.setiawanpaiman.sunnyreader.HackerNewsApplication;
import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;

public class BaseActivity extends AppCompatActivity {

    public final ApplicationComponent getApplicationComponent() {
        return ((HackerNewsApplication) getApplication()).getApplicationComponent();
    }
}
