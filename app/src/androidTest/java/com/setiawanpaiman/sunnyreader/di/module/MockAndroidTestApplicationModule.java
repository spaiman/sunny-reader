package com.setiawanpaiman.sunnyreader.di.module;

import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockAndroidTestApplicationModule {

    @Provides
    @Singleton
    IHackerNewsService providesHackerNewsService() {
        return Mockito.mock(IHackerNewsService.class);
    }
}
