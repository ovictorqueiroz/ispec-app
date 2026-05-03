package br.com.etecia.ispec_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.etecia.ispec_app.R;
import br.com.etecia.ispec_app.model.ClienteModel;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {
    private List<ClienteModel> listaClientes;

    public ClienteAdapter(List<ClienteModel> listaClientes){
        this.listaClientes = listaClientes;
    }
    @NonNull
    @Override
    public ClienteAdapter.ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ClienteViewHolder holder, int position) {
        ClienteModel cliente = listaClientes.get(position);
        holder.tvNomeEmpresa.setText(cliente.getRazaoSocial());
        holder.tvEndereco.setText(cliente.getEndereco());
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public  static  class ClienteViewHolder extends RecyclerView.ViewHolder{
        private  final TextView tvNomeEmpresa, tvEndereco;

        public  ClienteViewHolder(@NonNull View itemView){
            super(itemView);
            tvNomeEmpresa =itemView.findViewById(R.id.tvNomeEmpresa);
            tvEndereco =itemView.findViewById(R.id.tvEndereco);
        }
    }
}