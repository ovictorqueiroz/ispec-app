package br.com.etecia.ispec_app.model;

public class PerguntaInspecaoModel {

    private Long id;
    private String pergunta;
    private String tipoEquipamento;
    private boolean ativo;

    public PerguntaInspecaoModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPergunta() { return pergunta; }
    public void setPergunta(String pergunta) { this.pergunta = pergunta; }

    public String getTipoEquipamento() { return tipoEquipamento; }
    public void setTipoEquipamento(String tipoEquipamento) { this.tipoEquipamento = tipoEquipamento; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
