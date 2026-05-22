package br.com.etecia.ispec_app.repository;

import java.util.List;

import br.com.etecia.ispec_app.model.AgendamentoModel;

public interface AgendamentoCallback {
    void onSucesso(List<AgendamentoModel> agendamentos);
    void onErro(String mensagem);
}
