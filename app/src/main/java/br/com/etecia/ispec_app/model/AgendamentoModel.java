package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName;

public class AgendamentoModel {

    private Long id;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("data")
    private String data; // "YYYY-MM-DD"

    @SerializedName("tipo")
    private String tipo; // "INSPECAO" | "MANUTENCAO" | "VISITA_TECNICA"

    @SerializedName("status")
    private String status; // "PENDENTE" | "REALIZADO" | "CANCELADO"

    @SerializedName("responsavel")
    private ResponsavelModel responsavel;

    public AgendamentoModel() {}

    // --- Getters ---

    public Long getId() { return id; }

    public String getTitulo() { return titulo != null ? titulo : ""; }

    public String getDescricao() { return descricao != null ? descricao : ""; }

    public String getData() { return data != null ? data : ""; }

    public String getTipo() { return tipo != null ? tipo : ""; }

    public String getStatus() { return status != null ? status : ""; }

    public ResponsavelModel getResponsavel() { return responsavel; }

    // --- Setters ---

    public void setId(Long id) { this.id = id; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public void setData(String data) { this.data = data; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public void setStatus(String status) { this.status = status; }

    public void setResponsavel(ResponsavelModel responsavel) { this.responsavel = responsavel; }

    /**
     * Retorna o label de exibição do tipo de visita.
     * Ex: "INSPECAO" → "Inspeção"
     */
    public String getTipoLabel() {
        if (tipo == null) return "";
        switch (tipo) {
            case "INSPECAO":      return "Inspeção";
            case "MANUTENCAO":    return "Manutenção";
            case "VISITA_TECNICA": return "Visita Técnica";
            default:              return tipo;
        }
    }

    // Modelo aninhado para o responsável
    public static class ResponsavelModel {
        private Long id;

        @SerializedName("nome")
        private String nome;

        public ResponsavelModel() {}

        public ResponsavelModel(Long id) { this.id = id; }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNome() { return nome != null ? nome : ""; }
        public void setNome(String nome) { this.nome = nome; }
    }
}
