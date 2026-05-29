package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPrincipalActivity extends AppCompatActivity {

    CardView cardAvisos, cardClientes, cardRelatorios, cardAgenda, cardCadEquipamentos, cardInspecoes;

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

        cardAvisos           = findViewById(R.id.cardAvisos);
        cardClientes         = findViewById(R.id.cardClientes);
        cardCadEquipamentos  = findViewById(R.id.cardCadEquipamentos);
        cardAgenda           = findViewById(R.id.cardAgenda);
        cardInspecoes        = findViewById(R.id.cardInspecoes);

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

        // Inspeções → histórico de inspeções
        cardInspecoes.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), InspecoesActivity.class));
            finish();
        });
    }
}
