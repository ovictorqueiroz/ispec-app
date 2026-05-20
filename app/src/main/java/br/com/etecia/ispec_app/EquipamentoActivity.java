package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import br.com.etecia.ispec_app.adapter.EquipamentoAdapter;
import br.com.etecia.ispec_app.viewmodel.EquipamentoViewModel;

public class EquipamentoActivity extends AppCompatActivity {

    public static final String EXTRA_CLIENTE_ID   = "clienteId";
    public static final String EXTRA_CLIENTE_NOME = "clienteNome"; // FIX Bug #4

    private ImageView arrowLeft;
    private EquipamentoViewModel viewModel;
    private SwipeRefreshLayout swpRefresh; // FIX Bug #3

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

        // FIX Bug #4: exibe o nome do cliente no header
        String clienteNome = getIntent().getStringExtra(EXTRA_CLIENTE_NOME);
        TextView txtEquipamento = findViewById(R.id.txtEquipamento);
        if (clienteNome != null && !clienteNome.isEmpty()) {
            txtEquipamento.setText(clienteNome);
        } else {
            txtEquipamento.setText("Equipamentos");
        }

        // FIX Bug #2: valida o clienteId antes de continuar
        Long clienteId = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1L);
        if (clienteId == -1L) {
            Toast.makeText(this, "Cliente não identificado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configura RecyclerView e Adapter
        RecyclerView rvEquipamentos = findViewById(R.id.rvEquipamentos);
        rvEquipamentos.setLayoutManager(new LinearLayoutManager(this));
        EquipamentoAdapter adapter = new EquipamentoAdapter(new ArrayList<>());
        rvEquipamentos.setAdapter(adapter);

        // FIX Bug #3: conecta o SwipeRefreshLayout
        swpRefresh = findViewById(R.id.swpRefresh);
        swpRefresh.setOnRefreshListener(() -> viewModel.listarPorCliente(clienteId));

        // Inicializa ViewModel e faz a busca
        viewModel = new ViewModelProvider(this).get(EquipamentoViewModel.class);
        viewModel.listarPorCliente(clienteId);

        viewModel.getEquipamentos().observe(this, equipamentos -> {
            adapter.atualizarLista(equipamentos);
            swpRefresh.setRefreshing(false); // FIX Bug #3
        });
    }
}
