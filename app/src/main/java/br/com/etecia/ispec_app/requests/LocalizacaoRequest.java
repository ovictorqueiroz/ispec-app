package br.com.etecia.ispec_app.requests;

public class LocalizacaoRequest {

    private ClienteWrapper cliente;
    private String bloco;
    private String andar;
    private String sala;
    private String descricao;

    public LocalizacaoRequest(Long clienteId, String bloco, String andar, String sala, String descricao) {
        this.cliente = new ClienteWrapper(clienteId);
        this.bloco = bloco;
        this.andar = andar;
        this.sala = sala;
        this.descricao = descricao;
    }

    public ClienteWrapper getCliente() { return cliente; }
    public String getBloco() { return bloco; }
    public String getAndar() { return andar; }
    public String getSala() { return sala; }
    public String getDescricao() { return descricao; }

    public static class ClienteWrapper {
        private Long id;
        public ClienteWrapper(Long id) { this.id = id; }
        public Long getId() { return id; }
    }
}
