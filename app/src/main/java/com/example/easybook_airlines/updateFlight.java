package com.example.easybook_airlines;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class updateFlight extends AppCompatActivity {

    EditText id,src,des,seat,price,date,time;
    Button load;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_flight);
        id=findViewById(R.id.updateFLightId);
        src=findViewById(R.id.updateFlightSource);
        des=findViewById(R.id.updateFlightDestination);
        seat=findViewById(R.id.updateFlightSeat);
        price=findViewById(R.id.updateFlightPrice);
        date=findViewById(R.id.updateFlightDate);
        time=findViewById(R.id.updateTime);
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog=new DatePickerDialog(updateFlight.this, (view, year1, month1, dayOfMonth) -> {
                month1 = month1 +1;
                String d=dayOfMonth+"/"+ month1 +"/"+ year1;
                date.setText(d);
            },year,month,day);
            datePickerDialog.show();
        });

        load=findViewById(R.id.updateFlightLoad);
        confirm=findViewById(R.id.confirmFlightUpdate);

        load.setOnClickListener(v -> {
            String flightid=id.getText().toString().trim();
            FirebaseDatabase.getInstance().getReference("Flightid").child(flightid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()==null){
                                Toast.makeText(updateFlight.this,"No Flight Registered To This ID",Toast.LENGTH_SHORT).show();
                            }
                            else  {
                                Post user = snapshot.getValue(Post.class);
                                Toast.makeText(updateFlight.this, "Data Loaded", Toast.LENGTH_SHORT).show();
                                assert user != null;
                                src.setText(user.from);
                                des.setText(user.to);
                                seat.setText(user.seat);
                                price.setText(user.price);
                                date.setText(user.arrivalDate);
                                time.setText((user.time));

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {


                        }
                    });

        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=id.getText().toString().trim();
                String s1=src.getText().toString().toUpperCase().trim();
                String s2=des.getText().toString().toUpperCase().trim();
                String s3=seat.getText().toString().trim();
                String s4=price.getText().toString().trim();
                String s5=date.getText().toString().trim();
                String s7=time.getText().toString().trim();
                if(TextUtils.isEmpty(s1)){
                    id.setError("ID is required");
                    return;
                }
                if(TextUtils.isEmpty(s1)){
                    src.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s2)){
                    des.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s3)){
                    seat.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s5)){
                    date.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s4)){
                    price.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(s7)){
                    time.setError("This field is required");
                    return;
                }
                String s6=s1+s2+s5;
                Post x=new Post(key,s1,s2,s3,s5,s4,s6,s7);
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Flightid").child(key);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null){
                            ref.setValue(x)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(updateFlight.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                                            id.setText("");
                                            src.setText("");
                                            des.setText("");
                                            seat.setText("");
                                            date.setText("");
                                            price.setText("");
                                            time.setText("");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(updateFlight.this,"Failed To Updated",Toast.LENGTH_SHORT).show();
                                }
                            });
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