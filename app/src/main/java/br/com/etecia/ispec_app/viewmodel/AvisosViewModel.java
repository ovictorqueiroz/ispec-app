package br.com.etecia.ispec_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.com.etecia.ispec_app.model.AvisosResponse;
import br.com.etecia.ispec_app.repository.AvisosCallback;
import br.com.etecia.ispec_app.repository.AvisosRepository;

/**
 * ViewModel para a tela de Avisos.
 *
 * Expõe:
 *   - avisos         → resultado completo do GET /avisos
 *   - erro           → mensagem de falha para Toast
 *   - carregando     → controla o SwipeRefreshLayout
 *   - totalAvisos    → badge = vencidos + vencendo30 + vencendo90 + reprovadas
 *
 * Segue o padrão AndroidViewModel do projeto (InspecaoViewModel, AgendamentoViewModel).
 */
public class AvisosViewModel extends AndroidViewModel {

    private static final String TAG = "AvisosViewModel";

    private final AvisosRepository repository;

    private final MutableLiveData<AvisosResponse> avisosLiveData   = new MutableLiveData<>();
    private final MutableLiveData<String>          erroLiveData      = new MutableLiveData<>();
    private final MutableLiveData<Boolean>         carregandoLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer>         totalAvisosLiveData = new MutableLiveData<>();

    public AvisosViewModel(Application application) {
        super(application);
        repository = new AvisosRepository(application.getApplicationContext());
    }

    // --- Exposição de LiveData ---

    public LiveData<AvisosResponse> getAvisos()       { return avisosLiveData; }
    public LiveData<String>          getErro()         { return erroLiveData; }
    public LiveData<Boolean>         getCarregando()   { return carregandoLiveData; }
    public LiveData<Integer>         getTotalAvisos()  { return totalAvisosLiveData; }

    // --- Ação pública ---

    public void carregarAvisos() {
        carregandoLiveData.postValue(true);
        repository.buscarAvisos(new AvisosCallback() {
            @Override
            public void onSucesso(AvisosResponse response) {
                avisosLiveData.postValue(response);

                // Calcula badge: vencidos + vencendo30 + vencendo90 + reprovadas
                // Agenda da semana NÃO entra no badge (READMEFIRST)
                int total = 0;
                if (response.getVencidos()   != null) total += response.getVencidos().size();
                if (response.getVencendo30() != null) total += response.getVencendo30().size();
                if (response.getVencendo90() != null) total += response.getVencendo90().size();
                if (response.getReprovadas() != null) total += response.getReprovadas().size();
                totalAvisosLiveData.postValue(total);

                carregandoLiveData.postValue(false);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e(TAG, "Erro ao carregar avisos: " + mensagem);
                erroLiveData.postValue(mensagem);
                carregandoLiveData.postValue(false);
            }
        });
    }
}
