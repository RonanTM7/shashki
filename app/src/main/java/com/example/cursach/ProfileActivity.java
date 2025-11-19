package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView usernameTextView = findViewById(R.id.textViewUsername);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName == null || displayName.isEmpty()) {
                usernameTextView.setText(currentUser.getEmail());
            } else {
                usernameTextView.setText(displayName);
            }
        }

        TextView friendsTextView = findViewById(R.id.textViewFriends);
        friendsTextView.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FriendsListActivity.class);
            startActivity(intent);
        });


        Button logoutButton = findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
