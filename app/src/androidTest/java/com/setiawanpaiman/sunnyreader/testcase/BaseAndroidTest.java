package com.setiawanpaiman.sunnyreader.testcase;

import android.support.test.InstrumentationRegistry;

import com.setiawanpaiman.sunnyreader.AndroidTestApplication;
import com.setiawanpaiman.sunnyreader.di.component.ApplicationComponent;

public class BaseAndroidTest {

    public ApplicationComponent getApplicationComponent() {
        return ((AndroidTestApplication) InstrumentationRegistry.getTargetContext().getApplicationContext())
                .getApplicationComponent();
    }
}
