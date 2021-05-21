package com.ripetizioni.app.api;


import com.ripetizioni.app.utils.LoginManager;
import com.ripetizioni.app.utils.UnauthorizedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "INDIRIZZO_IP_WEBAPP:8080/api";
    private static Retrofit retrofit;



    private RetrofitClient(){

    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClient())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(2, TimeUnit.SECONDS);


        httpClient.addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl.Builder builder = request.url().newBuilder();


            String token = LoginManager.getToken();

            if (token != null)
                builder = builder.addQueryParameter("token",token);


            HttpUrl url = builder.build();

            request = request.newBuilder().url(url).build();
            Response res= chain.proceed(request);

            if(res.code() == 401)
                EventBus.getDefault().post(UnauthorizedEvent.instance());
            return res;



        });

        return httpClient.build();
    }


    public static Api getApi(){
        return getRetrofitInstance().create(Api.class);
    }
}
