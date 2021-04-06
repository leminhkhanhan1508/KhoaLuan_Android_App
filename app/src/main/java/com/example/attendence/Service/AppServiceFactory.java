package com.example.attendence.Service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppServiceFactory {
    public static Retrofit retrofit;
    private static AppService appService;

    public static void getInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.108:8000")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            appService = retrofit.create(AppService.class);
        }

    }

    public static AppService getAppService() {
        if (appService == null) {
            getInstance();
        }
        return appService;
    }

}
