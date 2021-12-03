package com.example.squizadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.example.squizadmin.R;
import com.example.squizadmin.adapters.Quiz_list_adapter;
import com.example.squizadmin.classes.QuizData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class activity_quiz_list extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab_add_quiz;
    private CardView card_loading;
    private ArrayList<QuizData> quizData;
    private String userNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        quizData = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        fab_add_quiz = findViewById(R.id.fab_add_quiz);
        card_loading = findViewById(R.id.card_loading);

        Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.continuous_rotation);
        rotation.setInterpolator(new LinearInterpolator());
        card_loading.startAnimation(rotation);

        getQuizDataForUser();

        fab_add_quiz.setOnClickListener(this);

    }

    private void getQuizDataForUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userNode = Objects.requireNonNull(Objects.requireNonNull(currentUser).getEmail()).substring(0, Objects.requireNonNull(currentUser.getEmail()).length() -10);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userNode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    quizData.add(new QuizData(dataSnapshot.getKey(), Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(activity_quiz_list.this));
                recyclerView.setAdapter(new Quiz_list_adapter(quizData));
                recyclerView.setHasFixedSize(true);
                card_loading.clearAnimation();
                card_loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}