package com.setiawanpaiman.sunnyreader.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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

    private Application mApplication;

    public MockApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences() {
        return mApplication.getSharedPreferences("test_preferences", Context.MODE_PRIVATE);
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