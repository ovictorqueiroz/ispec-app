import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class MenuPrincipalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Seus dados (grade com 6 itens)
        List<String> dados = Arrays.asList("Item 1", "Item 2", "Item 3",
                "Item 4", "Item 5", "Item 6");

        // Cria adapter e configura grade (2 colunas)
        RecyclerView.Adapter adapter = new RecyclerView.Adapter(dados) {
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}
