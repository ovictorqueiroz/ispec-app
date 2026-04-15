package br.com.etecia.ispec_app;

public class ExtintorModel extends EquipamentoModel {

    @SerializedName("tipo")
    private final String tipo = "extintor"; // obrigatório para o @JsonTypeInfo do backend

    private String classeFogo;
    private double capacidade;
    private String dataValidade;  // LocalDate no backend → String no Android
    private int pressao;

    public ExtintorModel() {}

    // Getters e Setters
    public String getTipo() { return tipo; }

    public String getClasseFogo() { return classeFogo; }
    public void setClasseFogo(String classeFogo) { this.classeFogo = classeFogo; }

    public double getCapacidade() { return capacidade; }
    public void setCapacidade(double capacidade) { this.capacidade = capacidade; }

    public String getDataValidade() { return dataValidade; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }

    public int getPressao() { return pressao; }
    public void setPressao(int pressao) { this.pressao = pressao; }
}