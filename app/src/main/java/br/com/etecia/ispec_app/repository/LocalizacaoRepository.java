package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.etecia.ispec_app.model.LocalizacaoModel;
import br.com.etecia.ispec_app.requests.LocalizacaoRequest;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalizacaoRepository {
    private final ApiService api;

    public LocalizacaoRepository(Context context) {
        api = RetrofitClient.getClient(context.getApplicationContext()).create(ApiService.class);
    }

    public void buscarLocalizacao(LocalizacaoCallback callback, Long id) {
        api.buscarLocalizacao(id).enqueue(new Callback<LocalizacaoModel>() {
            @Override
            public void onResponse(Call<LocalizacaoModel> call, Response<LocalizacaoModel> response) {
                if (response.isSuccessful()) {
                    callback.onSucesso(response.body());
                } else {
                    Log.e("RETROFIT_ERRO", "Código: " + response.code());
                    callback.onErro(response.message());
                }
            }
            @Override
            public void onFailure(Call<LocalizacaoModel> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void listarPorCliente(Long clienteId, LocalizacaoListCallback callback) {
        api.listarLocalizacoesPorCliente(clienteId).enqueue(new Callback<List<LocalizacaoModel>>() {
            @Override
            public void onResponse(Call<List<LocalizacaoModel>> call, Response<List<LocalizacaoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao buscar localizações: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<LocalizacaoModel>> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void criarLocalizacao(LocalizacaoRequest request, LocalizacaoCallback callback) {
        api.criarLocalizacao(request).enqueue(new Callback<LocalizacaoModel>() {
            @Override
            public void onResponse(Call<LocalizacaoModel> call, Response<LocalizacaoModel> response) {
                if (response.isSuccessful()) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao criar localização: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<LocalizacaoModel> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }
}
