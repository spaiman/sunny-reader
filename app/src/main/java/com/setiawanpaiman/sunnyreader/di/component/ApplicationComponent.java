package com.setiawanpaiman.sunnyreader.di.component;

import com.setiawanpaiman.sunnyreader.di.module.ApplicationModule;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    IHackerNewsService provideHackerNewsService();
}