package br.com.etecia.ispec_app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.67.96.23:8080/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

            // Interceptor: intercepta TODA requisição antes de enviar
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {

                        // Lê o token salvo no SharedPreferences
                        SharedPreferences prefs = context.getSharedPreferences("sessao", Context.MODE_PRIVATE);
                        String token = prefs.getString("token", "");
                        Log.d("INTERCEPTOR", "Token lido: " + token);

                        // Pega a requisição original e adiciona o header
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // usa o client com interceptor
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}