package br.com.etecia.ispec_app.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.repository.ClienteCallback;
import br.com.etecia.ispec_app.repository.ClienteRepository;


public class ClienteViewModel extends ViewModel {
    private MutableLiveData<List<ClienteModel>> clientesLiveData = new MutableLiveData<>();

    private ClienteRepository repository = new ClienteRepository();

    public LiveData<List<ClienteModel>> getClientes() {
        return clientesLiveData;
    }

    public void buscarClientes(){
        repository.listarClientes(
            new ClienteCallback() {
            @Override
            public void onSucesso(List<ClienteModel> clientes) {
                clientesLiveData.postValue(clientes);
            }

            @Override
            public void onErro(String mensagem) {
                Log.e("ViewModel", mensagem);
            }


        });
    }
}


