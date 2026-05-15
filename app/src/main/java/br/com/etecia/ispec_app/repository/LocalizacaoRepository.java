package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;
import br.com.etecia.ispec_app.model.LocalizacaoModel;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalizacaoRepository {
    private ApiService localizacaoApiService;

    public LocalizacaoRepository(Context context) {
        localizacaoApiService = RetrofitClient.getClient(context.getApplicationContext()).create(ApiService.class);
    }

    public void buscarLocalizacao(LocalizacaoCallback callback, Long id){
        Call<LocalizacaoModel> call = localizacaoApiService.buscarLocalizacao(id);

        call.enqueue(new Callback<LocalizacaoModel>() {
            @Override
            public void onResponse(Call<LocalizacaoModel> call, Response<LocalizacaoModel> response) {
                if (response.isSuccessful()){
                 callback.onSucesso(response.body()); // Porque esse response está esperando uma lista?

                }
                else {
                    Log.e("RETROFIT_ERRO", "Código: " + response.code() + " | Mensagem: " + response.message());
                    try {
                        Log.e("RETROFIT_ERRO", "Body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("RETROFIT_ERRO", "Sem body");
                    }
                    callback.onErro(response.message());
                }
            }

            @Override
            public void onFailure(Call<LocalizacaoModel> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }
}
