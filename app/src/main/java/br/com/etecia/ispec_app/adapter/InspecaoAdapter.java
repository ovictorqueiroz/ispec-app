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
import br.com.etecia.ispec_app.model.InspecaoModel;

public class InspecaoAdapter extends RecyclerView.Adapter<InspecaoAdapter.InspecaoViewHolder> {

    private List<InspecaoModel> listaCompleta = new ArrayList<>();
    private List<InspecaoModel> listaFiltrada = new ArrayList<>();

    public InspecaoAdapter(List<InspecaoModel> lista) {
        this.listaCompleta = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public InspecaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inspecao, parent, false);
        return new InspecaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InspecaoViewHolder holder, int position) {
        InspecaoModel inspecao = listaFiltrada.get(position);

        // Nome do equipamento
        String nomeEquipamento = (inspecao.getEquipamento() != null && inspecao.getEquipamento().getNome() != null)
                ? inspecao.getEquipamento().getNome()
                : "Equipamento desconhecido";
        holder.tvEquipamento.setText(nomeEquipamento);

        // Data
        String data = inspecao.getDataInspecao() != null ? inspecao.getDataInspecao() : "-";
        holder.tvData.setText(data);

        // Responsável
        String responsavel = (inspecao.getResponsavel() != null && inspecao.getResponsavel().getNome() != null)
                ? inspecao.getResponsavel().getNome()
                : "Responsável desconhecido";
        holder.tvResponsavel.setText(responsavel);

        // Status aprovado/reprovado
        if (inspecao.getAprovado() != null) {
            if (inspecao.getAprovado()) {
                holder.tvStatus.setText("Aprovada");
                holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.verdeSalvar));
            } else {
                holder.tvStatus.setText("Reprovada");
                holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.vermelhoClaro));
            }
        } else {
            holder.tvStatus.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void atualizarLista(List<InspecaoModel> novaLista) {
        this.listaCompleta = new ArrayList<>(novaLista);
        this.listaFiltrada = new ArrayList<>(novaLista);
        notifyDataSetChanged();
    }

    public void filtrar(String query) {
        listaFiltrada.clear();
        if (query == null || query.trim().isEmpty()) {
            listaFiltrada.addAll(listaCompleta);
        } else {
            String lower = query.toLowerCase().trim();
            for (InspecaoModel i : listaCompleta) {
                boolean matchEquipamento = i.getEquipamento() != null
                        && i.getEquipamento().getNome() != null
                        && i.getEquipamento().getNome().toLowerCase().contains(lower);
                boolean matchData = i.getDataInspecao() != null
                        && i.getDataInspecao().contains(lower);
                boolean matchResponsavel = i.getResponsavel() != null
                        && i.getResponsavel().getNome() != null
                        && i.getResponsavel().getNome().toLowerCase().contains(lower);
                if (matchEquipamento || matchData || matchResponsavel) {
                    listaFiltrada.add(i);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class InspecaoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvEquipamento, tvData, tvResponsavel, tvStatus;

        public InspecaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEquipamento = itemView.findViewById(R.id.tvEquipamentoInspecao);
            tvData = itemView.findViewById(R.id.tvDataInspecao);
            tvResponsavel = itemView.findViewById(R.id.tvResponsavelInspecao);
            tvStatus = itemView.findViewById(R.id.tvStatusInspecao);
        }
    }
}
