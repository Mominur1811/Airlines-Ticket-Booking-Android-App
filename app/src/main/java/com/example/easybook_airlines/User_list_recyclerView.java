package com.example.easybook_airlines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class User_list_recyclerView extends AppCompatActivity {
    RecyclerView recyclerview;
    adapterUser adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_list_recycler_view);
        recyclerview=(RecyclerView) findViewById(R.id.recyclerview4);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<USER> options = new FirebaseRecyclerOptions.Builder<USER>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("USER"),USER.class).build();
        adapter=new adapterUser(options);
        recyclerview.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}