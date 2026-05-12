package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.List;

import br.com.etecia.ispec_app.adapter.ClienteAdapter;
import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.viewmodel.ClienteViewModel;

public class ClientesActivity extends AppCompatActivity {
    private ImageView arrowLeft;
    private ClienteViewModel viewModel;

    private SwipeRefreshLayout swpRefresh;

    private EditText search_bar;


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
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
                finish();
            }
        });




        RecyclerView rvClientes = findViewById(R.id.rvClientes);

        rvClientes.setLayoutManager(new LinearLayoutManager(this));

        ClienteAdapter adapter = new ClienteAdapter(new ArrayList<>());
        rvClientes.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(ClienteViewModel .class);


        viewModel.buscarClientes(); //chama os clientes da API

        //Atualiza a página
        swpRefresh = findViewById(R.id.swpRefresh);
        swpRefresh.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

        @Override
        public void onRefresh() {
            viewModel.buscarClientes();
        }}
        );



        viewModel.getClientes().observe(this, clientes -> {
            adapter.atualizarLista(clientes);
            swpRefresh.setRefreshing(false);
        });


        //Busca os clientes na barra de pesquisa
        search_bar =findViewById(R.id.search_bar);


        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
}
