package br.com.etecia.ispec_app;

public class LocalizacaoModel {

    private Long id;
    private String nome;
    private String bloco;
    private String andar;
    private String sala;
    private String descricao;

    public LocalizacaoModel() {}

    public LocalizacaoModel(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getBloco() { return bloco; }
    public void setBloco(String bloco) { this.bloco = bloco; }

    public String getAndar() { return andar; }
    public void setAndar(String andar) { this.andar = andar; }

    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
