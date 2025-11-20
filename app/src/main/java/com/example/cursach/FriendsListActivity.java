package com.example.cursach;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFriends;
    private FriendAdapter friendAdapter;
    private List<User> friendList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        recyclerViewFriends = findViewById(R.id.recyclerViewFriends);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(friendList);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFriends.setAdapter(friendAdapter);

        loadFriends();
    }

    private void loadFriends() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("userProgress").document(currentUserId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> friendIds = (List<String>) document.get("friends");
                            if (friendIds != null && !friendIds.isEmpty()) {
                                db.collection("users").whereIn(com.google.firebase.firestore.FieldPath.documentId(), friendIds)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (DocumentSnapshot userDocument : task1.getResult()) {
                                                    User user = new User(userDocument.getId(), userDocument.getString("username"));
                                                    friendList.add(user);
                                                }
                                                friendAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
