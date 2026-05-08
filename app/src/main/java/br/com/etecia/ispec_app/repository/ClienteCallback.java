package br.com.etecia.ispec_app.repository;

public class ClienteCallback {
    void onSucesso(List<ClienteModel> clientes);
    void onErro(String mensagem);
}
