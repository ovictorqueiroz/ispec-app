package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import br.com.etecia.ispec_app.adapter.EquipamentoAdapter;
import br.com.etecia.ispec_app.viewmodel.EquipamentoViewModel;

public class EquipamentoActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    public static final String EXTRA_CLIENTE_ID   = "clienteId";
    public static final String EXTRA_CLIENTE_NOME = "clienteNome";

    private ImageView arrowLeft;
    private EquipamentoViewModel viewModel;
    private SwipeRefreshLayout swpRefresh;

    // FAB e overlay
    private FloatingActionButton fabAdicionar;
    private View overlayEscurecido;
    private LinearLayout menuFab;
    private boolean menuFabAberto = false;

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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int idItem = menuItem.getItemId();

            if (idItem == R.id.nav_home){
                startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
                finish();
            }
            return true;
        });

        arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ClientesActivity.class));
            finish();
        });

        // Exibe o nome do cliente no header
        String clienteNome = getIntent().getStringExtra(EXTRA_CLIENTE_NOME);
        TextView txtEquipamento = findViewById(R.id.txtEquipamento);
        if (clienteNome != null && !clienteNome.isEmpty()) {
            txtEquipamento.setText(clienteNome);
        } else {
            txtEquipamento.setText("Equipamentos");
        }

        Long clienteId = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1L);
        if (clienteId == -1L) {
            Toast.makeText(this, "Cliente não identificado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // RecyclerView
        RecyclerView rvEquipamentos = findViewById(R.id.rvEquipamentos);
        rvEquipamentos.setLayoutManager(new LinearLayoutManager(this));
        EquipamentoAdapter adapter = new EquipamentoAdapter(new ArrayList<>());
        rvEquipamentos.setAdapter(adapter);

        // SwipeRefresh
        swpRefresh = findViewById(R.id.swpRefresh);
        swpRefresh.setOnRefreshListener(() -> viewModel.listarPorCliente(clienteId));

        // ViewModel
        viewModel = new ViewModelProvider(this).get(EquipamentoViewModel.class);
        viewModel.listarPorCliente(clienteId);
        viewModel.getEquipamentos().observe(this, equipamentos -> {
            adapter.atualizarLista(equipamentos);
            swpRefresh.setRefreshing(false);
        });

        // ── FAB e overlay ──
        fabAdicionar     = findViewById(R.id.fabAdicionar);
        overlayEscurecido = findViewById(R.id.overlayEscurecido);
        menuFab          = findViewById(R.id.menuFab);

        fabAdicionar.setOnClickListener(v -> toggleMenuFab());

        // Fechar menu ao tocar no overlay
        overlayEscurecido.setOnClickListener(v -> fecharMenuFab());

        // Botão "Adicionar Equipamento" — abre CadastroEquipamentoActivity com clienteId pré-preenchido
        findViewById(R.id.btnAdicionarEquipamento).setOnClickListener(v -> {
            fecharMenuFab();
            Intent intent = new Intent(this, CadastroEquipamentoActivity.class);
            intent.putExtra(CadastroEquipamentoActivity.EXTRA_CLIENTE_ID,   clienteId);
            intent.putExtra(CadastroEquipamentoActivity.EXTRA_CLIENTE_NOME, clienteNome);
            startActivity(intent);
        });

        // Botão "Adicionar Localização" — abre CadastroLocalizacaoActivity com clienteId pré-preenchido
        findViewById(R.id.btnAdicionarLocalizacao).setOnClickListener(v -> {
            fecharMenuFab();
            Intent intent = new Intent(this, CadastroLocalizacaoActivity.class);
            intent.putExtra(CadastroLocalizacaoActivity.EXTRA_CLIENTE_ID,   clienteId);
            intent.putExtra(CadastroLocalizacaoActivity.EXTRA_CLIENTE_NOME, clienteNome);
            startActivity(intent);
        });
    }

    private void toggleMenuFab() {
        if (menuFabAberto) {
            fecharMenuFab();
        } else {
            abrirMenuFab();
        }
    }

    private void abrirMenuFab() {
        menuFabAberto = true;
        overlayEscurecido.setVisibility(View.VISIBLE);
        menuFab.setVisibility(View.VISIBLE);
        fabAdicionar.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
    }

    private void fecharMenuFab() {
        menuFabAberto = false;
        overlayEscurecido.setVisibility(View.GONE);
        menuFab.setVisibility(View.GONE);
        fabAdicionar.setImageResource(android.R.drawable.ic_input_add);
    }
}
