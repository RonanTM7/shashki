package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EnterNewPasswordActivity extends AppCompatActivity {

    private EditText editTextNewPassword;
    private TextView textViewError;
    private String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_password);

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        textViewError = findViewById(R.id.textViewError);
        currentPassword = getIntent().getStringExtra("currentPassword");

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        Button nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(v -> validatePassword());
    }

    private void validatePassword() {
        String newPassword = editTextNewPassword.getText().toString();

        if (newPassword.isEmpty()) {
            textViewError.setText("Password is required");
            return;
        }

        if (newPassword.equals(currentPassword)) {
            textViewError.setText(R.string.new_password_different_message);
            return;
        }

        Intent intent = new Intent(EnterNewPasswordActivity.this, ConfirmNewPasswordActivity.class);
        intent.putExtra("newPassword", newPassword);
        startActivity(intent);
    }
}
