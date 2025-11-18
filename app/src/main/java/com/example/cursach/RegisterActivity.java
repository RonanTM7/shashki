package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewUsernameError, textViewEmailError;
    private ImageButton togglePasswordVisibility;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewUsernameError = findViewById(R.id.textViewUsernameError);
        textViewEmailError = findViewById(R.id.textViewEmailError);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        buttonRegister.setOnClickListener(v -> registerUser());

        togglePasswordVisibility.setOnClickListener(v -> {
            if (editTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            editTextPassword.setSelection(editTextPassword.length());
        });
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        textViewUsernameError.setVisibility(View.GONE);
        textViewEmailError.setVisibility(View.GONE);

        if (TextUtils.isEmpty(username)) {
            textViewUsernameError.setText("Имя пользователя не может быть пустым");
            textViewUsernameError.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textViewEmailError.setText("Введите корректный email");
            textViewEmailError.setVisibility(View.VISIBLE);
            return;
        }

        if (password.length() < 6 || password.length() > 16) {
            editTextPassword.setError("Пароль должен быть от 6 до 16 символов");
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Пароли не совпадают");
            return;
        }

        checkUsernameAndEmailAndRegister(username, email, password);
    }

    private void checkUsernameAndEmailAndRegister(final String username, final String email, final String password) {
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null && !snapshot.isEmpty()) {
                            textViewUsernameError.setText("Имя пользователя уже занято");
                            textViewUsernameError.setVisibility(View.VISIBLE);
                        } else {
                            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(authTask -> {
                                if (authTask.isSuccessful()) {
                                    boolean isNewUser = authTask.getResult().getSignInMethods().isEmpty();
                                    if (isNewUser) {
                                        createUser(email, password, username);
                                    } else {
                                        textViewEmailError.setText("Почта уже занята");
                                        textViewEmailError.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Ошибка проверки почты.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка проверки имени пользователя.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("email", email);

                        db.collection("users").document(userId).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "Ошибка сохранения данных.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            textViewEmailError.setText("Почта уже занята");
                            textViewEmailError.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Ошибка регистрации.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
