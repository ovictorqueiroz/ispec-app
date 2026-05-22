package br.com.etecia.ispec_app.service;

import com.google.gson.JsonObject;

import java.util.List;

import br.com.etecia.ispec_app.model.AgendamentoModel;
import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.model.LocalizacaoModel;
import br.com.etecia.ispec_app.model.UsuarioModel;
import br.com.etecia.ispec_app.requests.EquipamentoRequest;
import br.com.etecia.ispec_app.requests.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // === CLIENTES ===
    @GET("clientes/{id}")
    Call<ClienteModel> buscarCliente(@Path("id") Long id);

    @GET("clientes")
    Call<List<ClienteModel>> listarClientes();

    @GET("localizacoes/{id}")
    Call<LocalizacaoModel> buscarLocalizacao(@Path("id") Long id);

    @GET("clientes/{id}/equipamentos")
    Call<List<JsonObject>> listarPorCliente(@Path("id") Long id);

    // === AGENDAMENTOS ===
    @GET("agendamentos")
    Call<List<AgendamentoModel>> listarTodosAgendamentos();

    @POST("agendamentos")
    Call<AgendamentoModel> salvarAgendamento(@Body AgendamentoModel agendamento);

    // === USUÁRIOS ===
    @GET("usuarios")
    Call<List<UsuarioModel>> listarUsuarios();

    // === AUTH ===
    @POST("auth/login")
    Call<String> autenticaUsuario(@Body LoginRequest request);

    // === EQUIPAMENTOS ===
    @POST("equipamentos")
    Call<Void> cadastrarEquipamento(@Body EquipamentoRequest request);
}
