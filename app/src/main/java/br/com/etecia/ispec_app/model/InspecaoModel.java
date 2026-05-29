package br.com.etecia.ispec_app.model;

import java.util.List;

public class InspecaoModel {

    private Long id;
    private EquipamentoModel equipamento;
    private UsuarioModel responsavel;
    private String dataInspecao; // formato "YYYY-MM-DD"
    private Boolean aprovado;
    private String observacoes;
    private List<ItemInspecaoModel> itens;

    public InspecaoModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EquipamentoModel getEquipamento() { return equipamento; }
    public void setEquipamento(EquipamentoModel equipamento) { this.equipamento = equipamento; }

    public UsuarioModel getResponsavel() { return responsavel; }
    public void setResponsavel(UsuarioModel responsavel) { this.responsavel = responsavel; }

    public String getDataInspecao() { return dataInspecao; }
    public void setDataInspecao(String dataInspecao) { this.dataInspecao = dataInspecao; }

    public Boolean getAprovado() { return aprovado; }
    public void setAprovado(Boolean aprovado) { this.aprovado = aprovado; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<ItemInspecaoModel> getItens() { return itens; }
    public void setItens(List<ItemInspecaoModel> itens) { this.itens = itens; }
}
