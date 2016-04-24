package com.setiawanpaiman.sunnyreader.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.setiawanpaiman.sunnyreader.BuildConfig;
import com.setiawanpaiman.sunnyreader.domain.api.ApiFactory;
import com.setiawanpaiman.sunnyreader.domain.api.HackerNewsApi;
import com.setiawanpaiman.sunnyreader.domain.service.HackerNewsService;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    public ApplicationModule() {
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    ApiFactory providesApiFactory(Gson gson) {
        return new ApiFactory(BuildConfig.BASE_URL_API, gson);
    }

    @Provides
    @Singleton
    HackerNewsApi providesHackerNewsApi(ApiFactory apiFactory) {
        return apiFactory.create(HackerNewsApi.class);
    }

    @Provides
    @Singleton
    IHackerNewsService providesHackerNewsService() {
        return new HackerNewsService();
    }
}