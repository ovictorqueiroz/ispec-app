package br.com.etecia.ispec_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.clientes_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvClientes = findViewById(R.id.rvClientes);

        rvClientes.setLayoutManager(new LinearLayoutManager(this));

        List<ClienteModel> listaClientes = new ArrayList<>();
        listaClientes.add(new ClienteModel(1L, "Etec Irmã Agostina", "Av. Feliciano Coreia, s/n - SP"));
        listaClientes.add(new ClienteModel(2L, "Alvaro Mercado", "Av. Feliciano Coreia, 380 - SP"));

        ClienteAdapter adapter = new ClienteAdapter(listaClientes);
        rvClientes.setAdapter(adapter);
    }
}