package com.example.cursach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private final List<Achievement> achievements;

    public AchievementAdapter(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        holder.achievementName.setText(achievement.getName());
        holder.achievementDescription.setText(achievement.getDescription());
        holder.itemView.setAlpha(achievement.isUnlocked() ? 1.0f : 0.5f);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView achievementName;
        TextView achievementDescription;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            achievementName = itemView.findViewById(R.id.achievementName);
            achievementDescription = itemView.findViewById(R.id.achievementDescription);
        }
    }
}
