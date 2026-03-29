package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CadastroEquipamentoActivity extends AppCompatActivity {
    //Seta Voltar
    private ImageView arrowLeft;
    // Campos comuns
    private EditText edtNome, edtLocalizacao, edtDataInstalacao, etEdtIdCliente;

    private EditText edtIdCliente;

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

        //Botão de voltar para o menu
        arrowLeft  = findViewById(R.id.arrowLeft);

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
                finish();
            }
        });

        // Campos Comuns
        edtIdCliente = findViewById(R.id.edtIdCliente);
        edtDataInstalacao = findViewById(R.id.edtDataInstalacao);
        edtLocalizacao = findViewById(R.id.edtLocalizacao);
        edtNome = findViewById(R.id.edtNome);
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

        //Populando os Spinners com dados
        String[] statusEquipamento = {"Ativo", "Inativo"}; //Array do estado do equipamento
        String[] tipoEquipamento = {"Alarme", "Extintor", "Hidrante"}; // Array dos tipos de Equipamento
        String[] tipoFogo = {"Água - A/B/C", "Gás Carbônico - B/C", "Pó Químico - B/C", "Pó Químico - A/B/C", "Pó Químico - D", "Espuma - A/B/C", "Acetato de Potássio - K"}; //Array dos tipos de Extintor
        String[] tipoSensor = {"Temperatura", "Movimento", "Fumaça"}; //Array dos tipos de Alarme

        ArrayAdapter<String> adapter;

        //Spinner tipoEquipamento
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoEquipamento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        //Spinner statusEquipamento
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,statusEquipamento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        //Spinner tipoFogo
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tipoFogo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasseFogo.setAdapter(adapter);

        //Spinner tipoSensor
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tipoSensor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSensor.setAdapter(adapter);

        //Escondendo as views com base no tipo
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                camposAlarme.setVisibility(View.GONE);
                camposExtintor.setVisibility(View.GONE);
                camposHidrante.setVisibility(View.GONE);

                switch (position){
                    case 0: camposAlarme.setVisibility(View.VISIBLE);
                    break;
                    case 1: camposExtintor.setVisibility(View.VISIBLE);
                    break;
                    case 2: camposHidrante.setVisibility(View.VISIBLE);
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

    }
}