package br.com.etecia.ispec_app.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    // null = nenhum botão selecionado ainda; true = Sim; false = Não
    private final Map<Long, Boolean> respostas = new HashMap<>();

    public PerguntaAdapter(List<PerguntaInspecaoModel> perguntas) {
        this.perguntas = new ArrayList<>(perguntas);
    }

    @NonNull
    @Override
    public PerguntaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pergunta_inspecao, parent, false);
        return new PerguntaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerguntaViewHolder holder, int position) {
        PerguntaInspecaoModel pergunta = perguntas.get(position);
        holder.tvPergunta.setText(pergunta.getPergunta());

        // Reflete o estado visual atual
        Boolean resposta = respostas.get(pergunta.getId());
        atualizarEstadoBotoes(holder, resposta);

        holder.btnSim.setOnClickListener(v -> {
            respostas.put(pergunta.getId(), true);
            atualizarEstadoBotoes(holder, true);
        });

        holder.btnNao.setOnClickListener(v -> {
            respostas.put(pergunta.getId(), false);
            atualizarEstadoBotoes(holder, false);
        });
    }

    /**
     * null  → ambos cinza (nenhum selecionado)
     * true  → Sim verde, Não cinza
     * false → Sim cinza, Não vermelho
     */
    private void atualizarEstadoBotoes(PerguntaViewHolder holder, Boolean resposta) {
        if (resposta == null) {
            holder.btnSim.setBackgroundResource(R.drawable.bg_button_inativo);
            holder.btnNao.setBackgroundResource(R.drawable.bg_button_inativo);
        } else if (resposta) {
            holder.btnSim.setBackgroundResource(R.drawable.bg_button_green);
            holder.btnNao.setBackgroundResource(R.drawable.bg_button_inativo);
        } else {
            holder.btnSim.setBackgroundResource(R.drawable.bg_button_inativo);
            holder.btnNao.setBackgroundResource(R.drawable.bg_button_nao);
        }
    }

    @Override
    public int getItemCount() {
        return perguntas.size();
    }

    /**
     * Retorna os itens para o request.
     * Lança IllegalStateException se alguma pergunta ainda não foi respondida.
     */
    public List<InspecaoRequest.ItemDTO> getItens() {
        List<InspecaoRequest.ItemDTO> itens = new ArrayList<>();
        for (PerguntaInspecaoModel p : perguntas) {
            Boolean resposta = respostas.get(p.getId());
            if (resposta == null) {
                throw new IllegalStateException("Responda todas as perguntas antes de finalizar.");
            }
            itens.add(new InspecaoRequest.ItemDTO(p.getId(), resposta));
        }
        return itens;
    }

    public static class PerguntaViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPergunta;
        final Button btnSim, btnNao;

        public PerguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPergunta = itemView.findViewById(R.id.tvPergunta);
            btnSim     = itemView.findViewById(R.id.btnSim);
            btnNao     = itemView.findViewById(R.id.btnNao);
        }
    }
}
