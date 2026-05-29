package br.com.etecia.ispec_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.etecia.ispec_app.R;
import br.com.etecia.ispec_app.model.PerguntaInspecaoModel;
import br.com.etecia.ispec_app.requests.InspecaoRequest;

public class PerguntaAdapter extends RecyclerView.Adapter<PerguntaAdapter.PerguntaViewHolder> {

    private List<PerguntaInspecaoModel> perguntas = new ArrayList<>();
    // Armazena a resposta de cada pergunta por ID. Default = true (Sim)
    private final Map<Long, Boolean> respostas = new HashMap<>();

    public PerguntaAdapter(List<PerguntaInspecaoModel> perguntas) {
        this.perguntas = new ArrayList<>(perguntas);
        for (PerguntaInspecaoModel p : perguntas) {
            respostas.put(p.getId(), true);
        }
    }

    @NonNull
    @Override
    public PerguntaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pergunta_inspecao, parent, false);
        return new PerguntaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerguntaViewHolder holder, int position) {
        PerguntaInspecaoModel pergunta = perguntas.get(position);
        holder.tvPergunta.setText(pergunta.getPergunta());

        // Define o estado do switch sem disparar o listener
        holder.swResposta.setOnCheckedChangeListener(null);
        Boolean resposta = respostas.get(pergunta.getId());
        holder.swResposta.setChecked(resposta != null ? resposta : true);

        // Atualiza o label e armazena a resposta ao mudar o switch
        atualizarLabelSwitch(holder.swResposta, holder.swResposta.isChecked());
        holder.swResposta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            respostas.put(pergunta.getId(), isChecked);
            atualizarLabelSwitch(buttonView, isChecked);
        });
    }

    private void atualizarLabelSwitch(android.widget.CompoundButton sw, boolean isChecked) {
        ((Switch) sw).setText(isChecked ? "Sim" : "Não");
    }

    @Override
    public int getItemCount() {
        return perguntas.size();
    }

    /** Constrói a lista de ItemDTO para enviar ao backend */
    public List<InspecaoRequest.ItemDTO> getItens() {
        List<InspecaoRequest.ItemDTO> itens = new ArrayList<>();
        for (PerguntaInspecaoModel p : perguntas) {
            Boolean resposta = respostas.get(p.getId());
            itens.add(new InspecaoRequest.ItemDTO(p.getId(), resposta != null ? resposta : true));
        }
        return itens;
    }

    public static class PerguntaViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPergunta;
        final Switch swResposta;

        public PerguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPergunta  = itemView.findViewById(R.id.tvPergunta);
            swResposta  = itemView.findViewById(R.id.swResposta);
        }
    }
}
