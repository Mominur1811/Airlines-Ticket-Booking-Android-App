package com.example.easybook_airlines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class User_purchase_History extends AppCompatActivity {
    RecyclerView recyclerview;
    historyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_purchase__history);
        recyclerview=(RecyclerView) findViewById(R.id.recyclerview5);
       // recyclerview.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

// And set it to RecyclerView
        recyclerview.setLayoutManager(mLayoutManager);

        String key= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseRecyclerOptions<UserNotificationRetreiver> options =
                new FirebaseRecyclerOptions.Builder<UserNotificationRetreiver>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("History").child(key),UserNotificationRetreiver.class)
                        .build();
        adapter=new historyAdapter(options);
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