package br.com.etecia.ispec_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;

import br.com.etecia.ispec_app.service.ApiService;
import br.com.etecia.ispec_app.service.RetrofitClient;
import br.com.etecia.ispec_app.requests.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView txtRecupera;
    TextInputEditText txtEmail, txtSenha;
    MaterialButton btnEntrar;
    boolean termosAceitos = false;

    private LoginRequest montarRequest() {
        LoginRequest req = new LoginRequest();

        req.setEmail(txtEmail.getText().toString().trim());
        req.setSenha(txtSenha.getText().toString().trim());
        return req;
    }

    ;

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

        txtRecupera.setOnClickListener(new View.OnClickListener() {
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

                if (!termosAceitos) {
                    Toast.makeText(getApplicationContext(),
                            "Você precisa aceitar os Termos de Uso para continuar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = txtEmail.getText().toString().trim();
                String senha = txtSenha.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    LoginRequest request = montarRequest();
                    ApiService api = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
                    Call<String> call = api.autenticaUsuario(request);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(
                                        LoginActivity.this,
                                        "Usuário autenticado com sucesso!",
                                        Toast.LENGTH_SHORT
                                ).show();
                                // Pega o token que a API retornou
                                String token = response.body();

                                // Abre o SharedPreferences com o nome "sessao"
                                SharedPreferences prefs = getSharedPreferences("sessao", MODE_PRIVATE);

                                // Cria um editor para escrever
                                SharedPreferences.Editor editor = prefs.edit();

                                // Salva o token com a chave "token"
                                editor.putString("token", token);
                                editor.apply(); // Salva de forma assíncrona
                                startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
                                finish();
                            } else {
                                String errorMsg = "Erro desconhecido";
                                try {
                                    errorMsg = response.errorBody().string();
                                    Log.e("ERRO_AUTH", errorMsg);
                                } catch (IOException e) {
                                    errorMsg = e.getMessage();
                                }
                                Toast.makeText(
                                        LoginActivity.this,
                                        "Erro ao Autenticar: " + errorMsg,
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERRO_CONEXAO", "Falha na conexão", t);
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Falha na conexão: " + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

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