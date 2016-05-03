package com.setiawanpaiman.sunnyreader.testcase;

import com.setiawanpaiman.sunnyreader.BuildConfig;
import com.setiawanpaiman.sunnyreader.TestApplication;
import com.setiawanpaiman.sunnyreader.di.component.MockTestApplicationComponent;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@Config(application = TestApplication.class,
        constants = BuildConfig.class,
        sdk = 21)
public class BaseTest {

    public MockTestApplicationComponent getApplicationComponent() {
        return ((TestApplication) RuntimeEnvironment.application).getApplicationComponent();
    }
}
