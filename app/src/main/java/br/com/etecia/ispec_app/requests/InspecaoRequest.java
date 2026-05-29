package br.com.etecia.ispec_app.requests;

import java.util.List;

public class InspecaoRequest {

    private Long equipamentoId;
    private Long responsavelId; // null → backend resolve pelo JWT
    private String dataInspecao; // "YYYY-MM-DD"
    private String observacoes;
    private List<ItemDTO> itens;

    public InspecaoRequest() {}

    public Long getEquipamentoId() { return equipamentoId; }
    public void setEquipamentoId(Long equipamentoId) { this.equipamentoId = equipamentoId; }

    public Long getResponsavelId() { return responsavelId; }
    public void setResponsavelId(Long responsavelId) { this.responsavelId = responsavelId; }

    public String getDataInspecao() { return dataInspecao; }
    public void setDataInspecao(String dataInspecao) { this.dataInspecao = dataInspecao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<ItemDTO> getItens() { return itens; }
    public void setItens(List<ItemDTO> itens) { this.itens = itens; }

    public static class ItemDTO {
        private Long perguntaId;
        private boolean resposta;

        public ItemDTO(Long perguntaId, boolean resposta) {
            this.perguntaId = perguntaId;
            this.resposta = resposta;
        }

        public Long getPerguntaId() { return perguntaId; }
        public void setPerguntaId(Long perguntaId) { this.perguntaId = perguntaId; }

        public boolean isResposta() { return resposta; }
        public void setResposta(boolean resposta) { this.resposta = resposta; }
    }
}
