package com.example.cursach;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LegendDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_detail);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        String legendId = getIntent().getStringExtra("legend_id");
        if (legendId == null) {
            finish();
            return;
        }

        TextView legendName = findViewById(R.id.textViewLegendName);
        TextView legendDescription = findViewById(R.id.textViewLegendDescription);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("legends").document(legendId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Legend legend = documentSnapshot.toObject(Legend.class);
                legendName.setText(legend.getName());
                String description = legend.getDescription();
                if (description != null) {
                    legendDescription.setText(description.replace("\\n", "\n"));
                }
            }
        });
    }

}
