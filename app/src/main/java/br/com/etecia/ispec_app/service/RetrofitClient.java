package br.com.etecia.ispec_app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://localhost:8080/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        SharedPreferences prefs = context.getSharedPreferences("sessao", Context.MODE_PRIVATE);
                        String token = prefs.getString("token", "");
                        Log.d("INTERCEPTOR", "Token lido: " + token);

                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            Gson gson = new Gson();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}