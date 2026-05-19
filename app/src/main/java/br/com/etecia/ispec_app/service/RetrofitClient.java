package br.com.etecia.ispec_app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import br.com.etecia.ispec_app.adapter.RuntimeTypeAdapterFactory;
import br.com.etecia.ispec_app.model.AlarmeModel;
import br.com.etecia.ispec_app.model.EquipamentoModel;
import br.com.etecia.ispec_app.model.ExtintorModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.67.97.2:8080/";
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

            RuntimeTypeAdapterFactory<EquipamentoModel> adapterFactory = //está vermelho
                    RuntimeTypeAdapterFactory.of(EquipamentoModel.class, "tipo")
                            .registerSubtype(ExtintorModel.class, "extintor")
                            .registerSubtype(AlarmeModel.class, "alarme");


            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(adapterFactory)
                    .build(); //aqui continua vermelho

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // usa o client com interceptor
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}