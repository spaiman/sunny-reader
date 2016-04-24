package com.setiawanpaiman.sunnyreader.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.setiawanpaiman.sunnyreader.domain.api.HackerNewsApi;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsDiskStore;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsPersistent;
import com.setiawanpaiman.sunnyreader.domain.service.HackerNewsService;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockApplicationModule {

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    HackerNewsApi providesHackerNewsApi() {
        return Mockito.mock(HackerNewsApi.class);
    }

    @Provides
    @Singleton
    HackerNewsPersistent providesHackerNewsPersistent() {
        return Mockito.mock(HackerNewsDiskStore.class);
    }

    @Provides
    @Singleton
    IHackerNewsService providesHackerNewsService(HackerNewsApi hackerNewsApi,
                                                 HackerNewsPersistent hackerNewsPersistent) {
        return new HackerNewsService(hackerNewsApi, hackerNewsPersistent);
    }
}