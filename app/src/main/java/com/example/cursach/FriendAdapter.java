package com.example.cursach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<User> friendList;

    public FriendAdapter(List<User> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User friend = friendList.get(position);
        holder.textViewFriendName.setText(friend.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriendName;

        FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFriendName = itemView.findViewById(R.id.textViewFriendName);
        }
    }
}
