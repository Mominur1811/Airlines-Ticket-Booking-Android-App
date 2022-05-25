package com.example.easybook_airlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Notification extends AppCompatActivity {

    RecyclerView recyclerview;
    NotificationAdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin__notification);

        recyclerview=(RecyclerView) findViewById(R.id.RecyclerNotificationAdmin);
      //  recyclerview.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

// And set it to RecyclerView
        recyclerview.setLayoutManager(mLayoutManager);
        FirebaseRecyclerOptions<Passenger> options =
                new FirebaseRecyclerOptions.Builder<Passenger>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Request"),Passenger.class)
                        .build();
        adapter=new NotificationAdminAdapter(options);
        recyclerview.setAdapter(adapter);

        //
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