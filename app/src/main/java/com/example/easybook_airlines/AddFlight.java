package com.example.easybook_airlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddFlight extends AppCompatActivity {

    EditText flightId,source,destination,numberOfSeats,date,price,time;
    Button add;
    DatePickerDialog.OnDateSetListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_flight);

        flightId=findViewById(R.id.flightid);
        source=findViewById(R.id.source);
        destination=findViewById(R.id.destination);
        numberOfSeats=findViewById(R.id.noseats);
        date=findViewById(R.id.date);
        price=findViewById(R.id.price);
        add=findViewById(R.id.add);
        time=findViewById(R.id.time);

        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(AddFlight.this, new DatePickerDialog.OnDateSetListener() {
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=flightId.getText().toString().trim();
                String s2=source.getText().toString().toUpperCase().trim();
                String s3=destination.getText().toString().toUpperCase().trim();
                String s4=numberOfSeats.getText().toString().trim();
                String s6=price.getText().toString().trim();
                String s5=date.getText().toString().trim();
                String s8=time.getText().toString().trim();
                if(TextUtils.isEmpty(s1)){
                    flightId.setError("ID is required");
                    return;
                }
                if(TextUtils.isEmpty(s2)){
                    source.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s3)){
                    destination.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s4)){
                    numberOfSeats.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s5)){
                    date.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s6)){
                    price.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s8)){
                    time.setError("This field is required");
                    return;
                }


                String s7=s2+s3+s5;

                Post obj=new Post(s1,s2,s3,s4,s5,s6,s7,s8);
               DatabaseReference db=FirebaseDatabase.getInstance().getReference("Flightid");
               db.child(s1).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.getValue()==null){
                           db.child(s1).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(getApplicationContext(),"Successfully Added.",Toast.LENGTH_SHORT).show();
                                   flightId.setText("");
                                   source.setText("");
                                   destination.setText("");
                                   numberOfSeats.setText("");
                                   date.setText("");
                                   price.setText("");
                                   time.setText("");
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(getApplicationContext(),"Failed Yo Add.",Toast.LENGTH_SHORT).show();

                               }
                           });
                       }
                       else{
                           Toast.makeText(getApplicationContext(),"A Flight is already registered to this Id. Try a new one .",Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


            }
        });
    }
}