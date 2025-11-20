package com.example.cursach;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements UserAdapter.OnSendRequestClickListener {

    private EditText editTextFriend;
    private Button buttonFindFriend;
    private Button buttonFriendsList;
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private UserProgress currentUserProgress;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        editTextFriend = findViewById(R.id.editTextFriend);
        buttonFindFriend = findViewById(R.id.buttonFindFriend);
        buttonFriendsList = findViewById(R.id.buttonFriendsList);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userList = new ArrayList<>();

        loadCurrentUserProgressAndSetupAdapter();

        buttonFindFriend.setOnClickListener(v -> findFriend());
        buttonFriendsList.setOnClickListener(v -> {
            startActivity(new android.content.Intent(FriendsActivity.this, FriendsListActivity.class));
        });
    }

    private void loadCurrentUserProgressAndSetupAdapter() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("userProgress").document(currentUserId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserProgress = task.getResult().toObject(UserProgress.class);
                        setupRecyclerView();
                    }
                });
    }

    private void setupRecyclerView() {
        userAdapter = new UserAdapter(userList, this, currentUserProgress);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);
    }

    private void findFriend() {
        String friendUsername = editTextFriend.getText().toString().trim();
        if (friendUsername.isEmpty()) {
            Toast.makeText(this, "Введите имя пользователя", Toast.LENGTH_SHORT).show();
            return;
        }

        userList.clear();
        db.collection("users")
                .whereEqualTo("username", friendUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!document.getId().equals(currentUserId)) {
                                User user = new User(document.getId(), document.getString("username"));
                                userList.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FriendsActivity.this, "Ошибка поиска", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onSendRequestClick(int position) {
        User targetUser = userList.get(position);
        String currentUserId = mAuth.getCurrentUser().getUid();

        db.collection("userProgress").document(currentUserId)
                .update("friendRequestsSent", com.google.firebase.firestore.FieldValue.arrayUnion(targetUser.getUserId()))
                .addOnSuccessListener(aVoid -> {
                    db.collection("userProgress").document(targetUser.getUserId())
                            .update("friendRequestsReceived", com.google.firebase.firestore.FieldValue.arrayUnion(currentUserId))
                            .addOnSuccessListener(aVoid1 -> {
                                if (currentUserProgress != null) {
                                    currentUserProgress.getFriendRequestsSent().add(targetUser.getUserId());
                                    userAdapter.updateUserProgress(currentUserProgress);
                                }
                                Toast.makeText(this, "Запрос отправлен", Toast.LENGTH_SHORT).show();
                            });
                });
    }
}
