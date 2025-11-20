package com.example.cursach;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class FriendsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());
    }
}
