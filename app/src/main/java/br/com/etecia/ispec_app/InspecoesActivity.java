package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;

import br.com.etecia.ispec_app.adapter.InspecaoAdapter;
import br.com.etecia.ispec_app.viewmodel.InspecaoViewModel;

public class InspecoesActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private InspecaoViewModel viewModel;
    private InspecaoAdapter adapter;
    private SwipeRefreshLayout swpRefresh;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inspecoes_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Voltar ao menu
        ImageView arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuPrincipalActivity.class));
            finish();
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

        // RecyclerView
        RecyclerView rvInspecoes = findViewById(R.id.rvInspecoes);
        rvInspecoes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InspecaoAdapter(new ArrayList<>());
        rvInspecoes.setAdapter(adapter);

        // SwipeRefresh
        swpRefresh = findViewById(R.id.swpRefresh);
        swpRefresh.setOnRefreshListener(() -> viewModel.listarTodas());

        // SearchBar — filtra a lista localmente
        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // ViewModel
        viewModel = new ViewModelProvider(this).get(InspecaoViewModel.class);

        viewModel.getInspecoes().observe(this, inspecoes -> {
            adapter.atualizarLista(inspecoes);
            swpRefresh.setRefreshing(false);
        });

        viewModel.getErro().observe(this, erro -> {
            swpRefresh.setRefreshing(false);
            Toast.makeText(this, "Erro ao carregar inspeções: " + erro, Toast.LENGTH_SHORT).show();
        });

        viewModel.listarTodas();
    }
}
