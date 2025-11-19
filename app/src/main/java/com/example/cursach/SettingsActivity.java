package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView emailTextView = findViewById(R.id.textViewEmail);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            emailTextView.setText("почта: " + currentUser.getEmail());
        }

        TextView passwordTextView = findViewById(R.id.textViewPassword);
        passwordTextView.setText("пароль: password");

        Button changePasswordButton = findViewById(R.id.buttonChangePassword);
        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, EnterCurrentPasswordActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
