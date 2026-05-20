package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName; // FIX Bug #1: import correto do Gson

public class AlarmeModel extends EquipamentoModel {

    @SerializedName("tipo")
    private final String tipo = "alarme"; // obrigatório para o @JsonTypeInfo do backend

    private TipoSensorModel tipoSensor;

    private boolean funcionando;
    private String ultimaVerificacao;

    public AlarmeModel() {}

    public String getTipo() {
        return tipo;
    }

    public boolean isFuncionando() {
        return funcionando;
    }

    public void setFuncionando(boolean funcionando) {
        this.funcionando = funcionando;
    }

    public String getUltimaVerificacao() {
        return ultimaVerificacao;
    }

    public void setUltimaVerificacao(String ultimaVerificacao) {
        this.ultimaVerificacao = ultimaVerificacao;
    }

    public TipoSensorModel getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(TipoSensorModel tipoSensor) {
        this.tipoSensor = tipoSensor;
    }
}
