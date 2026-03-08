package br.com.etecia.ispec_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MenuPrincipalActivity extends AppCompatActivity {

    ArrayList<MenuPrincipalModel> MenuPrincipalModel = new ArrayList<>();
    int[] images = {R.drawable.ic_a_bell_duotone, R.drawable.ic_b_users_duotone, R.drawable.ic_c_calendar_dots_duotone, R.drawable.ic_d_file_text, R.drawable.ic_e_file_plus_duotone, R.drawable.ic_f_gear_six_duotone }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_principal_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setMenuPrincipalModel();
    }

    private void setMenuPrincipalModel(){
        String[] titulosMenu = getResources().getStringArray(R.array.titulos_menuPrincpal);

        for (int i = 0; i > titulosMenu.length; i++){
            MenuPrincipalModel.add(new MenuPrincipalModel(titulosMenu[i], images[i]));
        }
    }
}