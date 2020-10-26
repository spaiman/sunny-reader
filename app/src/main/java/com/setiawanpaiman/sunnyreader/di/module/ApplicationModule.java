package com.setiawanpaiman.sunnyreader.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.setiawanpaiman.sunnyreader.BuildConfig;
import com.setiawanpaiman.sunnyreader.domain.api.ApiFactory;
import com.setiawanpaiman.sunnyreader.domain.api.HackerNewsApi;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsDiskStore;
import com.setiawanpaiman.sunnyreader.domain.persistent.HackerNewsPersistent;
import com.setiawanpaiman.sunnyreader.domain.service.HackerNewsService;
import com.setiawanpaiman.sunnyreader.domain.service.IHackerNewsService;
import com.setiawanpaiman.sunnyreader.util.GsonParser;
import com.setiawanpaiman.sunnyreader.util.JsonParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private static final String SHARED_PREFERENCES_NAME = "app_preferences";

    private Application mApplication;

    public ApplicationModule(@NonNull Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    JsonParser providesJsonParser(Gson gson) {
        return new GsonParser(gson);
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences() {
        return mApplication.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
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
    HackerNewsPersistent providesHackerNewsPersistent(JsonParser jsonParser,
                                                      SharedPreferences sharedPreferences) {
        return new HackerNewsDiskStore(jsonParser, sharedPreferences);
    }

    @Provides
    @Singleton
    IHackerNewsService providesHackerNewsService(HackerNewsApi hackerNewsApi,
                                                 HackerNewsPersistent hackerNewsPersistent) {
        return new HackerNewsService(hackerNewsApi, hackerNewsPersistent);
    }
}