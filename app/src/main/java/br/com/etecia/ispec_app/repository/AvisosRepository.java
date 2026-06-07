package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;

import br.com.etecia.ispec_app.model.AvisosResponse;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Responsável por buscar os dados do endpoint GET /avisos.
 * Segue o padrão Repository do projeto:
 *   - Recebe Context para criar o RetrofitClient com token JWT
 *   - Usa callback para comunicar resultado à ViewModel
 */
public class AvisosRepository {

    private static final String TAG = "AvisosRepository";
    private final ApiService apiService;

    public AvisosRepository(Context context) {
        apiService = RetrofitClient.getClient(context.getApplicationContext())
                .create(ApiService.class);
    }

    public void buscarAvisos(AvisosCallback callback) {
        apiService.buscarAvisos().enqueue(new Callback<AvisosResponse>() {
            @Override
            public void onResponse(Call<AvisosResponse> call, Response<AvisosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    Log.e(TAG, "Erro ao buscar avisos: " + response.code());
                    callback.onErro("Erro " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AvisosResponse> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage());
                callback.onErro(t.getMessage());
            }
        });
    }
}
