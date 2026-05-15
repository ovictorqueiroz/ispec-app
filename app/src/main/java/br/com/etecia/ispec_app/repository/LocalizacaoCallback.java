package br.com.etecia.ispec_app.repository;

import java.util.List;

import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.model.LocalizacaoModel;

public interface LocalizacaoCallback {
    void onSucesso(LocalizacaoModel localizacao);
    void onErro(String mensagem);
}
