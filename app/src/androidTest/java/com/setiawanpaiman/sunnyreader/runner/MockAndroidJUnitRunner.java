package com.setiawanpaiman.sunnyreader.runner;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.setiawanpaiman.sunnyreader.AndroidTestApplication;

public class MockAndroidJUnitRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, AndroidTestApplication.class.getName(), context);
    }
}
