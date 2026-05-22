package br.com.etecia.ispec_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.etecia.ispec_app.model.AgendamentoModel;
import br.com.etecia.ispec_app.model.UsuarioModel;
import br.com.etecia.ispec_app.repository.AgendamentoCallback;
import br.com.etecia.ispec_app.repository.AgendamentoRepository;
import br.com.etecia.ispec_app.repository.UsuarioCallback;

public class AgendamentoViewModel extends AndroidViewModel {

    private static final String TAG = "AgendamentoViewModel";

    private final AgendamentoRepository repository;

    private final MutableLiveData<List<AgendamentoModel>> agendamentosLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<UsuarioModel>> usuariosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> salvouComSucesso = new MutableLiveData<>();
    private final MutableLiveData<String> erroLiveData = new MutableLiveData<>();

    public AgendamentoViewModel(Application application) {
        super(application);
        repository = new AgendamentoRepository(application.getApplicationContext());
    }

    public LiveData<List<AgendamentoModel>> getAgendamentos() { return agendamentosLiveData; }
    public LiveData<List<UsuarioModel>> getUsuarios() { return usuariosLiveData; }
    public LiveData<Boolean> getSalvouComSucesso() { return salvouComSucesso; }
    public LiveData<String> getErro() { return erroLiveData; }

    public void buscarAgendamentos() {
        repository.listarTodos(new AgendamentoCallback() {
            @Override
            public void onSucesso(List<AgendamentoModel> agendamentos) {
                agendamentosLiveData.postValue(agendamentos);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e(TAG, "Erro ao buscar agendamentos: " + mensagem);
                erroLiveData.postValue(mensagem);
            }
        });
    }

    public void buscarUsuarios() {
        repository.listarUsuarios(new UsuarioCallback() {
            @Override
            public void onSucesso(List<UsuarioModel> usuarios) {
                usuariosLiveData.postValue(usuarios);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e(TAG, "Erro ao buscar usuários: " + mensagem);
                erroLiveData.postValue(mensagem);
            }
        });
    }

    public void salvarAgendamento(AgendamentoModel agendamento) {
        repository.salvar(agendamento, new AgendamentoRepository.SalvarAgendamentoCallback() {
            @Override
            public void onSucesso(AgendamentoModel saved) {
                salvouComSucesso.postValue(true);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e(TAG, "Erro ao salvar: " + mensagem);
                erroLiveData.postValue(mensagem);
            }
        });
    }
}
