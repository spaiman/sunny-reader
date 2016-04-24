package com.setiawanpaiman.sunnyreader;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.setiawanpaiman.sunnyreader.di.component.DaggerMockApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.component.MockApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.module.MockApplicationModule;

public class TestApplication extends Application {

    private MockApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        mApplicationComponent = DaggerMockApplicationComponent.builder()
                .mockApplicationModule(new MockApplicationModule())
                .build();
    }

    public MockApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
