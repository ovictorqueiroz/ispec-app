package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.etecia.ispec_app.repository.LocalizacaoCallback;
import br.com.etecia.ispec_app.repository.LocalizacaoRepository;
import br.com.etecia.ispec_app.model.LocalizacaoModel;
import br.com.etecia.ispec_app.requests.LocalizacaoRequest;

public class CadastroLocalizacaoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    public static final String EXTRA_CLIENTE_ID   = "clienteId";
    public static final String EXTRA_CLIENTE_NOME = "clienteNome";

    private ImageView arrowLeft;
    private EditText edtBloco, edtAndar, edtSala, edtDescricao;
    private Button btnSalvar;

    private Long clienteId;
    private String clienteNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cadastro_localizacao_layout);
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

        // Recebe o clienteId e nome passados por EquipamentoActivity
        clienteId   = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1L);
        clienteNome = getIntent().getStringExtra(EXTRA_CLIENTE_NOME);

        if (clienteId == -1L) {
            Toast.makeText(this, "Cliente não identificado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Header
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(v -> voltarParaEquipamentos());

        TextView txtNomeCliente = findViewById(R.id.txtNomeCliente);
        txtNomeCliente.setText(clienteNome != null ? clienteNome : "Cliente #" + clienteId);

        // Campos
        edtBloco     = findViewById(R.id.edtBloco);
        edtAndar     = findViewById(R.id.edtAndar);
        edtSala      = findViewById(R.id.edtSala);
        edtDescricao = findViewById(R.id.edtDescricao);
        btnSalvar    = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(v -> salvarLocalizacao());
    }

    private void salvarLocalizacao() {
        String bloco     = edtBloco.getText().toString().trim();
        String andar     = edtAndar.getText().toString().trim();
        String sala      = edtSala.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();

        // Pelo menos um campo de identificação deve ser preenchido
        if (bloco.isEmpty() && andar.isEmpty() && sala.isEmpty()) {
            Toast.makeText(this, "Preencha ao menos Bloco, Andar ou Sala.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSalvar.setEnabled(false);

        LocalizacaoRequest request = new LocalizacaoRequest(
                clienteId,
                bloco.isEmpty()     ? null : bloco,
                andar.isEmpty()     ? null : andar,
                sala.isEmpty()      ? null : sala,
                descricao.isEmpty() ? null : descricao
        );

        LocalizacaoRepository repo = new LocalizacaoRepository(getApplicationContext());
        repo.criarLocalizacao(request, new LocalizacaoCallback() {
            @Override
            public void onSucesso(LocalizacaoModel localizacao) {
                btnSalvar.setEnabled(true);
                Toast.makeText(CadastroLocalizacaoActivity.this,
                        "Localização adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                voltarParaEquipamentos();
            }

            @Override
            public void onErro(String mensagem) {
                btnSalvar.setEnabled(true);
                Toast.makeText(CadastroLocalizacaoActivity.this,
                        "Erro ao salvar: " + mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void voltarParaEquipamentos() {
        Intent intent = new Intent(this, EquipamentoActivity.class);
        intent.putExtra(EquipamentoActivity.EXTRA_CLIENTE_ID,   clienteId);
        intent.putExtra(EquipamentoActivity.EXTRA_CLIENTE_NOME, clienteNome);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        voltarParaEquipamentos();
    }
}
