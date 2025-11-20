package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class EnterCurrentPasswordActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_current_password);

        editTextPassword = findViewById(R.id.editTextPassword);
        textViewError = findViewById(R.id.textViewError);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        Button nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(v -> verifyPassword());

        TextView forgotPasswordTextView = findViewById(R.id.textViewForgotPassword);
        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(EnterCurrentPasswordActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void verifyPassword() {
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(password)) {
            textViewError.setText(R.string.incorrect_password);
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), password);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(EnterCurrentPasswordActivity.this, EnterNewPasswordActivity.class);
                        intent.putExtra("currentPassword", password);
                        startActivity(intent);
                    } else {
                        textViewError.setText(R.string.incorrect_password);
                    }
                }
            });
        }
    }
}
