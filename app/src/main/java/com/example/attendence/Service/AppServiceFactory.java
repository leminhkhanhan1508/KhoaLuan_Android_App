package com.example.attendence.Service;

import android.content.res.Resources;
import com.example.attendence.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.Settings.System.getString;

public class AppServiceFactory {
    public static Retrofit retrofit;
    private static AppService appService;
//    String string = getString(R.string.URL);
    public static void getInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://attendanceuit.azurewebsites.net")
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
