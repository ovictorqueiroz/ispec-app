package br.com.etecia.ispec_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.etecia.ispec_app.R;
import br.com.etecia.ispec_app.model.AgendamentoModel;
import br.com.etecia.ispec_app.model.AvisosResponse;
import br.com.etecia.ispec_app.model.EquipamentoAvisoModel;
import br.com.etecia.ispec_app.model.InspecaoAvisoModel;

/**
 * Adapter para a tela de Avisos.
 *
 * Usa múltiplos ViewTypes para renderizar numa única RecyclerView:
 *
 *   VIEW_TYPE_HEADER     → cabeçalho de seção  (ex: "Vencidos · 12")
 *   VIEW_TYPE_EQUIPAMENTO → card de equipamento (vencidos / vencendo30 / vencendo90)
 *   VIEW_TYPE_INSPECAO    → card de inspeção reprovada
 *   VIEW_TYPE_AGENDAMENTO → card de agendamento da semana
 *   VIEW_TYPE_EMPTY       → texto "Nenhum item" quando a lista está vazia
 *
 * A lista interna é uma List<Object> heterogênea.
 * Cada item é prefixado com um objeto HeaderItem para marcar seções.
 */
public class AvisosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // --- Constantes de ViewType ---
    private static final int VIEW_TYPE_HEADER      = 0;
    private static final int VIEW_TYPE_EQUIPAMENTO = 1;
    private static final int VIEW_TYPE_INSPECAO    = 2;
    private static final int VIEW_TYPE_AGENDAMENTO = 3;
    private static final int VIEW_TYPE_EMPTY       = 4;

    // Lista heterogênea: HeaderItem | EquipamentoAvisoModel | InspecaoAvisoModel | AgendamentoModel | EmptyItem
    private final List<Object> itens = new ArrayList<>();

    // --- Modelos internos para header e empty ---

    public static class HeaderItem {
        public final String titulo;
        public final int    cor;       // cor do KPI (vermelho, amarelo, laranja, cinza)
        public final String kpiLabel;  // ex: "Vencidos: 12"

        public HeaderItem(String titulo, int cor, String kpiLabel) {
            this.titulo   = titulo;
            this.cor      = cor;
            this.kpiLabel = kpiLabel;
        }
    }

    public static class EmptyItem {
        public final String mensagem;
        public EmptyItem(String mensagem) { this.mensagem = mensagem; }
    }

    // --- Atualização de dados ---

    public void atualizarDados(AvisosResponse response) {
        itens.clear();

        // Bloco 1 — Vencidos (vermelho)
        List<EquipamentoAvisoModel> vencidos = response.getVencidos();
        itens.add(new HeaderItem(
                "Equipamentos Vencidos",
                R.color.vermelhoClaro,
                "Vencidos: " + (vencidos != null ? vencidos.size() : 0)
        ));
        if (vencidos != null && !vencidos.isEmpty()) {
            itens.addAll(vencidos);
        } else {
            itens.add(new EmptyItem("Nenhum item"));
        }

        // Bloco 2 — Vencendo em 30 dias (amarelo → usamos cor inline)
        List<EquipamentoAvisoModel> vencendo30 = response.getVencendo30();
        itens.add(new HeaderItem(
                "Vencem em até 30 dias",
                R.color.amarelo,
                "Vencem em 30 dias: " + (vencendo30 != null ? vencendo30.size() : 0)
        ));
        if (vencendo30 != null && !vencendo30.isEmpty()) {
            itens.addAll(vencendo30);
        } else {
            itens.add(new EmptyItem("Nenhum item"));
        }

        // Bloco 3 — Vencendo entre 31 e 90 dias (laranja)
        List<EquipamentoAvisoModel> vencendo90 = response.getVencendo90();
        itens.add(new HeaderItem(
                "Vencem entre 31 e 90 dias",
                R.color.laranja,
                "Vencem em 90 dias: " + (vencendo90 != null ? vencendo90.size() : 0)
        ));
        if (vencendo90 != null && !vencendo90.isEmpty()) {
            itens.addAll(vencendo90);
        } else {
            itens.add(new EmptyItem("Nenhum item"));
        }

        // Bloco 4 — Inspeções reprovadas (vermelho escuro)
        List<InspecaoAvisoModel> reprovadas = response.getReprovadas();
        itens.add(new HeaderItem(
                "Inspeções Reprovadas (30 dias)",
                R.color.vermelhoEscuro,
                null   // reprovadas não têm KPI numérico próprio no READMEFIRST
        ));
        if (reprovadas != null && !reprovadas.isEmpty()) {
            itens.addAll(reprovadas);
        } else {
            itens.add(new EmptyItem("Nenhuma inspeção reprovada recentemente"));
        }

        // Bloco 5 — Agenda da semana (cinza escuro)
        List<AgendamentoModel> agenda = response.getAgendaSemana();
        itens.add(new HeaderItem(
                "Agenda desta semana",
                R.color.cinzaEscuro,
                "Agenda esta semana: " + (agenda != null ? agenda.size() : 0)
        ));
        if (agenda != null && !agenda.isEmpty()) {
            itens.addAll(agenda);
        } else {
            itens.add(new EmptyItem("Nenhum agendamento esta semana"));
        }

        notifyDataSetChanged();
    }

    // --- RecyclerView obrigatórios ---

    @Override
    public int getItemViewType(int position) {
        Object item = itens.get(position);
        if (item instanceof HeaderItem)            return VIEW_TYPE_HEADER;
        if (item instanceof EquipamentoAvisoModel) return VIEW_TYPE_EQUIPAMENTO;
        if (item instanceof InspecaoAvisoModel)    return VIEW_TYPE_INSPECAO;
        if (item instanceof AgendamentoModel)      return VIEW_TYPE_AGENDAMENTO;
        return VIEW_TYPE_EMPTY; // EmptyItem
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(inf.inflate(R.layout.item_aviso_header, parent, false));
            case VIEW_TYPE_EQUIPAMENTO:
                return new EquipamentoViewHolder(inf.inflate(R.layout.card_aviso_equipamento, parent, false));
            case VIEW_TYPE_INSPECAO:
                return new InspecaoViewHolder(inf.inflate(R.layout.card_aviso_inspecao, parent, false));
            case VIEW_TYPE_AGENDAMENTO:
                return new AgendamentoViewHolder(inf.inflate(R.layout.card_aviso_agendamento, parent, false));
            default:
                return new EmptyViewHolder(inf.inflate(R.layout.item_aviso_empty, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = itens.get(position);

        if (holder instanceof HeaderViewHolder) {
            HeaderItem header = (HeaderItem) item;
            HeaderViewHolder h = (HeaderViewHolder) holder;
            h.tvTitulo.setText(header.titulo);
            h.tvTitulo.setTextColor(ContextCompat.getColor(h.itemView.getContext(), header.cor));
            if (header.kpiLabel != null) {
                h.tvKpi.setVisibility(View.VISIBLE);
                h.tvKpi.setText(header.kpiLabel);
                h.tvKpi.setTextColor(ContextCompat.getColor(h.itemView.getContext(), header.cor));
            } else {
                h.tvKpi.setVisibility(View.GONE);
            }

        } else if (holder instanceof EquipamentoViewHolder) {
            EquipamentoAvisoModel eq = (EquipamentoAvisoModel) item;
            EquipamentoViewHolder h  = (EquipamentoViewHolder) holder;
            // "EXT-001 | Extintor de Pó"
            String codigo = eq.getNumSerie().isEmpty() ? "" : eq.getNumSerie() + " | ";
            h.tvCodNome.setText(codigo + eq.getNome());
            h.tvCliente.setText(eq.getNomeCliente());
            h.tvDataValidade.setText("Validade: " + formatarData(eq.getDataValidade()));

        } else if (holder instanceof InspecaoViewHolder) {
            InspecaoAvisoModel ins = (InspecaoAvisoModel) item;
            InspecaoViewHolder h   = (InspecaoViewHolder) holder;
            h.tvNomeEquipamento.setText(ins.getNomeEquipamento());
            h.tvCliente.setText(ins.getNomeCliente());
            h.tvResponsavel.setText("Responsável: " + ins.getNomeResponsavel());
            h.tvData.setText("Data: " + formatarData(ins.getDataInspecao()));
            h.tvStatus.setText("Reprovado");
            h.tvStatus.setTextColor(
                    ContextCompat.getColor(h.itemView.getContext(), R.color.vermelhoClaro));

        } else if (holder instanceof AgendamentoViewHolder) {
            AgendamentoModel ag = (AgendamentoModel) item;
            AgendamentoViewHolder h = (AgendamentoViewHolder) holder;
            h.tvTitulo.setText(ag.getTitulo());
            h.tvResponsavel.setText(
                    ag.getResponsavel() != null ? ag.getResponsavel().getNome() : "");
            h.tvTipo.setText(ag.getTipoLabel());
            h.tvData.setText(formatarData(ag.getData()));

        } else if (holder instanceof EmptyViewHolder) {
            EmptyItem empty = (EmptyItem) item;
            ((EmptyViewHolder) holder).tvMensagem.setText(empty.mensagem);
        }
    }

    @Override
    public int getItemCount() { return itens.size(); }

    // --- Formata "YYYY-MM-DD" → "DD/MM/YYYY" ---
    private String formatarData(String iso) {
        if (iso == null || iso.length() < 10) return iso != null ? iso : "-";
        // iso = "2026-01-15"
        String[] partes = iso.split("-");
        if (partes.length < 3) return iso;
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }

    // --- ViewHolders ---

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvKpi;
        HeaderViewHolder(View v) {
            super(v);
            tvTitulo = v.findViewById(R.id.tvAvisoHeaderTitulo);
            tvKpi    = v.findViewById(R.id.tvAvisoHeaderKpi);
        }
    }

    static class EquipamentoViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodNome, tvCliente, tvDataValidade;
        EquipamentoViewHolder(View v) {
            super(v);
            tvCodNome     = v.findViewById(R.id.tvAvisoEquipCodNome);
            tvCliente     = v.findViewById(R.id.tvAvisoEquipCliente);
            tvDataValidade = v.findViewById(R.id.tvAvisoEquipValidade);
        }
    }

    static class InspecaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeEquipamento, tvCliente, tvResponsavel, tvData, tvStatus;
        InspecaoViewHolder(View v) {
            super(v);
            tvNomeEquipamento = v.findViewById(R.id.tvAvisoInspEquipamento);
            tvCliente         = v.findViewById(R.id.tvAvisoInspCliente);
            tvResponsavel     = v.findViewById(R.id.tvAvisoInspResponsavel);
            tvData            = v.findViewById(R.id.tvAvisoInspData);
            tvStatus          = v.findViewById(R.id.tvAvisoInspStatus);
        }
    }

    static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvResponsavel, tvTipo, tvData;
        AgendamentoViewHolder(View v) {
            super(v);
            tvTitulo      = v.findViewById(R.id.tvAvisoAgendTitulo);
            tvResponsavel = v.findViewById(R.id.tvAvisoAgendResponsavel);
            tvTipo        = v.findViewById(R.id.tvAvisoAgendTipo);
            tvData        = v.findViewById(R.id.tvAvisoAgendData);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMensagem;
        EmptyViewHolder(View v) {
            super(v);
            tvMensagem = v.findViewById(R.id.tvAvisoEmptyMensagem);
        }
    }
}
