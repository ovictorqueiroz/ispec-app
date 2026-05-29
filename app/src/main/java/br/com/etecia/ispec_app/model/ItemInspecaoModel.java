package br.com.etecia.ispec_app.model;

public class ItemInspecaoModel {

    private Long id;
    private PerguntaInspecaoModel pergunta;
    private boolean resposta;

    public ItemInspecaoModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PerguntaInspecaoModel getPergunta() { return pergunta; }
    public void setPergunta(PerguntaInspecaoModel pergunta) { this.pergunta = pergunta; }

    public boolean isResposta() { return resposta; }
    public void setResposta(boolean resposta) { this.resposta = resposta; }
}
