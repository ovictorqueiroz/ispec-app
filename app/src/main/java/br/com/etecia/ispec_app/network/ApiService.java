package br.com.etecia.ispec_app.network;

import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.requests.EquipamentoRequest;
import br.com.etecia.ispec_app.requests.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    /**
     * Cadastra um novo equipamento.
     * Endpoint esperado na API Spring Boot: POST /equipamentos
     *
     * Ajuste o path se o seu controller usar uma rota diferente.
     */
    @POST("equipamentos")
    Call<Void> cadastrarEquipamento(@Body EquipamentoRequest request);

    @GET("clientes/{id}")
    Call<ClienteModel> buscarCliente(@Path("id") Long id);

    @POST("auth/login")
    Call<String> autenticaUsuario(@Body LoginRequest request);

}
