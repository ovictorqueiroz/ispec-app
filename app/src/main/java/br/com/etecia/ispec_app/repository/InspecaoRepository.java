package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.etecia.ispec_app.model.InspecaoModel;
import br.com.etecia.ispec_app.model.PerguntaInspecaoModel;
import br.com.etecia.ispec_app.requests.InspecaoRequest;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InspecaoRepository {

    private static final String TAG = "InspecaoRepository";
    private final ApiService apiService;

    public InspecaoRepository(Context context) {
        apiService = RetrofitClient.getClient(context.getApplicationContext()).create(ApiService.class);
    }

    public void listarTodas(InspecaoCallback callback) {
        apiService.listarInspecoes().enqueue(new Callback<List<InspecaoModel>>() {
            @Override
            public void onResponse(Call<List<InspecaoModel>> call, Response<List<InspecaoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    Log.e(TAG, "Erro ao listar inspeções: " + response.code());
                    callback.onErro("Erro " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<InspecaoModel>> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage());
                callback.onErro(t.getMessage());
            }
        });
    }

    public void buscarPerguntasPorTipo(String tipo, PerguntasCallback callback) {
        apiService.listarPerguntasPorTipo(tipo).enqueue(new Callback<List<PerguntaInspecaoModel>>() {
            @Override
            public void onResponse(Call<List<PerguntaInspecaoModel>> call, Response<List<PerguntaInspecaoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    Log.e(TAG, "Erro ao buscar perguntas: " + response.code());
                    callback.onErro("Erro " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PerguntaInspecaoModel>> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage());
                callback.onErro(t.getMessage());
            }
        });
    }

    public void criarInspecao(InspecaoRequest request, CriarInspecaoCallback callback) {
        apiService.criarInspecao(request).enqueue(new Callback<InspecaoModel>() {
            @Override
            public void onResponse(Call<InspecaoModel> call, Response<InspecaoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    Log.e(TAG, "Erro ao criar inspeção: " + response.code());
                    callback.onErro("Erro " + response.code());
                }
            }

            @Override
            public void onFailure(Call<InspecaoModel> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage());
                callback.onErro(t.getMessage());
            }
        });
    }

    public interface PerguntasCallback {
        void onSucesso(List<PerguntaInspecaoModel> perguntas);
        void onErro(String mensagem);
    }

    public interface CriarInspecaoCallback {
        void onSucesso(InspecaoModel inspecao);
        void onErro(String mensagem);
    }
}
