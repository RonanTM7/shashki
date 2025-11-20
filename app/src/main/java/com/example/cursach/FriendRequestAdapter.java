package com.example.cursach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private List<User> requestList;
    private OnRequestInteractionListener listener;

    public interface OnRequestInteractionListener {
        void onAcceptRequest(int position);
        void onDeclineRequest(int position);
    }

    public FriendRequestAdapter(List<User> requestList, OnRequestInteractionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        User user = requestList.get(position);
        holder.textViewUserName.setText(user.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        Button buttonAccept;
        Button buttonDecline;

        FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            buttonDecline = itemView.findViewById(R.id.buttonDecline);

            buttonAccept.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAcceptRequest(position);
                    }
                }
            });

            buttonDecline.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeclineRequest(position);
                    }
                }
            });
        }
    }
}
