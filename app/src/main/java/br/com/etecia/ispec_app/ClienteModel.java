package br.com.etecia.ispec_app;

public class ClienteModel {
    private Long id;
    private String nomeEmpresa;
    private String endereco;

    public ClienteModel(Long id, String nomeEmpresa, String endereco) {
        this.id = id;
        this.nomeEmpresa = nomeEmpresa;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public String getEndereco() {
        return endereco;
    }
}
