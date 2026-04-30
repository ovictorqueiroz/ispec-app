package br.com.etecia.ispec_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;

import br.com.etecia.ispec_app.model.EquipamentoRequest;
import br.com.etecia.ispec_app.network.ApiService;
import br.com.etecia.ispec_app.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroEquipamentoActivity extends AppCompatActivity {

    // Header
    private ImageView arrowLeft;

    //Campos datas
    private String dataInstalacaoFormatada;
    private String dataValidadeFormatada;
    private String ultimaVerificacaoFormatada;

    // Campos comuns
    private EditText edtIdCliente, edtNome, edtLocalizacao, edtDataInstalacao;
    private Spinner spinnerStatus, spinnerTipo;

    // Campos Extintor
    private LinearLayout camposExtintor;
    private Spinner spinnerClasseFogo;
    private EditText edtCapacidade, edtDataValidade, edtPressao;

    // Campos Alarme
    private LinearLayout camposAlarme;
    private Spinner spinnerTipoSensor;
    private EditText edtUltimaVerificacao;
    private Switch switchFuncionando;

    // Campos Hidrante
    private LinearLayout camposHidrante;
    private EditText edtPressaoAgua, edtCompMangueira;
    private Switch switchDisponivel;

    // Botão
    private Button btnCadastrar;

    // ----------------------------------------------------------------
    // DatePicker helper
    // ----------------------------------------------------------------
    private void datePicker(EditText campo) {
        LocalDate hoje = LocalDate.now();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String dataStandart = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    String dataUser = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);

                    if (campo == edtDataInstalacao) {
                        dataInstalacaoFormatada = dataStandart;
                    } else if (campo == edtDataValidade) {
                        dataValidadeFormatada = dataStandart;
                    } else if (campo == edtUltimaVerificacao) {
                        ultimaVerificacaoFormatada = dataStandart;
                    }

                    campo.setText(dataUser);
                },
                hoje.getYear(), hoje.getMonthValue() - 1, hoje.getDayOfMonth()
        );
        dialog.show();
    }

    // ----------------------------------------------------------------
    // Validação dos campos comuns
    // ----------------------------------------------------------------
    private boolean camposComumPreenchidos() {
        if (edtIdCliente.getText().toString().trim().isEmpty()) {
            edtIdCliente.setError("Informe o código do cliente");
            edtIdCliente.requestFocus();
            return false;
        }
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError("Informe o nome do equipamento");
            edtNome.requestFocus();
            return false;
        }
        if (edtLocalizacao.getText().toString().trim().isEmpty()) {
            edtLocalizacao.setError("Informe a localização");
            edtLocalizacao.requestFocus();
            return false;
        }
        if (edtDataInstalacao.getText().toString().trim().isEmpty()) {
            edtDataInstalacao.setError("Informe a data de instalação");
            edtDataInstalacao.requestFocus();
            return false;
        }
        return true;
    }

    // ----------------------------------------------------------------
    // Coleta os dados do formulário e monta o EquipamentoRequest
    // ----------------------------------------------------------------
    private EquipamentoRequest montarRequest() {
        EquipamentoRequest req = new EquipamentoRequest();

        // Campos comuns
        req.setCliente(new EquipamentoRequest.IdWrapper(Long.parseLong(edtIdCliente.getText().toString().trim())));
        req.setNome(edtNome.getText().toString().trim());
        req.setLocalizacao(new EquipamentoRequest.IdWrapper(Long.parseLong(edtLocalizacao.getText().toString().trim())));
        req.setDataInstalacao(dataInstalacaoFormatada.trim());
        req.setStatus(spinnerStatus.getSelectedItem().toString());

        String tipoStr = spinnerTipo.getSelectedItem().toString();
        EquipamentoRequest.IdWrapper tipoWrapper;

        req.setTipo(tipoStr);


        // Campos específicos por tipo
        switch (tipoStr) {
            case "extintor":
                tipoWrapper = new EquipamentoRequest.IdWrapper(1);
                req.setClasseFogo(spinnerClasseFogo.getSelectedItem().toString());

                String capStr = edtCapacidade.getText().toString().trim();
                if (!capStr.isEmpty()) req.setCapacidade(Double.parseDouble(capStr));

                req.setDataValidade(dataValidadeFormatada.trim());

                String pressaoStr = edtPressao.getText().toString().trim();
                if (!pressaoStr.isEmpty()) req.setPressao(Double.parseDouble(pressaoStr));
                break;

            case "alarme":
                tipoWrapper = new EquipamentoRequest.IdWrapper(2);
                req.setTipoSensor(spinnerTipoSensor.getSelectedItem().toString());
                req.setUltimaVerificacao(ultimaVerificacaoFormatada.trim());
                req.setFuncionando(switchFuncionando.isChecked());
                break;

            case "hidrante":
                tipoWrapper = new EquipamentoRequest.IdWrapper(3);
                String pressaoAguaStr = edtPressaoAgua.getText().toString().trim();
                if (!pressaoAguaStr.isEmpty()) req.setPressaoAgua(Double.parseDouble(pressaoAguaStr));

                String compStr = edtCompMangueira.getText().toString().trim();
                if (!compStr.isEmpty()) req.setComprimentoMangueira(Double.parseDouble(compStr));

                req.setDisponivel(switchDisponivel.isChecked());
                break;

            default:
                tipoWrapper = null;
                break;
        }
        req.setTipoEquipamento(tipoWrapper);
        Gson gson = new Gson();
        String json = gson.toJson(req);

        Log.d("testeCadastro", json);

        return req;
    }

    // ----------------------------------------------------------------
    // Chamada à API
    // ----------------------------------------------------------------
    private void cadastrarEquipamento() {
        if (!camposComumPreenchidos()) return;

        EquipamentoRequest request = montarRequest();

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = api.cadastrarEquipamento(request);

        // Desabilita o botão para evitar duplo envio
        btnCadastrar.setEnabled(false);
        Log.d("testeAPICadastro", call.request().url().toString());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnCadastrar.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(
                            CadastroEquipamentoActivity.this,
                            "Equipamento cadastrado com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish(); // Volta para a tela anterior
                } else {
                    String errorMsg = "Erro desconhecido";
                    try {
                        errorMsg = response.errorBody().string();
                        Log.e("ERRO_CADASTRO", errorMsg);
                    } catch (IOException e) {
                        errorMsg = e.getMessage();
                    }
                    Toast.makeText(
                            CadastroEquipamentoActivity.this,
                            "Erro ao cadastrar: " + errorMsg,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnCadastrar.setEnabled(true);
                Toast.makeText(
                        CadastroEquipamentoActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // ----------------------------------------------------------------
    // onCreate
    // ----------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cadastro_equipamento_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Header — botão voltar
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
            finish();
        });

        // Campos comuns
        edtIdCliente = findViewById(R.id.edtIdCliente);
        edtNome = findViewById(R.id.edtNome);
        edtLocalizacao = findViewById(R.id.edtLocalizacao);
        edtDataInstalacao = findViewById(R.id.edtDataInstalacao);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        // Campos Extintor
        camposExtintor = findViewById(R.id.camposExtintor);
        spinnerClasseFogo = findViewById(R.id.spinnerClasseFogo);
        edtCapacidade = findViewById(R.id.edtCapacidade);
        edtDataValidade = findViewById(R.id.edtDataValidade);
        edtPressao = findViewById(R.id.edtPressao);

        // Campos Alarme
        camposAlarme = findViewById(R.id.camposAlarme);
        spinnerTipoSensor = findViewById(R.id.spinnerTipoSensor);
        edtUltimaVerificacao = findViewById(R.id.edtUltimaVerificacao);
        switchFuncionando = findViewById(R.id.switchFuncionando);

        // Campos Hidrante
        camposHidrante = findViewById(R.id.camposHidrante);
        edtPressaoAgua = findViewById(R.id.edtPressaoAgua);
        edtCompMangueira = findViewById(R.id.edtCompMangueira);
        switchDisponivel = findViewById(R.id.switchDisponivel);

        // Botão
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // ---- Populando Spinners ----
        String[] statusEquipamento = {"ATIVO", "INATIVO"};
        String[] tipoEquipamento   = {"alarme", "extintor", "hidrante"};
        String[] classeFogo          = {"Água - A/B/C", "Gás Carbônico - B/C", "Pó Químico - B/C",
                                      "Pó Químico - A/B/C", "Pó Químico - D",
                                      "Espuma - A/B/C", "Acetato de Potássio - K"};
        String[] tipoSensor        = {"Temperatura", "Movimento", "Fumaça"};

        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoEquipamento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusEquipamento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classeFogo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasseFogo.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoSensor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSensor.setAdapter(adapter);

        // ---- Mostrar/esconder campos por tipo ----
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                camposAlarme.setVisibility(View.GONE);
                camposExtintor.setVisibility(View.GONE);
                camposHidrante.setVisibility(View.GONE);
                switch (position) {
                    case 0: camposAlarme.setVisibility(View.VISIBLE); break;
                    case 1: camposExtintor.setVisibility(View.VISIBLE); break;
                    case 2: camposHidrante.setVisibility(View.VISIBLE); break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                camposAlarme.setVisibility(View.GONE);
                camposExtintor.setVisibility(View.GONE);
                camposHidrante.setVisibility(View.GONE);
            }
        });

        // ---- DatePickers ----
        edtDataInstalacao.setOnClickListener(v -> datePicker(edtDataInstalacao));
        edtDataValidade.setOnClickListener(v -> datePicker(edtDataValidade));
        edtUltimaVerificacao.setOnClickListener(v -> datePicker(edtUltimaVerificacao));

        // ---- Botão Cadastrar ----
        btnCadastrar.setOnClickListener(v -> cadastrarEquipamento());
    }
}
