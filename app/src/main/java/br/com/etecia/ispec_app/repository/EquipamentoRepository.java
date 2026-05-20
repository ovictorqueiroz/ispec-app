package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import br.com.etecia.ispec_app.adapter.EquipamentoTypeAdapter;
import br.com.etecia.ispec_app.model.EquipamentoModel;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipamentoRepository {
    private ApiService equipamentoApiService;
    private EquipamentoTypeAdapter typeAdapter = new EquipamentoTypeAdapter();

    public EquipamentoRepository(Context context) {
        equipamentoApiService = RetrofitClient.getClient(context.getApplicationContext()).create(ApiService.class);
    }

    public void listarPorCliente(EquipamentoCallback callback, Long id) {
        Call<List<JsonObject>> call = equipamentoApiService.listarPorCliente(id);

        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<EquipamentoModel> equipamentos = new ArrayList<>();
                    for (JsonObject json : response.body()) {
                        try {
                            EquipamentoModel equipamento = typeAdapter.deserialize(json, EquipamentoModel.class, null);
                            equipamentos.add(equipamento);
                        } catch (Exception e) {
                            Log.e("REPOSITORY", "Erro ao deserializar equipamento: " + e.getMessage());
                        }
                    }
                    callback.onSucesso(equipamentos);
                } else {
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
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }
}