package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
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
                    Log.e("RETROFIT_ERRO", "Código: " + response.code() + " | " + response.message());
                    callback.onErro(response.message());
                }
            }
            @Override
            public void onFailure(Call<LocalizacaoModel> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    // Busca TODAS as localizações e filtra localmente pelo clienteId.
    // Não exige nenhum endpoint novo no backend — usa o GET /localizacoes que já existe.
    public void listarPorCliente(Long clienteId, LocalizacaoListCallback callback) {
        api.listarTodasLocalizacoes().enqueue(new Callback<List<LocalizacaoModel>>() {
            @Override
            public void onResponse(Call<List<LocalizacaoModel>> call, Response<List<LocalizacaoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LocalizacaoModel> filtradas = new ArrayList<>();
                    for (LocalizacaoModel loc : response.body()) {
                        if (loc.getCliente() != null
                                && clienteId.equals(loc.getCliente().getId())) {
                            filtradas.add(loc);
                        }
                    }
                    callback.onSucesso(filtradas);
                } else {
                    Log.e("RETROFIT_ERRO", "Localizações: código " + response.code());
                    callback.onErro("Erro ao buscar localizações: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<LocalizacaoModel>> call, Throwable t) {
                Log.e("RETROFIT_ERRO", "Falha localizações: " + t.getMessage());
                callback.onErro(t.getMessage());
            }
        });
    }

    // Cria nova localização (usa POST /localizacoes que já existe no backend)
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
