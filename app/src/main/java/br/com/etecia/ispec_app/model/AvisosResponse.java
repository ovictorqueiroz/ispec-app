package br.com.etecia.ispec_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Mapeia o JSON retornado pelo GET /avisos:
 * {
 *   "vencidos":     [ ...Equipamento... ],
 *   "vencendo30":   [ ...Equipamento... ],
 *   "vencendo90":   [ ...Equipamento... ],
 *   "reprovadas":   [ ...Inspecao...    ],
 *   "agendaSemana": [ ...Agendamento... ]
 * }
 *
 * NOTA: Equipamentos chegam como JsonObject porque são polimórficos
 * (Extintor / Alarme / Hidrante). Usamos EquipamentoAvisoModel — um
 * modelo flat dedicado a Avisos — para evitar depender do TypeAdapter
 * completo e manter a tela simples conforme o MUST DO.
 */
public class AvisosResponse {

    @SerializedName("vencidos")
    private List<EquipamentoAvisoModel> vencidos;

    @SerializedName("vencendo30")
    private List<EquipamentoAvisoModel> vencendo30;

    @SerializedName("vencendo90")
    private List<EquipamentoAvisoModel> vencendo90;

    @SerializedName("reprovadas")
    private List<InspecaoAvisoModel> reprovadas;

    @SerializedName("agendaSemana")
    private List<AgendamentoModel> agendaSemana;

    public AvisosResponse() {}

    public List<EquipamentoAvisoModel> getVencidos()          { return vencidos; }
    public List<EquipamentoAvisoModel> getVencendo30()        { return vencendo30; }
    public List<EquipamentoAvisoModel> getVencendo90()        { return vencendo90; }
    public List<InspecaoAvisoModel>    getReprovadas()        { return reprovadas; }
    public List<AgendamentoModel>      getAgendaSemana()      { return agendaSemana; }
}
