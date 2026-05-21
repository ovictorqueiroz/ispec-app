package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName;

public class HidranteModel extends EquipamentoModel {

    @SerializedName("tipo")
    private final String tipo = "hidrante";

    @SerializedName("pressaoAgua")
    private Double pressaoAgua;

    @SerializedName("comprimentoMangueira")
    private Double comprimentoMangueira;

    public HidranteModel() {}

    public String getTipo() { return tipo; }

    public Double getPressaoAgua() { return pressaoAgua; }
    public void setPressaoAgua(Double pressaoAgua) { this.pressaoAgua = pressaoAgua; }

    public Double getComprimentoMangueira() { return comprimentoMangueira; }
    public void setComprimentoMangueira(Double comprimentoMangueira) { this.comprimentoMangueira = comprimentoMangueira; }
}
