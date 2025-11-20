package com.example.cursach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long startTime;
    private AchievementManager achievementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Not signed in, launch the Login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Button buttonAchievements = findViewById(R.id.buttonAchievements);
        Button buttonProgress = findViewById(R.id.buttonProgress);
        Button buttonProfile = findViewById(R.id.buttonProfile);
        Button buttonRules = findViewById(R.id.buttonRules);
        Button buttonLegends = findViewById(R.id.buttonLegends);
        Button buttonArticles = findViewById(R.id.buttonArticles);
        Button buttonSettings = findViewById(R.id.buttonSettings);
        Button buttonFriends = findViewById(R.id.buttonFriends);
        Button buttonFriendRequests = findViewById(R.id.buttonFriendRequests);

        buttonAchievements.setOnClickListener(this);
        buttonProgress.setOnClickListener(this);
        buttonProfile.setOnClickListener(this);
        buttonRules.setOnClickListener(this);
        buttonLegends.setOnClickListener(this);
        buttonArticles.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
        buttonFriends.setOnClickListener(this);
        buttonFriendRequests.setOnClickListener(this);

        achievementManager = AchievementManager.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;

        DocumentReference userProgressRef = achievementManager.getUserProgressRef();
        if (userProgressRef != null) {
            userProgressRef.update("timeSpentInApp", FieldValue.increment(timeSpent))
                    .addOnSuccessListener(aVoid -> {
                        userProgressRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                UserProgress userProgress = documentSnapshot.toObject(UserProgress.class);
                                if (userProgress != null) {
                                    achievementManager.checkTimeAchievements(this, userProgress.getTimeSpentInApp() / (1000 * 60));
                                }
                            }
                        });
                    });
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
        if (id == R.id.buttonAchievements) {
            intent = new Intent(this, AchievementsActivity.class);
        } else if (id == R.id.buttonProgress) {
            intent = new Intent(this, ProgressActivity.class);
        } else if (id == R.id.buttonProfile) {
            intent = new Intent(this, ProfileActivity.class);
        } else if (id == R.id.buttonRules) {
            intent = new Intent(this, RulesActivity.class);
        } else if (id == R.id.buttonLegends) {
            intent = new Intent(this, LegendsActivity.class);
        } else if (id == R.id.buttonArticles) {
            intent = new Intent(this, ArticlesActivity.class);
        } else if (id == R.id.buttonSettings) {
            intent = new Intent(this, SettingsActivity.class);
        } else if (id == R.id.buttonFriends) {
            intent = new Intent(this, FriendsActivity.class);
        } else if (id == R.id.buttonFriendRequests) {
            intent = new Intent(this, FriendRequestsActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}