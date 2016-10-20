package com.setiawanpaiman.sunnyreader.di.component;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.setiawanpaiman.sunnyreader.di.module.MockTestApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MockTestApplicationModule.class)
public interface MockTestApplicationComponent extends ApplicationComponent {

    Gson providesGson();
    SharedPreferences providesSharedPreferences();
}
