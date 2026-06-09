package br.com.etecia.ispec_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.etecia.ispec_app.adapter.PerguntaAdapter;
import br.com.etecia.ispec_app.requests.InspecaoRequest;
import br.com.etecia.ispec_app.viewmodel.InspecaoViewModel;

public class RealizarInspecaoActivity extends AppCompatActivity {


    public static final String EXTRA_EQUIPAMENTO_ID   = "equipamentoId";
    public static final String EXTRA_EQUIPAMENTO_NOME = "equipamentoNome";
    public static final String EXTRA_EQUIPAMENTO_TIPO = "equipamentoTipo";

    private InspecaoViewModel viewModel;
    private PerguntaAdapter perguntaAdapter;

    private Long equipamentoId;
    private String dataInspecao = "";
    private EditText etData, etObservacoes;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.realizar_inspecao_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        equipamentoId = getIntent().getLongExtra(EXTRA_EQUIPAMENTO_ID, -1L);
        String equipamentoNome = getIntent().getStringExtra(EXTRA_EQUIPAMENTO_NOME);
        String equipamentoTipo = getIntent().getStringExtra(EXTRA_EQUIPAMENTO_TIPO);

        if (equipamentoId == -1L || equipamentoTipo == null) {
            Toast.makeText(this, "Equipamento inválido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView txtTitulo = findViewById(R.id.txtTituloInspecao);
        txtTitulo.setText(equipamentoNome != null ? equipamentoNome : "Inspeção");

        ImageView arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> finish());

        etData = findViewById(R.id.etDataInspecao);
        etData.setFocusable(false);
        etData.setOnClickListener(v -> abrirDatePicker());

        etObservacoes = findViewById(R.id.etObservacoes);
        progressBar   = findViewById(R.id.progressBar);

        RecyclerView rvPerguntas = findViewById(R.id.rvPerguntas);
        rvPerguntas.setLayoutManager(new LinearLayoutManager(this));
        perguntaAdapter = new PerguntaAdapter(new ArrayList<>());
        rvPerguntas.setAdapter(perguntaAdapter);

        viewModel = new ViewModelProvider(this).get(InspecaoViewModel.class);

        viewModel.getPerguntas().observe(this, perguntas -> {
            progressBar.setVisibility(View.GONE);
            perguntaAdapter = new PerguntaAdapter(perguntas);
            rvPerguntas.setAdapter(perguntaAdapter);
        });

        viewModel.getInspecaoCriada().observe(this, inspecao -> {
            progressBar.setVisibility(View.GONE);
            String resultado = Boolean.TRUE.equals(inspecao.getAprovado()) ? "APROVADA ✓" : "REPROVADA ✗";
            Toast.makeText(this, "Inspeção " + resultado, Toast.LENGTH_LONG).show();
            finish();
        });

        viewModel.getErro().observe(this, erro -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Erro: " + erro, Toast.LENGTH_SHORT).show();
        });

        viewModel.getCarregando().observe(this, carregando ->
            progressBar.setVisibility(Boolean.TRUE.equals(carregando) ? View.VISIBLE : View.GONE)
        );

        viewModel.buscarPerguntasPorTipo(equipamentoTipo);

        Button btnSalvar = findViewById(R.id.btnSalvarInspecao);
        btnSalvar.setOnClickListener(v -> submeterInspecao());
    }

    private void abrirDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            dataInspecao = String.format("%04d-%02d-%02d", year, month + 1, day);
            etData.setText(dataInspecao);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void submeterInspecao() {
        if (dataInspecao.isEmpty()) {
            Toast.makeText(this, "Informe a data da inspeção.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<InspecaoRequest.ItemDTO> itens;
        try {
            itens = perguntaAdapter.getItens();
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        if (itens.isEmpty()) {
            Toast.makeText(this, "Nenhuma pergunta disponível.", Toast.LENGTH_SHORT).show();
            return;
        }

        InspecaoRequest request = new InspecaoRequest();
        request.setEquipamentoId(equipamentoId);
        request.setResponsavelId(null);
        request.setDataInspecao(dataInspecao);
        String obs = etObservacoes.getText().toString().trim();
        request.setObservacoes(obs.isEmpty() ? null : obs);
        request.setItens(itens);

        viewModel.criarInspecao(request);
    }
}
