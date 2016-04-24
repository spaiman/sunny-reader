package com.setiawanpaiman.sunnyreader.di.component;

import com.google.gson.Gson;

import com.setiawanpaiman.sunnyreader.di.module.MockApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MockApplicationModule.class)
public interface MockApplicationComponent extends ApplicationComponent {

    Gson providesGson();
}
