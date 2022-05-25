package com.example.easybook_airlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Admin_menu extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CardView add,update,show,delete,request;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_menu);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigationview);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        add=findViewById(R.id.add_card);
        update=findViewById(R.id.update_card);
        show=findViewById(R.id.show_card);
        delete=findViewById(R.id.delete_card);
        request=findViewById(R.id.request_card);
        add.setOnClickListener(this);
        update.setOnClickListener(this);
        show.setOnClickListener(this);
        delete.setOnClickListener(this);
        request.setOnClickListener(this);
        //delete hostory
        delteUserHitory();

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.home_menu){
                    Toast.makeText(getApplicationContext(),"Home Panel open",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if(id==R.id.user_info){
                    Intent intent=new Intent(getApplicationContext(),User_list_recyclerView.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if(id==R.id.admin_logout){
                    Toast.makeText(getApplicationContext(),"Admin Has logout",Toast.LENGTH_SHORT).show();
                    // drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent=new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {


        int id=v.getId();
        if(id==R.id.add_card){
             i=new Intent(this,AddFlight.class);startActivity(i);
        }
        else if(id==R.id.update_card){
            i=new Intent(this,updateFlight.class);startActivity(i);
        }
        else if(id==R.id.show_card){
            i=new Intent(this,Important.class);startActivity(i);
        }
        else if(id==R.id.delete_card){
            AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
            View dialogView= LayoutInflater.from(v.getContext()).inflate(R.layout.cancel_flight,null);
            EditText deleteFlightid=dialogView.findViewById(R.id.deleteFlightId);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.setTitle("Cancel Flight ?");

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String s=deleteFlightid.getText().toString().trim();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Flightid").child(s);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){
                                        snapshot.getRef().removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
        else if(id==R.id.request_card){
            FirebaseDatabase.getInstance().getReference("Request")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!=null){
                                i=new Intent(getApplicationContext(),Admin_Notification.class);startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(),"No New Notification Exist",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



        }

    }

    void delteUserHitory(){
        DatabaseReference df=FirebaseDatabase.getInstance().getReference("USER");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot cd:snapshot.getChildren()){
                String x=cd.getKey();
                long cutoff=new Date().getTime()- TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS);
                Date  dd=new Date(cutoff);
                SimpleDateFormat dateFormat=new SimpleDateFormat("d/M/yyyy");
                String oldDate=dateFormat.format(new Date(cutoff));
                //Toast.makeText(Admin_menu.this,oldDate,Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(),oldDate,Toast.LENGTH_SHORT).show();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("History").child(x);
                ref.orderByChild("acceptDate").endAt(cutoff)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dc : snapshot.getChildren()){
                                    dc.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("EXIT");
        builder.setMessage("Click LOGOUT To Exit.")
                .setCancelable(false)
                .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Admin Has logout",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Admin_menu.this,Login.class);
                        startActivity(intent);
                        finish();
                        Admin_menu.super.onBackPressed();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}