package br.com.etecia.ispec_app.network;
import br.com.etecia.ispec_app.EquipamentoModel;
import br.com.etecia.ispec_app.ExtintorModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {
    @POST("equipamentos") // caminho do seu endpoint
    Call<ExtintorModel> salvarExtintor(@Body ExtintorModel equipamento);
}
