package com.setiawanpaiman.sunnyreader;

import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.component.DaggerMockAndroidTestApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.component.MockAndroidTestApplicationComponent;
import com.setiawanpaiman.sunnyreader.di.module.MockAndroidTestApplicationModule;

public class AndroidTestApplication extends HackerNewsApplication {

    private MockAndroidTestApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerMockAndroidTestApplicationComponent.builder()
                .mockAndroidTestApplicationModule(new MockAndroidTestApplicationModule())
                .build();
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
