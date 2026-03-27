package br.com.etecia.ispec_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CadastroEquipamentoActivity extends AppCompatActivity {
    // Campos comuns
    private EditText edtNome, edtLocalizacao, edtDataInstalacao;
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
    }
}