package br.com.etecia.ispec_app.repository;

import java.util.List;

import br.com.etecia.ispec_app.model.UsuarioModel;

public interface UsuarioCallback {
    void onSucesso(List<UsuarioModel> usuarios);
    void onErro(String mensagem);
}
