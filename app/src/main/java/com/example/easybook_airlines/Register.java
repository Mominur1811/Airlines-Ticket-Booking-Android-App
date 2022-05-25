package com.example.easybook_airlines;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    EditText efullname,egmail,epassword,date;
    Button eregister;
    FirebaseAuth fauth;
    TextView already_regigter;
    ProgressBar progressBar;
    RadioGroup radioGroup;
    RadioButton genderButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        efullname=findViewById(R.id.efullname);
        egmail=findViewById(R.id.egmail);
        epassword=findViewById(R.id.epassword);
        eregister=findViewById(R.id.eregister);
        already_regigter=findViewById(R.id.already_registered);
        date=findViewById(R.id.dateBirth);
        progressBar=findViewById(R.id.registerProgress);
        radioGroup=findViewById(R.id.radioGroup);
        fauth=FirebaseAuth.getInstance();

       // if(fauth.getCurrentUser()!=null){
         //   startActivity(new Intent(getApplicationContext(),SearchTicket.class));
           // finish();
        //   }
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
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
        eregister.setOnClickListener(v -> {
            int id=radioGroup.getCheckedRadioButtonId();
            genderButton=findViewById(id);
            String gender;
            if(radioGroup.getCheckedRadioButtonId()==-1){
                Toast.makeText(getApplicationContext(),"Gender is not selected",Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                gender=genderButton.getText().toString().trim();

            }
            String fname=efullname.getText().toString().trim();
            String birth=date.getText().toString().trim();
            String gmail=egmail.getText().toString().trim();
            String password=epassword.getText().toString().trim();

            if(TextUtils.isEmpty(fname)){
                efullname.setError("Full name is required");
                return;
            }
            if(TextUtils.isEmpty(birth)){
                date.setError("Date is required");
                return;
            }
            if(TextUtils.isEmpty(gmail)){
                egmail.setError("Gmail is required");
                return;
            }
            if(TextUtils.isEmpty(password)){
                epassword.setError("Password is required");
                return;
            }
            if(password.length()<8){
                epassword.setError("Password must be 8 characters");
                return;
            }


            progressBar.setVisibility(View.VISIBLE);

            fauth.createUserWithEmailAndPassword(gmail,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                    String key=fauth.getUid();
                    FirebaseDatabase.getInstance().getReference("USER").child(key).setValue(new USER(fname,gmail,gender,birth));
                    startActivity(new Intent(getApplicationContext(),SearchTicket.class));
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    Toast.makeText(Register.this,"Failed to create new account!. Check the email address.",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        });

        already_regigter.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(getApplicationContext(),Login.class));
        });
    }
}
