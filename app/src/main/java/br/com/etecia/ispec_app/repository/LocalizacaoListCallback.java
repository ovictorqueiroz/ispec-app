package br.com.etecia.ispec_app.repository;

import java.util.List;
import br.com.etecia.ispec_app.model.LocalizacaoModel;

public interface LocalizacaoListCallback {
    void onSucesso(List<LocalizacaoModel> localizacoes);
    void onErro(String mensagem);
}
