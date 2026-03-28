package br.com.etecia.ispec_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class NvSenhaActivity extends AppCompatActivity {


    MaterialButton btnEnviarNV;

    TextInputEditText txtConfSenha, txtSenhaNV;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nv_senha_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnEnviarNV = findViewById(R.id.btnEnviarNV);
        txtConfSenha = findViewById(R.id.txtConfSenha);
        txtSenhaNV = findViewById(R.id.txtSenhaNV);

        btnEnviarNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String senha = txtSenhaNV.getText().toString();
                String csenha = txtConfSenha.getText().toString();

                if (senha.equals(csenha) && !senha.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(NvSenhaActivity.this);
                    builder.setTitle("Sistema de criação de senha.");
                    builder.setMessage("Senha criada com sucesso!!!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(getApplicationContext(),
                                    "Senha criada com sucesso.", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}