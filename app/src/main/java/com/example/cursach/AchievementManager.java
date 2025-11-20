package com.example.cursach;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {

    private static AchievementManager instance;
    private List<Achievement> allAchievements;
    private final FirebaseFirestore db;
    private final FirebaseUser currentUser;

    private static final String USER_PROGRESS_COLLECTION = "userProgress";
    private static final String UNLOCKED_ACHIEVEMENTS_COLLECTION = "unlockedAchievements";

    private AchievementManager() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        initializeAchievements();
    }

    public static synchronized AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    private void initializeAchievements() {
        allAchievements = new ArrayList<>();
        allAchievements.add(new Achievement("time_10", "Вы провели здесь 10 минут", "Провести в приложении 10 минут"));
        allAchievements.add(new Achievement("time_20", "Вы провели здесь 20 минут", "Провести в приложении 20 минут"));
        allAchievements.add(new Achievement("time_30", "Вы провели здесь 30 минут", "Провести в приложении 30 минут"));
        allAchievements.add(new Achievement("time_60", "Вы провели здесь 60 минут", "Провести в приложении 60 минут"));
        allAchievements.add(new Achievement("time_100", "Вы провели здесь 100 минут", "Провести в приложении 100 минут"));
        allAchievements.add(new Achievement("read_rules", "Подкованный игрок", "Прочитать правила"));
        allAchievements.add(new Achievement("read_5_legends", "Начинающий историк", "Прочитать 5 легенд"));
        allAchievements.add(new Achievement("read_9_legends", "Хранитель легенд", "Прочитать 9 легенд"));
        allAchievements.add(new Achievement("first_friend", "Новый друг", "Добавить первого друга"));
    }

    public List<Achievement> getAllAchievements() {
        return allAchievements;
    }

    public DocumentReference getUserProgressRef() {
        if (currentUser != null) {
            return db.collection(USER_PROGRESS_COLLECTION).document(currentUser.getUid());
        }
        return null;
    }

    public void unlockAchievement(Context context, String achievementId) {
        if (currentUser == null) {
            return;
        }

        DocumentReference achievementRef = getUserProgressRef().collection(UNLOCKED_ACHIEVEMENTS_COLLECTION).document(achievementId);
        achievementRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                allAchievements.stream().filter(achievement -> achievement.getId().equals(achievementId)).findFirst().ifPresent(achievementToUnlock -> achievementRef.set(achievementToUnlock)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Достижение разблокировано: " + achievementToUnlock.getName(), Toast.LENGTH_SHORT).show();
                        }));

            }
        });
    }

    public void checkTimeAchievements(Context context, long timeSpentInMinutes) {
        if (timeSpentInMinutes >= 10) {
            unlockAchievement(context, "time_10");
        }
        if (timeSpentInMinutes >= 20) {
            unlockAchievement(context, "time_20");
        }
        if (timeSpentInMinutes >= 30) {
            unlockAchievement(context, "time_30");
        }
        if (timeSpentInMinutes >= 60) {
            unlockAchievement(context, "time_60");
        }
        if (timeSpentInMinutes >= 100) {
            unlockAchievement(context, "time_100");
        }
    }

    public void checkLegendsAchievements(Context context, long legendsReadCount) {
        if (legendsReadCount >= 5) {
            unlockAchievement(context, "read_5_legends");
        }
        if (legendsReadCount >= 9) {
            unlockAchievement(context, "read_9_legends");
        }
    }

    public void checkRulesAchievement(Context context) {
        unlockAchievement(context, "read_rules");
    }

    public void checkFirstFriendAchievement(Context context) {
        if (currentUser == null) {
            return;
        }

        getUserProgressRef().get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                UserProgress userProgress = documentSnapshot.toObject(UserProgress.class);
                if (userProgress != null && userProgress.getFriends().size() == 1) {
                    unlockAchievement(context, "first_friend");
                }
            }
        });
    }
}
