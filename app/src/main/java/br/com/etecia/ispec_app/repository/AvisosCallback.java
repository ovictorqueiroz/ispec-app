package br.com.etecia.ispec_app.repository;

import br.com.etecia.ispec_app.model.AvisosResponse;

/**
 * Callback assíncrono para o AvisosRepository.
 * Segue o mesmo padrão dos demais callbacks do projeto
 * (InspecaoCallback, ClienteCallback, etc.).
 */
public interface AvisosCallback {
    void onSucesso(AvisosResponse response);
    void onErro(String mensagem);
}
