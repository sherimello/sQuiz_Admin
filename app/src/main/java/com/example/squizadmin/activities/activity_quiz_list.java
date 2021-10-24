package com.example.squizadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.squizadmin.R;
import com.example.squizadmin.adapters.Quiz_list_adapter;

public class activity_quiz_list extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity_quiz_list.this));
        recyclerView.setAdapter(new Quiz_list_adapter());
        recyclerView.setHasFixedSize(true);

    }
}