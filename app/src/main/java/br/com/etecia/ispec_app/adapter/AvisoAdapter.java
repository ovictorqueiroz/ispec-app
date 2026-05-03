package br.com.etecia.ispec_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.etecia.ispec_app.R;
import br.com.etecia.ispec_app.model.AvisoModel;

public class AvisoAdapter extends RecyclerView.Adapter<AvisoAdapter.ViewHolder> {

    List<AvisoModel> lista;

    ImageView icon;


    public AvisoAdapter(List<AvisoModel> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aviso_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AvisoModel aviso = lista.get(position);
        holder.titulo.setText(lista.get(position).getNome());
        String texto = lista.get(position).getNome();
        holder.tvNot.setText(aviso.getDescricao());

        if (texto.contains("Extintor")) {
            holder.icon.setImageResource(R.drawable.ic_a_bell_duotone);
        } else if (texto.contains("Inspeção")) {
            holder.icon.setImageResource(R.drawable.ic_c_calendar_dots_duotone);
        } else if (texto.contains("Mangueira")) {
            holder.icon.setImageResource(R.drawable.ic_a_bell_duotone);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, tvNot;
        ImageView icon;
        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvNomeAviso);
            tvNot = itemView.findViewById(R.id.tvNot);
            icon = itemView.findViewById(R.id.iconAviso);
        }
    }
}