package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName;

public class UsuarioModel {

    private Long id;

    @SerializedName("nome")
    private String nome;

    public UsuarioModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome != null ? nome : ""; }
    public void setNome(String nome) { this.nome = nome; }

    // Usado pelo Spinner para exibir o nome do usuário
    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
