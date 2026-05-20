package br.com.etecia.ispec_app.service;

import com.google.gson.JsonObject;

import java.util.List;

import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.model.LocalizacaoModel;
import br.com.etecia.ispec_app.requests.EquipamentoRequest;
import br.com.etecia.ispec_app.requests.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    //===GETS===
    @GET("clientes/{id}")
    Call<ClienteModel> buscarCliente(@Path("id") Long id);

    @GET("localizacoes/{id}")
    Call<LocalizacaoModel> buscarLocalizacao(@Path("id") Long id);

    @GET("clientes")
    Call<List<ClienteModel>> listarClientes();

    // Agora retorna List<JsonObject> para deserializar manualmente no Repository
    @GET("clientes/{id}/equipamentos")
    Call<List<JsonObject>> listarPorCliente(@Path("id") Long id);

    //===POSTS===
    @POST("auth/login")
    Call<String> autenticaUsuario(@Body LoginRequest request);

    @POST("equipamentos")
    Call<Void> cadastrarEquipamento(@Body EquipamentoRequest request);
}