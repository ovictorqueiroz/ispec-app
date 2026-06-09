package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.etecia.ispec_app.viewmodel.AvisosViewModel;

/**
 * MODIFICADO: adicionado badge de avisos no cardAvisos.
 *
 * O badge exibe a soma de:
 *   vencidos + vencendo30 + vencendo90 + reprovadas
 * (Agenda da semana NÃO entra no cálculo — READMEFIRST)
 *
 * Caso total == 0, o badge fica oculto (visibility GONE).
 */
public class MenuPrincipalActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    CardView cardAvisos, cardClientes, cardAgenda, cardCadEquipamentos, cardInspecoes;
    private TextView tvBadgeAvisos;
    private AvisosViewModel avisosViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_principal_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        cardAvisos           = findViewById(R.id.cardAvisos);
        cardClientes         = findViewById(R.id.cardClientes);
        cardCadEquipamentos  = findViewById(R.id.cardCadEquipamentos);
        cardAgenda           = findViewById(R.id.cardAgenda);
        cardInspecoes        = findViewById(R.id.cardInspecoes);
        tvBadgeAvisos        = findViewById(R.id.tvBadgeAvisos);

        // --- Navegação ---
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int idItem = menuItem.getItemId();

            if (idItem == R.id.nav_home){
                startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
                finish();
            }
            return true;
        });


        cardAvisos.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AvisosActivity.class));
            finish();
        });

        cardClientes.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ClientesActivity.class));
            finish();
        });

        cardCadEquipamentos.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CadastroEquipamentoActivity.class));
            finish();
        });

        cardAgenda.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AgendaActivity.class));
            finish();
        });

        cardInspecoes.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), InspecoesActivity.class));
            finish();
        });

        // --- Badge de avisos ---
        avisosViewModel = new ViewModelProvider(this).get(AvisosViewModel.class);

        avisosViewModel.getTotalAvisos().observe(this, total -> {
            if (total != null && total > 0) {
                tvBadgeAvisos.setText(total + (total == 1 ? " aviso" : " avisos"));
                tvBadgeAvisos.setVisibility(View.VISIBLE);
            } else {
                tvBadgeAvisos.setVisibility(View.GONE);
            }
        });

        // Busca silenciosa ao abrir o menu (apenas para o badge)
        avisosViewModel.carregarAvisos();
    }
}
