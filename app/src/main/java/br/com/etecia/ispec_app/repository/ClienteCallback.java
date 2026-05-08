package br.com.etecia.ispec_app.repository;

import java.util.List;

import br.com.etecia.ispec_app.model.ClienteModel;

public interface ClienteCallback {
    void onSucesso(List<ClienteModel> clientes){};
    void onErro(String mensagem){};
}
