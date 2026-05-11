package br.com.etecia.ispec_app.repository;
import java.util.List;

import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Context;
import android.util.Log;

public class ClienteRepository {
    private ApiService clienteApiService;

    public ClienteRepository(Context context) {
        clienteApiService = RetrofitClient.getClient(context.getApplicationContext()).create(ApiService.class);
    }

    public void listarClientes(ClienteCallback callback){
        Call<List<ClienteModel>> call = clienteApiService.listarClientes();


        call.enqueue(new Callback<List<ClienteModel>>() {
            @Override
            public void onResponse(Call<List<ClienteModel>> call, Response<List<ClienteModel>> response) {
                if (response.isSuccessful()){
                 callback.onSucesso(response.body()); // Solicita na API e joga o resultado pro callback
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
            public void onFailure(Call<List<ClienteModel>> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }
}
