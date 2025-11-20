package com.example.cursach;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsActivity extends AppCompatActivity implements FriendRequestAdapter.OnRequestInteractionListener {

    private RecyclerView recyclerViewFriendRequests;
    private FriendRequestAdapter friendRequestAdapter;
    private List<User> requestList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        recyclerViewFriendRequests = findViewById(R.id.recyclerViewFriendRequests);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        requestList = new ArrayList<>();
        friendRequestAdapter = new FriendRequestAdapter(requestList, this);
        recyclerViewFriendRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFriendRequests.setAdapter(friendRequestAdapter);

        loadFriendRequests();
    }

    private void loadFriendRequests() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("userProgress").document(currentUserId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> receivedRequests = (List<String>) document.get("friendRequestsReceived");
                            if (receivedRequests != null && !receivedRequests.isEmpty()) {
                                db.collection("users").whereIn(com.google.firebase.firestore.FieldPath.documentId(), receivedRequests)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (DocumentSnapshot userDocument : task1.getResult()) {
                                                    User user = new User(userDocument.getId(), userDocument.getString("username"));
                                                    requestList.add(user);
                                                }
                                                friendRequestAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public void onAcceptRequest(int position) {
        User sender = requestList.get(position);
        String currentUserId = mAuth.getCurrentUser().getUid();

        // Add to friends for both users
        db.collection("userProgress").document(currentUserId)
                .update("friends", com.google.firebase.firestore.FieldValue.arrayUnion(sender.getUserId()));
        db.collection("userProgress").document(sender.getUserId())
                .update("friends", com.google.firebase.firestore.FieldValue.arrayUnion(currentUserId));

        // Remove from requests
        db.collection("userProgress").document(currentUserId)
                .update("friendRequestsReceived", com.google.firebase.firestore.FieldValue.arrayRemove(sender.getUserId()));
        db.collection("userProgress").document(sender.getUserId())
                .update("friendRequestsSent", com.google.firebase.firestore.FieldValue.arrayRemove(currentUserId));

        requestList.remove(position);
        friendRequestAdapter.notifyItemRemoved(position);

        AchievementManager.getInstance().checkFirstFriendAchievement(this);

        Toast.makeText(this, "Друг добавлен", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineRequest(int position) {
        User sender = requestList.get(position);
        String currentUserId = mAuth.getCurrentUser().getUid();

        // Remove from requests
        db.collection("userProgress").document(currentUserId)
                .update("friendRequestsReceived", com.google.firebase.firestore.FieldValue.arrayRemove(sender.getUserId()));
        db.collection("userProgress").document(sender.getUserId())
                .update("friendRequestsSent", com.google.firebase.firestore.FieldValue.arrayRemove(currentUserId));

        requestList.remove(position);
        friendRequestAdapter.notifyItemRemoved(position);

        Toast.makeText(this, "Запрос отклонен", Toast.LENGTH_SHORT).show();
    }
}
