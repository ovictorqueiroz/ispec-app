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
import br.com.etecia.ispec_app.model.EquipamentoModel;

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

        String localizacao = equipamento.getLocalizacao().getBloco() + " - " +
                equipamento.getLocalizacao().getAndar() + " - " +
                equipamento.getLocalizacao().getSala();
        holder.tvLocalizacao.setText(localizacao);
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

        public EquipamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeEquipamento = itemView.findViewById(R.id.tvNomeEquipamento);
            tvLocalizacao = itemView.findViewById(R.id.tvLocalizacao);
        }
    }
}