package com.setiawanpaiman.sunnyreader.di.component;

import com.setiawanpaiman.sunnyreader.di.module.MockAndroidTestApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MockAndroidTestApplicationModule.class)
public interface MockAndroidTestApplicationComponent extends ApplicationComponent {
}
