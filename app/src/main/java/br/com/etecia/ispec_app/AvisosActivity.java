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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.etecia.ispec_app.adapter.AvisoAdapter;
import br.com.etecia.ispec_app.model.AvisoModel;

public class AvisosActivity extends AppCompatActivity {

    ImageView arrowLeft;

    RecyclerView recyclerAvisos;

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
        ImageView seta = findViewById(R.id.arrowLeft);
        RecyclerView recycler = findViewById(R.id.recyclerAvisos);

        seta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvisosActivity.this, MenuPrincipalActivity.class);
                startActivity(intent);
            }
        });
        List<AvisoModel> listaAvisos = new ArrayList<>();

        listaAvisos.add(new AvisoModel(1L, "Extintor vencido", "São Paulo-SP, 04/02/2026"));
        listaAvisos.add(new AvisoModel(2L, "Inspeção agendada para amanhã", "São Paulo-SP, 23/06/2026"));
        listaAvisos.add(new AvisoModel(3L, "Mangueira precisa de manutenção", "São Paulo-SP, 09/05/2026"));

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new AvisoAdapter(listaAvisos));
    }
}