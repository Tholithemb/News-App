package com.southaficannewsapp.rests;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.southaficannewsapp.util.NetworkConnection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://newsapi.org/v2/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
         int cacheSize = 10 * 1024 * 1024; // 10mb

       final NetworkConnection connection =new NetworkConnection(context);


        Interceptor onlineInterceptor =new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Response response  =chain.proceed(chain.request());
                int maxAge =60; // read from cache for 60 seconds even if there is internet connection

                return response.newBuilder()
                        .header("Cache-Control","public, max-age"+maxAge)
                        .removeHeader("Pragma")
                        .build();
            }
        };

        Interceptor offlineInterceptor  =new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!connection.isNetworkAvailable()){
                    int maxStale =60 * 60 * 24 * 10; // offline cache available for 10 days
                    request = request.newBuilder()
                            .header("Cache-Control","public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return chain.proceed(request);
            }
        };

        Cache cache =new Cache(context.getCacheDir(),cacheSize);

        OkHttpClient okHttpClient =new OkHttpClient.Builder()
               .addInterceptor(offlineInterceptor)
                .addNetworkInterceptor(onlineInterceptor)
                .cache(cache)
                .build();

        if (retrofit==null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }
}
