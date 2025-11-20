package com.example.cursach;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

public class ProgressActivity extends AppCompatActivity {
    private TextView timeSpentTextView;
    private AchievementManager achievementManager;
    private long totalTimeInMillis = 0;
    private long sessionStartTime = 0;
    private final Handler timerHandler = new Handler();
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = totalTimeInMillis + (System.currentTimeMillis() - sessionStartTime);
            long minutes = millis / (1000 * 60);
            timeSpentTextView.setText(String.valueOf(minutes));
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        timeSpentTextView = findViewById(R.id.textViewTime);
        achievementManager = AchievementManager.getInstance();

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        loadUserProgress();
    }

    private void loadUserProgress() {
        DocumentReference userProgressRef = achievementManager.getUserProgressRef();
        if (userProgressRef != null) {
            userProgressRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserProgress userProgress = documentSnapshot.toObject(UserProgress.class);
                    if (userProgress != null) {
                        totalTimeInMillis = userProgress.getTimeSpentInApp();
                        sessionStartTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (totalTimeInMillis > 0) { // Start timer only after initial load
            sessionStartTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        long sessionTime = System.currentTimeMillis() - sessionStartTime;
        totalTimeInMillis += sessionTime;

        DocumentReference userProgressRef = achievementManager.getUserProgressRef();
        if (userProgressRef != null) {
            userProgressRef.update("timeSpentInApp", FieldValue.increment(sessionTime))
                    .addOnSuccessListener(aVoid -> {
                        achievementManager.checkTimeAchievements(this, totalTimeInMillis / (1000 * 60));
                    });
        }
    }
}
