package com.example.cursach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnSendRequestClickListener listener;
    private UserProgress currentUserProgress;

    public void updateUserProgress(UserProgress currentUserProgress) {
        this.currentUserProgress = currentUserProgress;
        notifyDataSetChanged();
    }

    public interface OnSendRequestClickListener {
        void onSendRequestClick(int position);
    }

    public UserAdapter(List<User> userList, OnSendRequestClickListener listener, UserProgress currentUserProgress) {
        this.userList = userList;
        this.listener = listener;
        this.currentUserProgress = currentUserProgress;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewUserName.setText(user.getDisplayName());

        if (currentUserProgress != null) {
            if (currentUserProgress.getFriends().contains(user.getUserId())) {
                holder.buttonSendRequest.setText("Друзья");
                holder.buttonSendRequest.setEnabled(false);
            } else if (currentUserProgress.getFriendRequestsSent().contains(user.getUserId())) {
                holder.buttonSendRequest.setText("Запрос отправлен");
                holder.buttonSendRequest.setEnabled(false);
            } else {
                holder.buttonSendRequest.setText("Отправить запрос");
                holder.buttonSendRequest.setEnabled(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        Button buttonSendRequest;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            buttonSendRequest = itemView.findViewById(R.id.buttonSendRequest);

            buttonSendRequest.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onSendRequestClick(position);
                    }
                }
            });
        }
    }
}
