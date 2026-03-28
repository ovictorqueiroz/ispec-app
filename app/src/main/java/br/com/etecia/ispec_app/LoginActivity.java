package br.com.etecia.ispec_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextView txtRecupera;
    TextInputEditText txtEmail, txtSenha;
    MaterialButton btnEntrar;
    boolean termosAceitos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtRecupera = findViewById(R.id.txtRecupera);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);

        txtRecupera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecSenhaActivity.class));
                finish();
            }
        });

        showTermsOfUseDialog();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!termosAceitos){
                    Toast.makeText(getApplicationContext(),
                            "Você precisa aceitar os Termos de Uso para continuar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = txtEmail.getText().toString().trim();
                String senha = txtSenha.getText().toString().trim();

                if(email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
                    finish();
                }
            }
        });

    }
    private void showTermsOfUseDialog() {
        String termos = "Termos de Uso da iSpec\n\n" +
                "Este aplicativo coleta dados de cadastro e localização do usuário para inspeção de produtos de incêndio.\n\n" +
                "Você deve aceitar estes termos para usar o aplicativo.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Termos de Uso");
        builder.setMessage(termos);
        builder.setCancelable(false);

        builder.setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                termosAceitos = true;
                Toast.makeText(LoginActivity.this, "Termos aceitos.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Recusar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this,
                        "Você precisa aceitar os termos para usar o app.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}