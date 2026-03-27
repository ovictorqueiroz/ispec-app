package br.com.etecia.ispec_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class CodSenhaActivity extends AppCompatActivity {

    MaterialButton btnEnviarCO;

    EditText cod1, cod2, cod3, cod4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cod_senha_layout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnEnviarCO = findViewById(R.id.btnEnviarCO);

        cod1 = findViewById(R.id.cod1);
        cod2 = findViewById(R.id.cod2);
        cod3 = findViewById(R.id.cod3);
        cod4 = findViewById(R.id.cod4);

        setupOTP();

        btnEnviarCO.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), NvSenhaActivity.class));
            finish();
        });
    }


    private void setupOTP() {


        cod1.addTextChangedListener(new GenericTextWatcher(cod1, cod2));
        cod2.addTextChangedListener(new GenericTextWatcher(cod2, cod3));
        cod3.addTextChangedListener(new GenericTextWatcher(cod3, cod4));
        cod4.addTextChangedListener(new GenericTextWatcher(cod4, null));


        cod2.setOnKeyListener(new GenericKeyEvent(cod2, cod1));
        cod3.setOnKeyListener(new GenericKeyEvent(cod3, cod2));
        cod4.setOnKeyListener(new GenericKeyEvent(cod4, cod3));
    }


    public class GenericTextWatcher implements TextWatcher {

        private EditText currentView;
        private EditText nextView;

        public GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    public class GenericKeyEvent implements View.OnKeyListener {

        private EditText currentView;
        private EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_DEL &&
                    currentView.getText().toString().isEmpty()) {

                previousView.requestFocus();
                return true;
            }
            return false;
        }
    }
}