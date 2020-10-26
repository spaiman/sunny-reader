package com.setiawanpaiman.sunnyreader.testcase;

import com.setiawanpaiman.sunnyreader.AndroidTestApplication;
import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;

import androidx.test.core.app.ApplicationProvider;

public class BaseAndroidTest {

    public ApplicationComponent getApplicationComponent() {
        return ((AndroidTestApplication) ApplicationProvider.getApplicationContext())
                .getApplicationComponent();
    }
}
