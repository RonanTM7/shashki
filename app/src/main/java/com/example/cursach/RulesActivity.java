package com.example.cursach;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RulesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView rulesContent = findViewById(R.id.textViewRulesContent);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("rules").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                StringBuilder rulesText = new StringBuilder();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    rulesText.append(document.getString("text")).append("\n\n");
                }
                rulesContent.setText(rulesText.toString());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}