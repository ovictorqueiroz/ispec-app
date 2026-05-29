package br.com.etecia.ispec_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.etecia.ispec_app.model.InspecaoModel;
import br.com.etecia.ispec_app.model.PerguntaInspecaoModel;
import br.com.etecia.ispec_app.repository.InspecaoCallback;
import br.com.etecia.ispec_app.repository.InspecaoRepository;
import br.com.etecia.ispec_app.requests.InspecaoRequest;

public class InspecaoViewModel extends AndroidViewModel {

    private static final String TAG = "InspecaoViewModel";

    private final InspecaoRepository repository;

    private final MutableLiveData<List<InspecaoModel>> inspecoesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PerguntaInspecaoModel>> perguntasLiveData = new MutableLiveData<>();
    private final MutableLiveData<InspecaoModel> inspecaoCriadaLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erroLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> carregandoLiveData = new MutableLiveData<>();

    public InspecaoViewModel(Application application) {
        super(application);
        repository = new InspecaoRepository(application.getApplicationContext());
    }

    public LiveData<List<InspecaoModel>> getInspecoes() { return inspecoesLiveData; }
    public LiveData<List<PerguntaInspecaoModel>> getPerguntas() { return perguntasLiveData; }
    public LiveData<InspecaoModel> getInspecaoCriada() { return inspecaoCriadaLiveData; }
    public LiveData<String> getErro() { return erroLiveData; }
    public LiveData<Boolean> getCarregando() { return carregandoLiveData; }

    public void listarTodas() {
        carregandoLiveData.postValue(true);
        repository.listarTodas(new InspecaoCallback() {
            @Override
            public void onSucesso(List<InspecaoModel> inspecoes) {
                inspecoesLiveData.postValue(inspecoes);
                carregandoLiveData.postValue(false);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e(TAG, "Erro ao listar inspeções: " + mensagem);
                erroLiveData.postValue(mensagem);
                carregandoLiveData.postValue(false);
            }
        });
    }

    public void buscarPerguntasPorTipo(String tipo) {
        carregandoLiveData.postValue(true);
        repository.buscarPerguntasPorTipo(tipo, new InspecaoRepository.PerguntasCallback() {
            @Override
            public void onSucesso(List<PerguntaInspecaoModel> perguntas) {
                perguntasLiveData.postValue(perguntas);
                carregandoLiveData.postValue(false);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e(TAG, "Erro ao buscar perguntas: " + mensagem);
                erroLiveData.postValue(mensagem);
                carregandoLiveData.postValue(false);
            }
        });
    }

    public void criarInspecao(InspecaoRequest request) {
        carregandoLiveData.postValue(true);
        repository.criarInspecao(request, new InspecaoRepository.CriarInspecaoCallback() {
            @Override
            public void onSucesso(InspecaoModel inspecao) {
                inspecaoCriadaLiveData.postValue(inspecao);
                carregandoLiveData.postValue(false);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e(TAG, "Erro ao criar inspeção: " + mensagem);
                erroLiveData.postValue(mensagem);
                carregandoLiveData.postValue(false);
            }
        });
    }
}
