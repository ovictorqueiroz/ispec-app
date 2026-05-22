package br.com.etecia.ispec_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.etecia.ispec_app.R;
import br.com.etecia.ispec_app.model.AgendamentoModel;

/**
 * Adapter que exibe agendamentos agrupados por data.
 * Cada grupo tem um header com "DD Mês" e abaixo os cards do dia.
 *
 * Tipos de ViewHolder:
 *  - TYPE_HEADER: exibe a data (ex: "12 Abril")
 *  - TYPE_ITEM:   exibe o card do agendamento
 */
public class AgendamentoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM   = 1;

    // Cada entrada da lista pode ser um header (String) ou um item (AgendamentoModel)
    private final List<Object> listaComHeaders = new ArrayList<>();

    private static final String[] MESES = {
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    public void atualizarLista(List<AgendamentoModel> agendamentos) {
        listaComHeaders.clear();

        if (agendamentos == null || agendamentos.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        // Agrupa por data (os agendamentos já vêm ordenados pelo back via findByDataBetween)
        String dataAtual = null;
        for (AgendamentoModel ag : agendamentos) {
            String data = ag.getData(); // "YYYY-MM-DD"
            if (!data.equals(dataAtual)) {
                listaComHeaders.add(formatarDataHeader(data)); // adiciona header
                dataAtual = data;
            }
            listaComHeaders.add(ag); // adiciona item
        }

        notifyDataSetChanged();
    }

    /**
     * Converte "2024-04-12" → "12 Abril"
     */
    private String formatarDataHeader(String data) {
        try {
            String[] partes = data.split("-");
            int mes = Integer.parseInt(partes[1]);
            int dia = Integer.parseInt(partes[2]);
            String nomeMes = (mes >= 1 && mes <= 12) ? MESES[mes - 1] : "";
            return dia + " " + nomeMes;
        } catch (Exception e) {
            return data;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (listaComHeaders.get(position) instanceof String) ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_agenda_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.card_agendamento, parent, false);
            return new AgendamentoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((String) listaComHeaders.get(position));
        } else if (holder instanceof AgendamentoViewHolder) {
            ((AgendamentoViewHolder) holder).bind((AgendamentoModel) listaComHeaders.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listaComHeaders.size();
    }

    // --- ViewHolders ---

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvData;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tvDataHeader);
        }

        void bind(String dataFormatada) {
            tvData.setText(dataFormatada);
        }
    }

    static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTituloCard;
        private final TextView tvEnderecoCard;

        AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloCard   = itemView.findViewById(R.id.tvTituloCard);
            tvEnderecoCard = itemView.findViewById(R.id.tvEnderecoCard);
        }

        void bind(AgendamentoModel agendamento) {
            // Formato: "Inspeção | Clinica Vida Saudável"
            String tipoLabel = agendamento.getTipoLabel();
            String titulo    = agendamento.getTitulo();
            tvTituloCard.setText(tipoLabel + " | " + titulo);

            // Descrição como endereço/detalhe abaixo
            String descricao = agendamento.getDescricao();
            if (descricao != null && !descricao.isEmpty()) {
                tvEnderecoCard.setText(descricao);
                tvEnderecoCard.setVisibility(View.VISIBLE);
            } else {
                tvEnderecoCard.setVisibility(View.GONE);
            }
        }
    }
}
