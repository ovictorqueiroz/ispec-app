package br.com.etecia.ispec_app.repository;

import java.util.List;

import br.com.etecia.ispec_app.model.EquipamentoModel;

public interface EquipamentoCallback {
    void onSucesso (List<EquipamentoModel> equipamento);

    void onErro (String mensagem);
}
