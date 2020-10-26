package com.setiawanpaiman.sunnyreader.testcase;

import com.setiawanpaiman.sunnyreader.TestApplication;
import com.setiawanpaiman.sunnyreader.di.component.MockTestApplicationComponent;

import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;

@Config(application = TestApplication.class,
        sdk = 21)
public class BaseTest {

    public MockTestApplicationComponent getApplicationComponent() {
        return ((TestApplication) ApplicationProvider.getApplicationContext())
                .getApplicationComponent();
    }
}
