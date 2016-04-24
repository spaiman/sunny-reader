package com.setiawanpaiman.sunnyreader;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.component.DaggerApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.module.ApplicationModule;

public class HackerNewsApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
