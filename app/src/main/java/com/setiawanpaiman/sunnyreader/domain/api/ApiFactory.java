package com.setiawanpaiman.sunnyreader.domain.api;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import rx.schedulers.Schedulers;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private final Retrofit mRetrofit;

    public ApiFactory(@NonNull String baseUrl, @NonNull Gson gson) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Log.d("Retrofit", request.url().toString());
                        return chain.proceed(request);
                    }
                })
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }
}
