package br.com.etecia.ispec_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.etecia.ispec_app.R;
import br.com.etecia.ispec_app.model.AlarmeModel;
import br.com.etecia.ispec_app.model.EquipamentoModel;
import br.com.etecia.ispec_app.model.ExtintorModel;
import br.com.etecia.ispec_app.model.HidranteModel;
import br.com.etecia.ispec_app.model.LocalizacaoModel;

public class EquipamentoAdapter extends RecyclerView.Adapter<EquipamentoAdapter.EquipamentoViewHolder> {
    private List<EquipamentoModel> listaEquipamentos;

    public EquipamentoAdapter(List<EquipamentoModel> listaEquipamentos) {
        this.listaEquipamentos = new ArrayList<>(listaEquipamentos);
    }

    @NonNull
    @Override
    public EquipamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_equipamento, parent, false);
        return new EquipamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipamentoViewHolder holder, int position) {
        EquipamentoModel equipamento = listaEquipamentos.get(position);

        holder.tvNomeEquipamento.setText(equipamento.getNome());

        // Ícone dinâmico por tipo de equipamento
        if (equipamento instanceof ExtintorModel) {
            holder.ivIcone.setImageResource(R.drawable.ic_fire_extinguisher_duotone);
        } else if (equipamento instanceof AlarmeModel) {
            holder.ivIcone.setImageResource(R.drawable.ic_siren_duotone);
        } else if (equipamento instanceof HidranteModel) {
            holder.ivIcone.setImageResource(R.drawable.ic_fire_hydrant);
        } else {
            holder.ivIcone.setImageResource(R.drawable.ic_fire_extinguisher_duotone);
        }

        // Null-check na localização para evitar NullPointerException
        LocalizacaoModel loc = equipamento.getLocalizacao();
        if (loc != null) {
            String bloco = loc.getBloco() != null ? loc.getBloco() : "-";
            String andar = loc.getAndar() != null ? loc.getAndar() : "-";
            String sala  = loc.getSala()  != null ? loc.getSala()  : "-";
            holder.tvLocalizacao.setText(bloco + " - " + andar + " - " + sala);
        } else {
            holder.tvLocalizacao.setText("Localização não informada");
        }
    }

    @Override
    public int getItemCount() {
        return listaEquipamentos.size();
    }

    public void atualizarLista(List<EquipamentoModel> novaLista) {
        this.listaEquipamentos = new ArrayList<>(novaLista);
        notifyDataSetChanged();
    }

    public static class EquipamentoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomeEquipamento, tvLocalizacao;
        private final ImageView ivIcone;

        public EquipamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeEquipamento = itemView.findViewById(R.id.tvNomeEquipamento);
            tvLocalizacao = itemView.findViewById(R.id.tvLocalizacao);
            ivIcone = itemView.findViewById(R.id.ivIconeEquipamento);
        }
    }
}
