package br.com.etecia.ispec_app.repository;
import java.util.List;

import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepository {
    private ApiService clienteApiService;

    public ClienteRepository() {
        clienteApiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void listarClientes(ClienteCallback callback){
        Call<List<ClienteModel>> call = clienteApiService.listarClientes();


        call.enqueue(new Callback<List<ClienteModel>>() {
            @Override
            public void onResponse(Call<List<ClienteModel>> call, Response<List<ClienteModel>> response) {
                if (response.isSuccessful()){
                 callback.onSucesso(response.body());
                }
                else {
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
