package com.example.cursach;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AchievementsActivity extends AppCompatActivity {
    private AchievementAdapter adapter;
    private List<Achievement> achievementList;
    private TextView achievementCount;
    private AchievementManager achievementManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAchievements);
        achievementCount = findViewById(R.id.textViewAchievementCount);
        achievementManager = AchievementManager.getInstance();

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        achievementList = new ArrayList<>();
        adapter = new AchievementAdapter(achievementList);
        recyclerView.setAdapter(adapter);

        loadAchievements();
    }

    private void loadAchievements() {
        List<Achievement> allAchievements = achievementManager.getAllAchievements();
        achievementManager.getUserProgressRef().collection("unlockedAchievements").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> unlockedAchievementIds = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        unlockedAchievementIds.add(document.getId());
                    }

                    int unlockedCount = 0;
                    for (Achievement achievement : allAchievements) {
                        if (unlockedAchievementIds.contains(achievement.getId())) {
                            achievement.setUnlocked(true);
                            unlockedCount++;
                        } else {
                            achievement.setUnlocked(false);
                        }
                        achievementList.add(achievement);
                    }

                    achievementCount.setText(String.valueOf(unlockedCount));
                    adapter.notifyDataSetChanged();
                });
    }
}
