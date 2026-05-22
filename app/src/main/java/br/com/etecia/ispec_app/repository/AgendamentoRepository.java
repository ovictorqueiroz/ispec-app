package br.com.etecia.ispec_app.repository;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.etecia.ispec_app.model.AgendamentoModel;
import br.com.etecia.ispec_app.model.UsuarioModel;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendamentoRepository {

    private final ApiService apiService;
    private static final String TAG = "AgendamentoRepository";

    public AgendamentoRepository(Context context) {
        apiService = RetrofitClient.getClient(context.getApplicationContext()).create(ApiService.class);
    }

    public void listarTodos(AgendamentoCallback callback) {
        Call<List<AgendamentoModel>> call = apiService.listarTodosAgendamentos();
        call.enqueue(new Callback<List<AgendamentoModel>>() {
            @Override
            public void onResponse(Call<List<AgendamentoModel>> call, Response<List<AgendamentoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AgendamentoModel>> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void salvar(AgendamentoModel agendamento, SalvarAgendamentoCallback callback) {
        Call<AgendamentoModel> call = apiService.salvarAgendamento(agendamento);
        call.enqueue(new Callback<AgendamentoModel>() {
            @Override
            public void onResponse(Call<AgendamentoModel> call, Response<AgendamentoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    Log.e(TAG, "Erro ao salvar agendamento: " + response.code());
                    callback.onErro("Erro " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AgendamentoModel> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage());
                callback.onErro(t.getMessage());
            }
        });
    }

    public void listarUsuarios(UsuarioCallback callback) {
        Call<List<UsuarioModel>> call = apiService.listarUsuarios();
        call.enqueue(new Callback<List<UsuarioModel>>() {
            @Override
            public void onResponse(Call<List<UsuarioModel>> call, Response<List<UsuarioModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    Log.e(TAG, "Erro ao listar usuários: " + response.code());
                    callback.onErro("Erro " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioModel>> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage());
                callback.onErro(t.getMessage());
            }
        });
    }

    public interface SalvarAgendamentoCallback {
        void onSucesso(AgendamentoModel agendamento);
        void onErro(String mensagem);
    }
}
