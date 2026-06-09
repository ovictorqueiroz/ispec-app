package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.etecia.ispec_app.adapter.AvisosAdapter;
import br.com.etecia.ispec_app.viewmodel.AvisosViewModel;

/**
 * Tela de Avisos — mini-dashboard dinâmico.
 *
 * Exibe 5 blocos calculados ao vivo pelo backend (GET /avisos):
 *   1. Equipamentos vencidos
 *   2. Vencendo em até 30 dias
 *   3. Vencendo entre 31 e 90 dias
 *   4. Inspeções reprovadas nos últimos 30 dias
 *   5. Agenda da semana (próximos 7 dias, status PENDENTE)
 *
 * NÃO usa tabela de notificações nem alertas persistidos.
 * Todos os dados são calculados dinamicamente no backend.
 */
public class AvisosActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private AvisosViewModel viewModel;
    private AvisosAdapter   adapter;
    private SwipeRefreshLayout swpRefresh;
    private TextView tvAtualizadoAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.avisos_layout);

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

        // --- Views ---
        ImageView arrowLeft    = findViewById(R.id.arrowLeft);
        RecyclerView recycler  = findViewById(R.id.recyclerAvisos);
        swpRefresh             = findViewById(R.id.swpRefreshAvisos);
        tvAtualizadoAs         = findViewById(R.id.tvAtualizadoAs);

        // Voltar ao menu
        arrowLeft.setOnClickListener(v ->
                startActivity(new Intent(this, MenuPrincipalActivity.class)));

        // RecyclerView
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AvisosAdapter();
        recycler.setAdapter(adapter);

        // SwipeRefresh — recarrega ao puxar para baixo
        swpRefresh.setOnRefreshListener(() -> viewModel.carregarAvisos());

        // --- ViewModel e observadores ---
        viewModel = new ViewModelProvider(this).get(AvisosViewModel.class);

        viewModel.getAvisos().observe(this, response -> {
            adapter.atualizarDados(response);
            swpRefresh.setRefreshing(false);
            // Exibe horário de atualização no canto superior direito
            String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            tvAtualizadoAs.setText("Atualizado às " + hora);
            tvAtualizadoAs.setVisibility(View.VISIBLE);
        });

        viewModel.getCarregando().observe(this, carregando -> {
            if (Boolean.TRUE.equals(carregando)) swpRefresh.setRefreshing(true);
        });

        viewModel.getErro().observe(this, erro -> {
            swpRefresh.setRefreshing(false);
            Toast.makeText(this, "Erro ao carregar avisos: " + erro, Toast.LENGTH_SHORT).show();
        });

        // Carrega ao abrir a tela
        viewModel.carregarAvisos();
    }
}
