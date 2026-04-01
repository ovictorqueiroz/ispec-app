package br.com.etecia.ispec_app;

public abstract class EquipamentoModel {

    private Long id;
    private String nome;
    private String status;      // Enum no backend → String no Android
    private boolean ativo;
    private String dataInstalacao;  // LocalDate no backend → String no Android (formato: "2024-01-15")
    private LocalizacaoModel localizacao;

    public EquipamentoModel() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getDataInstalacao() { return dataInstalacao; }
    public void setDataInstalacao(String dataInstalacao) { this.dataInstalacao = dataInstalacao; }

    public LocalizacaoModel getLocalizacao() { return localizacao; }
    public void setLocalizacao(LocalizacaoModel localizacao) { this.localizacao = localizacao; }
}
