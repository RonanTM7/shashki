package com.example.cursach;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RulesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        TextView rulesContent = findViewById(R.id.textViewRulesContent);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("rules").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SpannableStringBuilder rulesText = new SpannableStringBuilder();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    String text = document.getString("text");

                    if (title != null && !title.isEmpty()) {
                        int start = rulesText.length();
                        rulesText.append(title);
                        rulesText.setSpan(new StyleSpan(Typeface.BOLD), start, rulesText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rulesText.append("\n");
                    }

                    if (text != null) {
                        rulesText.append(text).append("\n\n");
                    }
                }
                rulesContent.setText(rulesText);
            }
        });
    }

}
