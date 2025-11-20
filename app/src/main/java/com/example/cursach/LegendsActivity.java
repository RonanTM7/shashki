package com.example.cursach;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LegendsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legends);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLegends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("legends").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Legend> legends = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Legend legend = document.toObject(Legend.class);
                    legend.setId(document.getId());
                    legends.add(legend);
                }
                LegendAdapter adapter = new LegendAdapter(this, legends);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
