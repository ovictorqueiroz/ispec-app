package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName;

public class EquipamentoRequest {

    // Campos comuns
    @SerializedName("idCliente")
    private int idCliente;

    @SerializedName("nome")
    private String nome;

    @SerializedName("localizacao")
    private String localizacao;

    @SerializedName("dataInstalacao")
    private String dataInstalacao;   // formato: dd/MM/yyyy

    @SerializedName("status")
    private String status;           // "Ativo" ou "Inativo"

    @SerializedName("tipoEquipamento")
    private String tipoEquipamento;  // "Alarme", "Extintor" ou "Hidrante"

    // Campos Extintor
    @SerializedName("classeFogo")
    private String classeFogo;

    @SerializedName("capacidade")
    private Double capacidade;

    @SerializedName("dataValidade")
    private String dataValidade;     // formato: dd/MM/yyyy

    @SerializedName("pressao")
    private Double pressao;

    // Campos Alarme
    @SerializedName("tipoSensor")
    private String tipoSensor;

    @SerializedName("ultimaVerificacao")
    private String ultimaVerificacao; // formato: dd/MM/yyyy

    @SerializedName("funcionando")
    private Boolean funcionando;

    // Campos Hidrante
    @SerializedName("pressaoAgua")
    private Double pressaoAgua;

    @SerializedName("comprimentoMangueira")
    private Double comprimentoMangueira;

    @SerializedName("disponivel")
    private Boolean disponivel;

    // ----------------------------------------------------------------
    // Getters e Setters
    // ----------------------------------------------------------------

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getDataInstalacao() { return dataInstalacao; }
    public void setDataInstalacao(String dataInstalacao) { this.dataInstalacao = dataInstalacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTipoEquipamento() { return tipoEquipamento; }
    public void setTipoEquipamento(String tipoEquipamento) { this.tipoEquipamento = tipoEquipamento; }

    public String getClasseFogo() { return classeFogo; }
    public void setClasseFogo(String classeFogo) { this.classeFogo = classeFogo; }

    public Double getCapacidade() { return capacidade; }
    public void setCapacidade(Double capacidade) { this.capacidade = capacidade; }

    public String getDataValidade() { return dataValidade; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }

    public Double getPressao() { return pressao; }
    public void setPressao(Double pressao) { this.pressao = pressao; }

    public String getTipoSensor() { return tipoSensor; }
    public void setTipoSensor(String tipoSensor) { this.tipoSensor = tipoSensor; }

    public String getUltimaVerificacao() { return ultimaVerificacao; }
    public void setUltimaVerificacao(String ultimaVerificacao) { this.ultimaVerificacao = ultimaVerificacao; }

    public Boolean getFuncionando() { return funcionando; }
    public void setFuncionando(Boolean funcionando) { this.funcionando = funcionando; }

    public Double getPressaoAgua() { return pressaoAgua; }
    public void setPressaoAgua(Double pressaoAgua) { this.pressaoAgua = pressaoAgua; }

    public Double getComprimentoMangueira() { return comprimentoMangueira; }
    public void setComprimentoMangueira(Double comprimentoMangueira) { this.comprimentoMangueira = comprimentoMangueira; }

    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }
}
