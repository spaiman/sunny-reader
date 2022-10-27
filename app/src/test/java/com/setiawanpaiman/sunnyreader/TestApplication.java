package com.setiawanpaiman.sunnyreader;

import android.app.Application;

import com.setiawanpaiman.sunnyreader.di.component.DaggerMockTestApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.component.MockTestApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.module.MockTestApplicationModule;

public class TestApplication extends Application {

    private MockTestApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerMockTestApplicationComponent.builder()
                .mockTestApplicationModule(new MockTestApplicationModule(this))
                .build();
    }

    public MockTestApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
