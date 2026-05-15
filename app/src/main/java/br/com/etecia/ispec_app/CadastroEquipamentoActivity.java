package br.com.etecia.ispec_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import br.com.etecia.ispec_app.model.ClienteModel;
import br.com.etecia.ispec_app.model.LocalizacaoModel;
import br.com.etecia.ispec_app.model.TipoSensorModel;
import br.com.etecia.ispec_app.requests.EquipamentoRequest;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
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
    private TextView txtVIdCliente, txtLocalizacao;

    // Campos Extintor
    private LinearLayout camposExtintor;
    private Spinner spinnerClasseFogo;
    private EditText edtCapacidade, edtDataValidade, edtPressao;
    private HashMap<String, Integer> agenteMap = new HashMap<>();
    private HashMap<String, List<Integer>> classeFogoMap = new HashMap<>();
    private HashMap<String, TipoSensorModel> tipoSensorMap = new HashMap<>();

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
            case "Extintor":
                tipoWrapper = new EquipamentoRequest.IdWrapper(1);

                List<Integer> ids = classeFogoMap.get(spinnerClasseFogo.getSelectedItem().toString());
                List<EquipamentoRequest.IdWrapper> wrappers = ids.stream()
                        .map(id -> new EquipamentoRequest.IdWrapper(id))
                        .collect(Collectors.toList());
                req.setClasseFogo(wrappers);

                int idAgente = agenteMap.get(spinnerClasseFogo.getSelectedItem().toString());
                req.setAgente(new EquipamentoRequest.IdWrapper(idAgente));

                String capStr = edtCapacidade.getText().toString().trim();
                if (!capStr.isEmpty()) req.setCapacidade(Double.parseDouble(capStr));

                req.setDataValidade(dataValidadeFormatada.trim());

                String pressaoStr = edtPressao.getText().toString().trim();
                if (!pressaoStr.isEmpty()) req.setPressao(Double.parseDouble(pressaoStr));
                break;

            case "Alarme":
                tipoWrapper = new EquipamentoRequest.IdWrapper(2);
                req.setTipoSensor(tipoSensorMap.get(spinnerTipoSensor.getSelectedItem().toString()));
                req.setUltimaVerificacao(ultimaVerificacaoFormatada.trim());
                req.setFuncionando(switchFuncionando.isChecked());
                break;

            case "Hidrante":
                tipoWrapper = new EquipamentoRequest.IdWrapper(3);
                String pressaoAguaStr = edtPressaoAgua.getText().toString().trim();
                if (!pressaoAguaStr.isEmpty())
                    req.setPressaoAgua(Double.parseDouble(pressaoAguaStr));

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

    private void limparFormulario() {
        // Campos comuns
        edtIdCliente.setText("");
        edtNome.setText("");
        edtLocalizacao.setText("");
        edtDataInstalacao.setText("");
        dataInstalacaoFormatada = null;
        spinnerStatus.setSelection(0);
        spinnerTipo.setSelection(0);

        // Campos Extintor
        edtCapacidade.setText("");
        edtDataValidade.setText("");
        edtPressao.setText("");
        dataValidadeFormatada = null;
        spinnerClasseFogo.setSelection(0);

        // Campos Alarme
        edtUltimaVerificacao.setText("");
        ultimaVerificacaoFormatada = null;
        switchFuncionando.setChecked(false);

        // Campos Hidrante
        edtPressaoAgua.setText("");
        edtCompMangueira.setText("");
        switchDisponivel.setChecked(false);
    }

    // ----------------------------------------------------------------
    // Chamada à API
    // ----------------------------------------------------------------
    private void cadastrarEquipamento() {
        if (!camposComumPreenchidos()) return;

        EquipamentoRequest request = montarRequest();

        ApiService api = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
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
                    limparFormulario();

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

    private void buscarCliente(Long id) {
        txtVIdCliente.setVisibility(View.VISIBLE);
        txtVIdCliente.setText("Buscando...");
        txtVIdCliente.setTextColor(Color.rgb(179, 177, 177));

        ApiService api = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<ClienteModel> call = api.buscarCliente(id);

        call.enqueue(new Callback<ClienteModel>() {
            @Override
            public void onResponse(Call<ClienteModel> call, Response<ClienteModel> response) {
                if (response.isSuccessful()) {
                    txtVIdCliente.setVisibility(View.VISIBLE);
                    txtVIdCliente.setText(response.body().getRazaoSocial());
                    txtVIdCliente.setTextColor(Color.rgb(178, 213, 121));
                } else {
                    txtVIdCliente.setVisibility(View.VISIBLE);
                    txtVIdCliente.setText("Cliente não encontrado");
                    txtVIdCliente.setTextColor(Color.rgb(163, 29, 29));
                }
            }

            @Override
            public void onFailure(Call<ClienteModel> call, Throwable t) {
                txtVIdCliente.setVisibility(View.VISIBLE);
                txtVIdCliente.setText("Erro de conexão");
                txtVIdCliente.setTextColor(Color.rgb(163, 29, 29));
            }
        });
    }

    private void buscarLocalizacao(long id) {
        txtLocalizacao.setVisibility(View.VISIBLE);
        txtLocalizacao.setText("Buscando...");
        txtLocalizacao.setTextColor(Color.rgb(179, 177, 177));

        ApiService api = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<LocalizacaoModel> call = api.buscarLocalizacao(id);

        call.enqueue(new Callback<LocalizacaoModel>() {


            @Override
            public void onResponse(Call<LocalizacaoModel> call, Response<LocalizacaoModel> response) {
                if (response.isSuccessful()) {
                    String texto = String.format("Bloco: %s | Andar: %s | Sala: %s",
                            response.body().getBloco(),
                            response.body().getAndar(),
                            response.body().getSala());

                    txtVIdCliente.setVisibility(View.VISIBLE);
                    txtVIdCliente.setText(texto);
                    txtVIdCliente.setTextColor(Color.rgb(178, 213, 121));
                } else {
                    txtVIdCliente.setVisibility(View.VISIBLE);
                    txtVIdCliente.setText("Localização não encontrada");
                    txtVIdCliente.setTextColor(Color.rgb(163, 29, 29));
                }
            }

            @Override
            public void onFailure(Call<LocalizacaoModel> call, Throwable t) {
                txtVIdCliente.setVisibility(View.VISIBLE);
                txtVIdCliente.setText("Erro de conexão");
                txtVIdCliente.setTextColor(Color.rgb(163, 29, 29));
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
        txtVIdCliente = findViewById(R.id.txtVIdCliente);

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

        edtIdCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty() || !TextUtils.isDigitsOnly(s.toString())) {
                    txtVIdCliente.setVisibility(View.GONE);
                    return;
                }
                buscarCliente(Long.parseLong(s.toString()));
            }
        });


        // ---- Populando Spinners ----
        String[] statusEquipamento = {"ATIVO", "INATIVO"};
        String[] tipoEquipamento = {"Alarme", "Extintor", "Hidrante"};
        String[] classeFogo = {"Água - A/B/C", "Gás Carbônico - B/C", "Pó Químico - B/C",
                "Pó Químico - A/B/C", "Pó Químico - D",
                "Espuma - A/B/C", "Acetato de Potássio - K"};
        String[] tipoSensor = {"Fumaça", "Temperatura", "Movimento", "Chama"};

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

        // Populando HashMaps dos Extintores
        agenteMap.put("Água - A/B/C", 1);
        agenteMap.put("Gás Carbônico - B/C", 3);
        agenteMap.put("Pó Químico - B/C", 2);
        agenteMap.put("Pó Químico - A/B/C", 2);
        agenteMap.put("Pó Químico - D", 2);
        agenteMap.put("Espuma - A/B/C", 4);
        agenteMap.put("Acetato de Potássio - K", 5);

        classeFogoMap.put("Água - A/B/C", Arrays.asList(1, 2, 3));
        classeFogoMap.put("Gás Carbônico - B/C", Arrays.asList(2, 3));
        classeFogoMap.put("Pó Químico - B/C", Arrays.asList(2, 3));
        classeFogoMap.put("Pó Químico - A/B/C", Arrays.asList(1, 2, 3));
        classeFogoMap.put("Pó Químico - D", Arrays.asList(4));
        classeFogoMap.put("Espuma - A/B/C", Arrays.asList(1, 2, 3));
        classeFogoMap.put("Acetato de Potássio - K", Arrays.asList(5));

        TipoSensorModel sensorFumacaOBJ = new TipoSensorModel();
        sensorFumacaOBJ.setId(1);
        sensorFumacaOBJ.setDescSensor("Fumaça");
        tipoSensorMap.put("Fumaça", sensorFumacaOBJ);

        TipoSensorModel sensorTempOBJ = new TipoSensorModel();
        sensorTempOBJ.setId(2);
        sensorTempOBJ.setDescSensor("Temperatura");
        tipoSensorMap.put("Temperatura", sensorTempOBJ);

        TipoSensorModel sensorMovOBJ = new TipoSensorModel();
        sensorMovOBJ.setId(3);
        sensorMovOBJ.setDescSensor("Movimento");
        tipoSensorMap.put("Movimento", sensorMovOBJ);

        TipoSensorModel sensorChaOBJ = new TipoSensorModel();
        sensorChaOBJ.setId(4);
        sensorChaOBJ.setDescSensor("Chama");
        tipoSensorMap.put("Chama", sensorChaOBJ);


        // ---- Mostrar/esconder campos por tipo ----
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                camposAlarme.setVisibility(View.GONE);
                camposExtintor.setVisibility(View.GONE);
                camposHidrante.setVisibility(View.GONE);
                switch (position) {
                    case 0:
                        camposAlarme.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        camposExtintor.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        camposHidrante.setVisibility(View.VISIBLE);
                        break;
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
