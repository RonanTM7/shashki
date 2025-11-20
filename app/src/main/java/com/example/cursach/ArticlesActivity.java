package com.example.cursach;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> onBackPressed());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewArticles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("articles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Article> articles = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    articles.add(document.toObject(Article.class));
                }
                ArticleAdapter adapter = new ArticleAdapter(this, articles);
                recyclerView.setAdapter(adapter);
            }
        });
    }

}
