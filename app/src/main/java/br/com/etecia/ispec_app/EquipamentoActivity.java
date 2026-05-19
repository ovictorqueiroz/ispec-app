package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.etecia.ispec_app.adapter.EquipamentoAdapter;
import br.com.etecia.ispec_app.viewmodel.EquipamentoViewModel;

public class EquipamentoActivity extends AppCompatActivity {

    public static final String EXTRA_CLIENTE_ID = "clienteId";

    private ImageView arrowLeft;
    private EquipamentoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.equipamento_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ClientesActivity.class));
            finish();
        });

        // 1. Recupera o clienteId que veio do Intent
        Long clienteId = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1);

        // 2. Configura o RecyclerView e o Adapter
        RecyclerView rvEquipamentos = findViewById(R.id.rvEquipamentos);
        rvEquipamentos.setLayoutManager(new LinearLayoutManager(this));
        EquipamentoAdapter adapter = new EquipamentoAdapter(new ArrayList<>());
        rvEquipamentos.setAdapter(adapter);

        // 3. Inicializa o ViewModel e busca os equipamentos
        viewModel = new ViewModelProvider(this).get(EquipamentoViewModel.class);
        viewModel.listarPorCliente(clienteId);

        // 4. Observa o LiveData e atualiza a tela quando os dados chegarem
        viewModel.getEquipamentos().observe(this, equipamentos -> {
            adapter.atualizarLista(equipamentos);
        });
    }
}