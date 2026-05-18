package br.com.etecia.ispec_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.etecia.ispec_app.model.EquipamentoModel;
import br.com.etecia.ispec_app.repository.EquipamentoCallback;
import br.com.etecia.ispec_app.repository.EquipamentoRepository;

public class EquipamentoViewModel extends AndroidViewModel {
    private MutableLiveData<List<EquipamentoModel>> equipamentoLiveData = new MutableLiveData<>();
    private EquipamentoRepository repository;

    public EquipamentoViewModel(Application application) {
        super(application);
        repository = new EquipamentoRepository(application.getApplicationContext());
    }

    public LiveData<List<EquipamentoModel>> getEquipamentos() {
        return equipamentoLiveData;
    }

    public void listarPorCliente(Long id) {
        repository.listarPorCliente(new EquipamentoCallback() {
                                        @Override
                                        public void onSucesso(List<EquipamentoModel> equipamento) {
                                            equipamentoLiveData.postValue(equipamento);
                                        }

                                        @Override
                                        public void onErro(String mensagem) {
                                            Log.e("ViewModel", mensagem);
                                        }
                                    }, id

        );
    }
}
