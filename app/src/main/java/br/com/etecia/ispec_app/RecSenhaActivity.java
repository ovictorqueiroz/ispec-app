package br.com.etecia.ispec_app;

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

public class RecSenhaActivity extends AppCompatActivity {

    MaterialButton btnEnviarR;

    TextInputEditText txtEmailR;

    TextView txtVlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rec_senha_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtEmailR = findViewById(R.id.txtEmailR);
        txtVlogin = findViewById(R.id.txtVlogin);
        btnEnviarR = findViewById(R.id.btnEnviarR);

        btnEnviarR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String remail = txtEmailR.getText().toString().trim();

                if(remail.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else {

                    startActivity(new Intent(getApplicationContext(), CodSenhaActivity.class));
                    finish();
                }
            }
        });
        txtVlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}