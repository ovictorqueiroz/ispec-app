package br.com.etecia.ispec_app;
public class AvisoModel {
    private Long id;
    private String nome, descricao;

    public AvisoModel(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
}