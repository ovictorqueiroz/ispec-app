package br.com.etecia.ispec_app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Troque pelo IP/URL da sua API Spring Boot
    // Em emulador Android, use 10.0.2.2 para acessar o localhost da máquina
    private static final String BASE_URL = "http://192.168.1.13:8080/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
