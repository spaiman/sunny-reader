package com.setiawanpaiman.sunnyreader;

import android.app.Application;

import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.component.DaggerApplicationComponent;

public class HackerNewsApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
