package br.com.etecia.ispec_app.repository;

import java.util.List;

import br.com.etecia.ispec_app.model.InspecaoModel;

public interface InspecaoCallback {
    void onSucesso(List<InspecaoModel> inspecoes);
    void onErro(String mensagem);
}
