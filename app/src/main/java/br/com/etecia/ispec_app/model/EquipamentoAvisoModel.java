package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model flat para equipamentos retornados pelo /avisos.
 *
 * O backend serializa Equipamento com polimorfismo (@JsonTypeInfo),
 * mas para a tela de Avisos só precisamos de:
 *   - numSerie / nome
 *   - cliente.razaoSocial
 *   - dataValidade
 *
 * Usar um model dedicado evita acoplamento com o TypeAdapter de Equipamento
 * e mantém a solução simples (MUST DO nº 3).
 */
public class EquipamentoAvisoModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("numSerie")
    private String numSerie;

    @SerializedName("nome")
    private String nome;

    // dataValidade existe no backend em Equipamento (dt_validade).
    // O ExtintorModel do Android já a declara; aqui repetimos no modelo flat.
    @SerializedName("dataValidade")
    private String dataValidade;

    // O backend embute o objeto Cliente dentro de Equipamento.
    // Precisamos apenas de razaoSocial → ClienteRef interno.
    @SerializedName("cliente")
    private ClienteRef cliente;

    public EquipamentoAvisoModel() {}

    // --- Getters ---

    public Long getId()             { return id; }
    public String getNumSerie()     { return numSerie != null ? numSerie : ""; }
    public String getNome()         { return nome != null ? nome : ""; }
    public String getDataValidade() { return dataValidade != null ? dataValidade : ""; }

    public String getNomeCliente() {
        return (cliente != null && cliente.getRazaoSocial() != null)
                ? cliente.getRazaoSocial()
                : "Cliente desconhecido";
    }

    // --- Classe interna para o cliente embutido ---

    public static class ClienteRef {
        @SerializedName("razaoSocial")
        private String razaoSocial;

        public String getRazaoSocial() { return razaoSocial; }
    }
}
