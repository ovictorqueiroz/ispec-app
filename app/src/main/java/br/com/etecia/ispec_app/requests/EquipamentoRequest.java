package br.com.etecia.ispec_app.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.etecia.ispec_app.model.TipoSensorModel;

public class EquipamentoRequest {

    // Campos comuns
    @SerializedName("cliente")
    private IdWrapper cliente;

    @SerializedName("nome")
    private String nome;

    @SerializedName("localizacao")
    private IdWrapper localizacao;

    @SerializedName("dataInstalacao")
    private String dataInstalacao;   // formato: dd/MM/yyyy

    @SerializedName("status")
    private String status;           // "Ativo" ou "Inativo"

    @SerializedName("tipoEquipamento")
    private IdWrapper tipoEquipamento;  // "Alarme", "Extintor" ou "Hidrante"

    // Campos Extintor
    @SerializedName("classeFogo")
    private List<IdWrapper> classeFogo;

    @SerializedName("agente")
    private IdWrapper agente;

    @SerializedName("capacidade")
    private Double capacidade;

    @SerializedName("dataValidade")
    private String dataValidade;     // formato: dd/MM/yyyy

    @SerializedName("pressao")
    private Double pressao;

    // Campos Alarme
    @SerializedName("tipoSensor")
    private TipoSensorModel tipoSensor;

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

    @SerializedName("tipo")
    private String tipo;

    // ----------------------------------------------------------------
    // Getters e Setters
    // ----------------------------------------------------------------

    public IdWrapper getCliente() { return cliente; }
    public void setCliente(IdWrapper cliente) { this.cliente = cliente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public IdWrapper getLocalizacao() { return localizacao; }
    public void setLocalizacao(IdWrapper localizacao) { this.localizacao = localizacao; }

    public String getDataInstalacao() { return dataInstalacao; }
    public void setDataInstalacao(String dataInstalacao) { this.dataInstalacao = dataInstalacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public IdWrapper getTipoEquipamento() { return tipoEquipamento; }
    public void setTipoEquipamento(IdWrapper tipoEquipamento) { this.tipoEquipamento = tipoEquipamento; }

    public List<IdWrapper> getClasseFogo() { return classeFogo; }
    public void setClasseFogo(List<IdWrapper> classeFogo) { this.classeFogo = classeFogo; }

    public Double getCapacidade() { return capacidade; }
    public void setCapacidade(Double capacidade) { this.capacidade = capacidade; }

    public String getDataValidade() { return dataValidade; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }

    public Double getPressao() { return pressao; }
    public void setPressao(Double pressao) { this.pressao = pressao; }

    public TipoSensorModel getTipoSensor() { return tipoSensor; }
    public void setTipoSensor(TipoSensorModel tipoSensor) { this.tipoSensor = tipoSensor; }

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public IdWrapper getAgente() {
        return agente;
    }

    public void setAgente(IdWrapper agente) {
        this.agente = agente;
    }

    public static class IdWrapper {
        @SerializedName("id")
        private long id;

        public IdWrapper(long id) { this.id = id; }
        public long getId() { return id; }
        public void setId(long id) { this.id = id; }
    }
}
