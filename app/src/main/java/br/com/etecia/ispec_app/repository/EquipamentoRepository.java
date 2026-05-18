package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.etecia.ispec_app.model.EquipamentoModel;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipamentoRepository {
    private ApiService equipamentoApiService;

    public EquipamentoRepository(Context context){
        equipamentoApiService = RetrofitClient.getClient(context.getApplicationContext()).create(ApiService.class);
    }

    public void listarPorCliente(EquipamentoCallback callback, Long id){
        Call<List<EquipamentoModel>> call = equipamentoApiService.listarPorCliente(id);

        call.enqueue(new Callback<List<EquipamentoModel>>() {
            @Override
            public void onResponse(Call<List<EquipamentoModel>> call, Response<List<EquipamentoModel>> response) {
                if(response.isSuccessful()){
                    callback.onSucesso(response.body());
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
            public void onFailure(Call<List<EquipamentoModel>> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }
}
