package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        TextView usernameTextView = findViewById(R.id.textViewUsername);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                usernameTextView.setText(displayName);
            } else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("users").document(currentUser.getUid());
                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        usernameTextView.setText(username);
                    } else {
                        usernameTextView.setText(currentUser.getEmail());
                    }
                });
            }
        }

        TextView friendsTextView = findViewById(R.id.textViewFriends);
        friendsTextView.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FriendsListActivity.class);
            startActivity(intent);
        });

        TextView achievementsTextView = findViewById(R.id.textViewAchievements);
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("userProgress").document(currentUser.getUid()).collection("unlockedAchievements").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int achievementsCount = queryDocumentSnapshots.size();
                        achievementsTextView.setText(getString(R.string.achievements_label, achievementsCount));
                    });
        }


        Button logoutButton = findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

}
