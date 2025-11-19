package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfirmNewPasswordActivity extends AppCompatActivity {

    private EditText editTextConfirmPassword;
    private TextView textViewError;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_new_password);

        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        textViewError = findViewById(R.id.textViewError);
        newPassword = getIntent().getStringExtra("newPassword");

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        Button nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(v -> confirmPassword());
    }

    private void confirmPassword() {
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (!confirmPassword.equals(newPassword)) {
            textViewError.setText(R.string.passwords_do_not_match);
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ConfirmNewPasswordActivity.this, R.string.password_updated_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmNewPasswordActivity.this, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ConfirmNewPasswordActivity.this, R.string.failed_to_update_password, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
