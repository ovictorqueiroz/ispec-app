package br.com.etecia.ispec_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import br.com.etecia.ispec_app.model.LocalizacaoModel;
import br.com.etecia.ispec_app.model.TipoSensorModel;
import br.com.etecia.ispec_app.repository.LocalizacaoListCallback;
import br.com.etecia.ispec_app.repository.LocalizacaoRepository;
import br.com.etecia.ispec_app.requests.EquipamentoRequest;
import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroEquipamentoActivity extends AppCompatActivity {

    // Extras recebidos de EquipamentoActivity (quando vindo do fluxo cliente > equipamentos)
    public static final String EXTRA_CLIENTE_ID   = "clienteId";
    public static final String EXTRA_CLIENTE_NOME = "clienteNome";

    // Header
    private ImageView arrowLeft;

    // Datas formatadas para a API
    private String dataInstalacaoFormatada;
    private String dataValidadeFormatada;
    private String ultimaVerificacaoFormatada;

    // Campos comuns
    private EditText edtIdCliente, edtNome, edtDataInstalacao;
    private Spinner spinnerStatus, spinnerTipo, spinnerLocalizacao;
    private TextView txtVIdCliente, txtSemLocalizacoes;
    private LinearLayout campoLocalizacaoContainer;

    // Lista de localizações carregadas do backend
    private List<LocalizacaoModel> listaLocalizacoes = new ArrayList<>();

    // clienteId pré-preenchido (quando vindo de EquipamentoActivity)
    private Long clienteIdPreenchido = null;

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
        new DatePickerDialog(
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
        ).show();
    }

    // ----------------------------------------------------------------
    // Carrega localizações do cliente no Spinner
    // ----------------------------------------------------------------
    private void carregarLocalizacoes(Long clienteId) {
        txtSemLocalizacoes.setVisibility(View.GONE);
        spinnerLocalizacao.setEnabled(false);

        LocalizacaoRepository repo = new LocalizacaoRepository(getApplicationContext());
        repo.listarPorCliente(clienteId, new LocalizacaoListCallback() {
            @Override
            public void onSucesso(List<LocalizacaoModel> localizacoes) {
                listaLocalizacoes = localizacoes;
                spinnerLocalizacao.setEnabled(true);

                if (localizacoes.isEmpty()) {
                    txtSemLocalizacoes.setVisibility(View.VISIBLE);
                    spinnerLocalizacao.setAdapter(null);
                    return;
                }

                List<String> labels = localizacoes.stream()
                        .map(l -> formatarLocalizacao(l))
                        .collect(Collectors.toList());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CadastroEquipamentoActivity.this,
                        android.R.layout.simple_spinner_item,
                        labels
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLocalizacao.setAdapter(adapter);
            }

            @Override
            public void onErro(String mensagem) {
                txtSemLocalizacoes.setText("Erro ao carregar localizações");
                txtSemLocalizacoes.setVisibility(View.VISIBLE);
                Log.e("LOCALIZACAO", mensagem);
            }
        });
    }

    private String formatarLocalizacao(LocalizacaoModel loc) {
        StringBuilder sb = new StringBuilder();
        if (loc.getBloco() != null && !loc.getBloco().isEmpty()) sb.append("Bloco ").append(loc.getBloco());
        if (loc.getAndar() != null && !loc.getAndar().isEmpty()) {
            if (sb.length() > 0) sb.append(" | ");
            sb.append("Andar ").append(loc.getAndar());
        }
        if (loc.getSala() != null && !loc.getSala().isEmpty()) {
            if (sb.length() > 0) sb.append(" | ");
            sb.append("Sala ").append(loc.getSala());
        }
        if (sb.length() == 0) sb.append("Localização #").append(loc.getId());
        return sb.toString();
    }

    // ----------------------------------------------------------------
    // Validação dos campos comuns
    // ----------------------------------------------------------------
    private boolean camposComumPreenchidos() {
        // clienteId: pode vir pré-preenchido ou digitado
        Long clienteId = getClienteId();
        if (clienteId == null) {
            if (clienteIdPreenchido == null) {
                edtIdCliente.setError("Informe o código do cliente");
                edtIdCliente.requestFocus();
            }
            return false;
        }
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError("Informe o nome do equipamento");
            edtNome.requestFocus();
            return false;
        }
        if (listaLocalizacoes.isEmpty() || spinnerLocalizacao.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Selecione uma localização", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataInstalacaoFormatada == null || dataInstalacaoFormatada.isEmpty()) {
            edtDataInstalacao.setError("Informe a data de instalação");
            edtDataInstalacao.requestFocus();
            return false;
        }
        return true;
    }

    // Retorna o clienteId seja ele pré-preenchido ou digitado no campo
    private Long getClienteId() {
        if (clienteIdPreenchido != null) return clienteIdPreenchido;
        String txt = edtIdCliente.getText().toString().trim();
        if (txt.isEmpty()) return null;
        try { return Long.parseLong(txt); } catch (NumberFormatException e) { return null; }
    }

    // ----------------------------------------------------------------
    // Monta o EquipamentoRequest
    // ----------------------------------------------------------------
    private EquipamentoRequest montarRequest() {
        EquipamentoRequest req = new EquipamentoRequest();

        req.setCliente(new EquipamentoRequest.IdWrapper(getClienteId()));
        req.setNome(edtNome.getText().toString().trim());

        // Localização vem do Spinner
        LocalizacaoModel locSelecionada = listaLocalizacoes.get(spinnerLocalizacao.getSelectedItemPosition());
        req.setLocalizacao(new EquipamentoRequest.IdWrapper(locSelecionada.getId()));

        req.setDataInstalacao(dataInstalacaoFormatada.trim());
        req.setStatus(spinnerStatus.getSelectedItem().toString());

        String tipoStr = spinnerTipo.getSelectedItem().toString();
        req.setTipo(tipoStr);
        EquipamentoRequest.IdWrapper tipoWrapper;

        switch (tipoStr) {
            case "Extintor":
                tipoWrapper = new EquipamentoRequest.IdWrapper(1);
                List<Integer> ids = classeFogoMap.get(spinnerClasseFogo.getSelectedItem().toString());
                req.setClasseFogo(ids.stream().map(EquipamentoRequest.IdWrapper::new).collect(Collectors.toList()));
                req.setAgente(new EquipamentoRequest.IdWrapper(agenteMap.get(spinnerClasseFogo.getSelectedItem().toString())));
                String capStr = edtCapacidade.getText().toString().trim();
                if (!capStr.isEmpty()) req.setCapacidade(Double.parseDouble(capStr));
                req.setDataValidade(dataValidadeFormatada != null ? dataValidadeFormatada.trim() : null);
                String pressaoStr = edtPressao.getText().toString().trim();
                if (!pressaoStr.isEmpty()) req.setPressao(Double.parseDouble(pressaoStr));
                break;

            case "Alarme":
                tipoWrapper = new EquipamentoRequest.IdWrapper(2);
                req.setTipoSensor(tipoSensorMap.get(spinnerTipoSensor.getSelectedItem().toString()));
                req.setUltimaVerificacao(ultimaVerificacaoFormatada != null ? ultimaVerificacaoFormatada.trim() : null);
                req.setFuncionando(switchFuncionando.isChecked());
                break;

            case "Hidrante":
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
        Log.d("testeCadastro", new Gson().toJson(req));
        return req;
    }

    private void limparFormulario() {
        if (clienteIdPreenchido == null) {
            edtIdCliente.setText("");
            txtVIdCliente.setVisibility(View.GONE);
            listaLocalizacoes.clear();
            spinnerLocalizacao.setAdapter(null);
        }
        edtNome.setText("");
        edtDataInstalacao.setText("");
        dataInstalacaoFormatada = null;
        spinnerStatus.setSelection(0);
        spinnerTipo.setSelection(0);
        edtCapacidade.setText("");
        edtDataValidade.setText("");
        edtPressao.setText("");
        dataValidadeFormatada = null;
        spinnerClasseFogo.setSelection(0);
        edtUltimaVerificacao.setText("");
        ultimaVerificacaoFormatada = null;
        switchFuncionando.setChecked(false);
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
        btnCadastrar.setEnabled(false);

        api.cadastrarEquipamento(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnCadastrar.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroEquipamentoActivity.this,
                            "Equipamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    limparFormulario();
                } else {
                    String errorMsg = "Erro desconhecido";
                    try { errorMsg = response.errorBody().string(); } catch (IOException e) { errorMsg = e.getMessage(); }
                    Log.e("ERRO_CADASTRO", errorMsg);
                    Toast.makeText(CadastroEquipamentoActivity.this,
                            "Erro ao cadastrar: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnCadastrar.setEnabled(true);
                Toast.makeText(CadastroEquipamentoActivity.this,
                        "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

        // Verifica se veio com clienteId pré-preenchido (fluxo: Clientes > Equipamentos > Cadastrar)
        long extraId = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1L);
        if (extraId != -1L) {
            clienteIdPreenchido = extraId;
        }

        // Header — botão voltar
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> {
            if (clienteIdPreenchido != null) {
                // Volta para EquipamentoActivity mantendo o contexto do cliente
                Intent intent = new Intent(getApplicationContext(), EquipamentoActivity.class);
                intent.putExtra(EquipamentoActivity.EXTRA_CLIENTE_ID, clienteIdPreenchido);
                intent.putExtra(EquipamentoActivity.EXTRA_CLIENTE_NOME,
                        getIntent().getStringExtra(EXTRA_CLIENTE_NOME));
                startActivity(intent);
            } else {
                startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
            }
            finish();
        });

        // Campos comuns
        edtIdCliente    = findViewById(R.id.edtIdCliente);
        txtVIdCliente   = findViewById(R.id.txtVIdCliente);
        edtNome         = findViewById(R.id.edtNome);
        edtDataInstalacao = findViewById(R.id.edtDataInstalacao);
        spinnerStatus   = findViewById(R.id.spinnerStatus);
        spinnerTipo     = findViewById(R.id.spinnerTipo);

        // Campo localização agora é um Spinner
        spinnerLocalizacao        = findViewById(R.id.spinnerLocalizacao);
        txtSemLocalizacoes        = findViewById(R.id.txtSemLocalizacoes);
        campoLocalizacaoContainer = findViewById(R.id.campoLocalizacaoContainer);

        // Campos Extintor
        camposExtintor   = findViewById(R.id.camposExtintor);
        spinnerClasseFogo = findViewById(R.id.spinnerClasseFogo);
        edtCapacidade    = findViewById(R.id.edtCapacidade);
        edtDataValidade  = findViewById(R.id.edtDataValidade);
        edtPressao       = findViewById(R.id.edtPressao);

        // Campos Alarme
        camposAlarme        = findViewById(R.id.camposAlarme);
        spinnerTipoSensor   = findViewById(R.id.spinnerTipoSensor);
        edtUltimaVerificacao = findViewById(R.id.edtUltimaVerificacao);
        switchFuncionando   = findViewById(R.id.switchFuncionando);

        // Campos Hidrante
        camposHidrante   = findViewById(R.id.camposHidrante);
        edtPressaoAgua   = findViewById(R.id.edtPressaoAgua);
        edtCompMangueira = findViewById(R.id.edtCompMangueira);
        switchDisponivel = findViewById(R.id.switchDisponivel);

        btnCadastrar = findViewById(R.id.btnCadastrar);

        // ── Se clienteId foi pré-preenchido: oculta o campo de texto, mostra nome, carrega localizações ──
        if (clienteIdPreenchido != null) {
            edtIdCliente.setVisibility(View.GONE);
            txtVIdCliente.setVisibility(View.VISIBLE);
            String clienteNome = getIntent().getStringExtra(EXTRA_CLIENTE_NOME);
            txtVIdCliente.setText(clienteNome != null ? clienteNome : "Cliente #" + clienteIdPreenchido);
            txtVIdCliente.setTextColor(Color.rgb(125, 149, 85));
            carregarLocalizacoes(clienteIdPreenchido);
        } else {
            // Modo normal: usuário digita o clienteId e o spinner carrega ao sair do campo
            edtIdCliente.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    String txt = edtIdCliente.getText().toString().trim();
                    if (!txt.isEmpty()) {
                        try {
                            Long id = Long.parseLong(txt);
                            buscarClienteECarregarLocalizacoes(id);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            });
        }

        // ── Spinners estáticos ──
        String[] statusEquipamento = {"ATIVO", "INATIVO"};
        String[] tipoEquipamento   = {"Alarme", "Extintor", "Hidrante"};
        String[] classeFogo        = {"Água - A/B/C", "Gás Carbônico - B/C", "Pó Químico - B/C",
                "Pó Químico - A/B/C", "Pó Químico - D", "Espuma - A/B/C", "Acetato de Potássio - K"};
        String[] tipoSensor        = {"Fumaça", "Temperatura", "Movimento", "Chama"};

        ArrayAdapter<String> adp;

        adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoEquipamento);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adp);

        adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusEquipamento);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adp);

        adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classeFogo);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasseFogo.setAdapter(adp);

        adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoSensor);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSensor.setAdapter(adp);

        // Mapas Extintor
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

        // Mapa TipoSensor
        String[][] sensores = {{"Fumaça","1"}, {"Temperatura","2"}, {"Movimento","3"}, {"Chama","4"}};
        for (String[] s : sensores) {
            TipoSensorModel m = new TipoSensorModel();
            m.setId(Integer.parseInt(s[1]));
            m.setDescSensor(s[0]);
            tipoSensorMap.put(s[0], m);
        }

        // Mostrar/esconder campos por tipo
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

        // DatePickers
        edtDataInstalacao.setOnClickListener(v -> datePicker(edtDataInstalacao));
        edtDataValidade.setOnClickListener(v -> datePicker(edtDataValidade));
        edtUltimaVerificacao.setOnClickListener(v -> datePicker(edtUltimaVerificacao));

        // Botão Cadastrar
        btnCadastrar.setOnClickListener(v -> cadastrarEquipamento());
    }

    // Busca nome do cliente e já dispara o carregamento das localizações
    private void buscarClienteECarregarLocalizacoes(Long id) {
        txtVIdCliente.setVisibility(View.VISIBLE);
        txtVIdCliente.setText("Buscando...");
        txtVIdCliente.setTextColor(Color.rgb(179, 177, 177));

        ApiService api = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        api.buscarCliente(id).enqueue(new Callback<br.com.etecia.ispec_app.model.ClienteModel>() {
            @Override
            public void onResponse(Call<br.com.etecia.ispec_app.model.ClienteModel> call,
                                   Response<br.com.etecia.ispec_app.model.ClienteModel> response) {
                if (response.isSuccessful()) {
                    txtVIdCliente.setText(response.body().getRazaoSocial());
                    txtVIdCliente.setTextColor(Color.rgb(125, 149, 85));
                    carregarLocalizacoes(id);
                } else {
                    txtVIdCliente.setText("Cliente não encontrado");
                    txtVIdCliente.setTextColor(Color.rgb(163, 29, 29));
                    listaLocalizacoes.clear();
                    spinnerLocalizacao.setAdapter(null);
                }
            }
            @Override
            public void onFailure(Call<br.com.etecia.ispec_app.model.ClienteModel> call, Throwable t) {
                txtVIdCliente.setText("Erro de conexão");
                txtVIdCliente.setTextColor(Color.rgb(163, 29, 29));
            }
        });
    }
}
