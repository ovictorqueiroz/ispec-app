package br.com.etecia.ispec_app.network;

import br.com.etecia.ispec_app.model.EquipamentoRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    /**
     * Cadastra um novo equipamento.
     * Endpoint esperado na API Spring Boot: POST /equipamentos
     *
     * Ajuste o path se o seu controller usar uma rota diferente.
     */
    @POST("equipamentos")
    Call<Void> cadastrarEquipamento(@Body EquipamentoRequest request);
}
