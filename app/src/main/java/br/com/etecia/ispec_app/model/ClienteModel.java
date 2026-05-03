package br.com.etecia.ispec_app.model;
import com.google.gson.annotations.SerializedName;

public class ClienteModel {
    private Long id;
    @SerializedName("razaoSocial")
    private String razaoSocial;
    private String endereco;

    public ClienteModel(Long id, String razaoSocial, String endereco) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getEndereco() {
        return endereco;
    }
}
