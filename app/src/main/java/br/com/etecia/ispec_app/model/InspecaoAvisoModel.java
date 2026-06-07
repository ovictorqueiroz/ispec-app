package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model flat para inspeções reprovadas retornadas pelo /avisos.
 *
 * Campos exibidos conforme READMEFIRST (Bloco 4):
 *   - Nome do equipamento
 *   - Cliente
 *   - Responsável pela inspeção
 *   - Data da inspeção
 *   - Status: Reprovado
 */
public class InspecaoAvisoModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("dataInspecao")
    private String dataInspecao;

    @SerializedName("aprovado")
    private Boolean aprovado;

    // O backend embute o Equipamento com polimorfismo.
    // Precisamos apenas de nome e cliente.razaoSocial.
    @SerializedName("equipamento")
    private EquipamentoRef equipamento;

    // O backend embute o responsável como Usuario.
    @SerializedName("responsavel")
    private ResponsavelRef responsavel;

    public InspecaoAvisoModel() {}

    // --- Getters ---

    public Long getId()             { return id; }
    public String getDataInspecao() { return dataInspecao != null ? dataInspecao : ""; }
    public Boolean getAprovado()    { return aprovado; }

    public String getNomeEquipamento() {
        return (equipamento != null && equipamento.getNome() != null)
                ? equipamento.getNome()
                : "Equipamento desconhecido";
    }

    public String getNomeCliente() {
        if (equipamento == null || equipamento.getCliente() == null) return "Cliente desconhecido";
        String rs = equipamento.getCliente().getRazaoSocial();
        return rs != null ? rs : "Cliente desconhecido";
    }

    public String getNomeResponsavel() {
        return (responsavel != null && responsavel.getNome() != null)
                ? responsavel.getNome()
                : "Responsável desconhecido";
    }

    // --- Classes internas ---

    public static class EquipamentoRef {
        @SerializedName("nome")
        private String nome;

        @SerializedName("cliente")
        private ClienteRef cliente;

        public String getNome()      { return nome; }
        public ClienteRef getCliente() { return cliente; }
    }

    public static class ClienteRef {
        @SerializedName("razaoSocial")
        private String razaoSocial;

        public String getRazaoSocial() { return razaoSocial; }
    }

    public static class ResponsavelRef {
        @SerializedName("nome")
        private String nome;

        public String getNome() { return nome; }
    }
}
