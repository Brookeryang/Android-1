package com.maqiang.doctor.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.maqiang.doctor.net.callAdapter.HttpCallAdapterFactory;
import com.maqiang.doctor.net.converter.GsonConverterFactory;
import com.maqiang.doctor.utils.ConstantUtil;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by maqiang on 2017/3/9.
 */

public class APIGenerator {
  private static OkHttpClient.Builder sBuilder = new OkHttpClient.Builder();
  private static Retrofit sRetrofit;

  static {
    APIGenerator.sBuilder.addInterceptor(HttpLoggingInterceptor.createLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY));

    APIGenerator.sRetrofit = new Retrofit.Builder().baseUrl(ConstantUtil.JUHE_BASE_URL)
        .addCallAdapterFactory(HttpCallAdapterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(sBuilder.build())
        .build();
  }

  public static <T> T createService(Class<T> serviceClass) {
    return sRetrofit.create(serviceClass);
  }
}
