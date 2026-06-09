package br.com.etecia.ispec_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.etecia.ispec_app.adapter.AgendamentoAdapter;
import br.com.etecia.ispec_app.model.AgendamentoModel;
import br.com.etecia.ispec_app.model.UsuarioModel;
import br.com.etecia.ispec_app.viewmodel.AgendamentoViewModel;

public class AgendaActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private AgendamentoViewModel viewModel;
    private AgendamentoAdapter adapter;

    // Views da tela principal
    private RecyclerView rvAgendamentos;
    private ImageView arrowLeft;

    // Estado do calendário
    private int anoAtual;
    private int mesAtual;

    // Dados para o dialog
    private List<UsuarioModel> listaUsuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.agenda_layout);
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

        // Inicializa com o mês atual
        Calendar cal = Calendar.getInstance();
        anoAtual = cal.get(Calendar.YEAR);
        mesAtual = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH é 0-based

        // Views
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuPrincipalActivity.class));
            finish();
        });

        Button btnAgendar = findViewById(R.id.btnAgendarNovaVisita);
        btnAgendar.setOnClickListener(v -> abrirDialogAgendamento());

        rvAgendamentos = findViewById(R.id.rvAgendamentos);
        rvAgendamentos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AgendamentoAdapter();
        rvAgendamentos.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(AgendamentoViewModel.class);

        viewModel.getAgendamentos().observe(this, agendamentos -> {
            if (agendamentos != null) {
                adapter.atualizarLista(agendamentos);
            }
        });

        viewModel.getUsuarios().observe(this, usuarios -> {
            if (usuarios != null) {
                listaUsuarios = usuarios;
            }
        });

        viewModel.getSalvouComSucesso().observe(this, sucesso -> {
            if (Boolean.TRUE.equals(sucesso)) {
                Toast.makeText(this, "Visita agendada com sucesso!", Toast.LENGTH_SHORT).show();
                viewModel.buscarAgendamentos();
            }
        });

        viewModel.getErro().observe(this, erro -> {
            if (erro != null && !erro.isEmpty()) {
                Toast.makeText(this, "Erro: " + erro, Toast.LENGTH_LONG).show();
            }
        });

        // Carrega dados iniciais
        viewModel.buscarAgendamentos();
        viewModel.buscarUsuarios();
    }

    private void abrirDialogAgendamento() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_agendar_visita, null);

        // --- Componentes do dialog ---
        Spinner spinnerTipo    = dialogView.findViewById(R.id.spinnerTipo);
        EditText etTitulo      = dialogView.findViewById(R.id.etTitulo);
        TextView tvData        = dialogView.findViewById(R.id.tvDataSelecionada);
        Spinner spinnerResponsavel = dialogView.findViewById(R.id.spinnerResponsavel);
        Spinner spinnerStatus  = dialogView.findViewById(R.id.spinnerStatus);
        Button btnSalvar       = dialogView.findViewById(R.id.btnSalvarAgendamento);
        Button btnEscolherData = dialogView.findViewById(R.id.btnEscolherData);

        // --- Spinner Tipo ---
        String[] tipos = {"INSPECAO", "MANUTENCAO", "VISITA_TECNICA"};
        String[] tiposLabel = {"Inspeção", "Manutenção", "Visita Técnica"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposLabel);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        // --- Spinner Status ---
        String[] statusValues = {"PENDENTE", "REALIZADO", "CANCELADO"};
        String[] statusLabels = {"Pendente", "Realizado", "Cancelado"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusLabels);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);

        // --- Spinner Responsável ---
        final String[] dataSelecionada = {""};
        List<String> nomesUsuarios = new ArrayList<>();
        nomesUsuarios.add("Selecione o responsável");
        for (UsuarioModel u : listaUsuarios) {
            nomesUsuarios.add(u.getNome());
        }
        ArrayAdapter<String> adapterResp = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nomesUsuarios);
        adapterResp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResponsavel.setAdapter(adapterResp);

        // --- DatePicker ---
        Calendar cal = Calendar.getInstance();
        btnEscolherData.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        // Formata como YYYY-MM-DD para enviar ao backend
                        String dataFormatada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        dataSelecionada[0] = dataFormatada;
                        // Exibe de forma amigável na tela
                        tvData.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        // --- Constrói o dialog ---
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // --- Botão Agendar ---
        btnSalvar.setOnClickListener(v -> {
            String titulo = etTitulo.getText() != null ? etTitulo.getText().toString().trim() : "";

            // Validações
            if (titulo.isEmpty()) {
                etTitulo.setError("Informe o título / razão social");
                return;
            }
            if (dataSelecionada[0].isEmpty()) {
                Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show();
                return;
            }
            int posResponsavel = spinnerResponsavel.getSelectedItemPosition();
            if (posResponsavel == 0 || posResponsavel > listaUsuarios.size()) {
                Toast.makeText(this, "Selecione o responsável", Toast.LENGTH_SHORT).show();
                return;
            }

            // Monta o objeto
            AgendamentoModel agendamento = new AgendamentoModel();
            agendamento.setTitulo(titulo);
            agendamento.setData(dataSelecionada[0]);
            agendamento.setTipo(tipos[spinnerTipo.getSelectedItemPosition()]);
            agendamento.setStatus(statusValues[spinnerStatus.getSelectedItemPosition()]);

            UsuarioModel usuarioSelecionado = listaUsuarios.get(posResponsavel - 1);
            AgendamentoModel.ResponsavelModel responsavel = new AgendamentoModel.ResponsavelModel(usuarioSelecionado.getId());
            agendamento.setResponsavel(responsavel);

            viewModel.salvarAgendamento(agendamento);
            dialog.dismiss();
        });

        dialog.show();
    }
}
