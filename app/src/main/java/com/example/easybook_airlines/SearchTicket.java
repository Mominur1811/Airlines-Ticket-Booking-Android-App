package com.example.easybook_airlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SearchTicket extends AppCompatActivity {

    EditText source,destination,date;
    Button search,clickToBook;
    FirebaseAuth mauth;
    ImageView notification,logout;
    TextView handle,notificationCount;
    int ticket_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_ticket);

        source=findViewById(R.id.userSource);
        destination=findViewById(R.id.userDestination);
        date=findViewById(R.id.userDate);
        search=findViewById(R.id.userSearch);
        logout=findViewById(R.id.logout);
        mauth=FirebaseAuth.getInstance();
        clickToBook=findViewById(R.id.clickToBook);
        notification=findViewById(R.id.userNotificationButton);
        handle=findViewById(R.id.userHandle);
        notificationCount=findViewById(R.id.notification_count);
        //fetch notification
        fetchNotificationCount();
        //delete user history of 90 days
       // deleteUserHistoryForNdays();


        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(SearchTicket.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        String d=dayOfMonth+"/"+month+"/"+year;
                        date.setText(d);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        String x=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("USER").child(x)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        USER user=snapshot.getValue(USER.class);
                        assert user != null;
                        handle.setText(user.fullname);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = source.getText().toString().toUpperCase().trim();
                String to = destination.getText().toString().toUpperCase().trim();
                String arrival = date.getText().toString().trim();
                String query = from + to + arrival;
                String valid_until = arrival;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                Calendar c= Calendar.getInstance();
                String dt=sdf.format(c.getTime());
                Date strDate = null;
                try {
                    strDate = sdf.parse(valid_until);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert strDate != null;
                //Toast.makeText(getApplicationContext(),dt,Toast.LENGTH_SHORT).show();
                if (dt.equals(arrival) || System.currentTimeMillis()<strDate.getTime()) {
                    FirebaseDatabase.getInstance().getReference("Flightid").orderByChild("query").equalTo(query)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Intent intent = new Intent(getApplicationContext(), userRecycler.class);
                                        intent.putExtra("searching", query);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                } else {

                    Toast.makeText(getApplicationContext(), "Date has been passed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SearchTicket.this);
                builder.setTitle("EXIT");
                builder.setMessage("Click LOGOUT To Logout.")
                        .setCancelable(false)
                        .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mauth.signOut();
                                Intent intent=new Intent(getApplicationContext(),Login.class);
                                Toast.makeText(SearchTicket.this,"Logged Out",Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
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
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("NotificationCount").child(uid).removeValue();

                FirebaseDatabase.getInstance().getReference("UserNotification").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue()!=null){
                                    Intent intent=new Intent(getApplicationContext(),User_Notification.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(),"You have no notification to show",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                fetchNotificationCount();

            }
        });
        clickToBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseDatabase.getInstance().getReference("History").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue()!=null){
                                    Intent intent=new Intent(getApplicationContext(),User_purchase_History.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(),"You have no notification to show",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("EXIT");
        builder.setMessage("Click LOGOUT To Exit.")
                .setCancelable(false)
                .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mauth.signOut();
                        finish();
                        SearchTicket.super.onBackPressed();
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

    void fetchNotificationCount(){
        String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("NotificationCount").child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    String c;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null){
                            c=snapshot.getValue(String.class);

                        }
                        else{
                            c="0";
                        }
                        notificationCount.setText(c);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}