package com.example.easybook_airlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    EditText lgmail,lpassword;
    Button llogin;
    FirebaseAuth fauth;
    TextView new_register,forgetpass;
    ProgressBar progressBar;
    RadioGroup radioGroup;
    //RadioButton radioButton1,radioButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_ticket);
        setContentView(R.layout.activity_login);
        lgmail=findViewById(R.id.lGmail);
        lpassword=findViewById(R.id.lPassword);
        llogin=findViewById(R.id.login);
        forgetpass=findViewById(R.id.reset);
        new_register=findViewById(R.id.new_register);
        progressBar=findViewById(R.id.loginProgess);
        radioGroup=findViewById(R.id.radioGroup2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.adminRadio){
                    new_register.setVisibility(View.INVISIBLE);
                    forgetpass.setVisibility(View.INVISIBLE);
                }
                else{
                    new_register.setVisibility(View.VISIBLE);
                    forgetpass.setVisibility(View.VISIBLE);
                }
            }
        });

        fauth=FirebaseAuth.getInstance();

        llogin.setOnClickListener(v -> {
            int id=radioGroup.getCheckedRadioButtonId();
            String gmail=lgmail.getText().toString().trim();
            String password=lpassword.getText().toString().trim();
            if(id==R.id.adminRadio){

                if(gmail.equals("admin@gmail.com") && password.equals("admin")){
                    Intent intet=new Intent(getApplicationContext(),Admin_menu.class);
                    startActivity(intet);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong Email or Password",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(id==R.id.userRadio) {

                if (TextUtils.isEmpty(gmail)) {
                    lgmail.setError("Gmail is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    lpassword.setError("Password is required");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fauth.signInWithEmailAndPassword(gmail, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Succesfully Logged In", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SearchTicket.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        lgmail.setText("");
                        lpassword.setText("");
                    } else {
                        Toast.makeText(Login.this, "Wrong password or email", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
            lgmail.setText("");
            lpassword.setText("");
        });

        new_register.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"Registration Page",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),Register.class);
            startActivity(intent);
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reset = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordResetDialog.setView(reset);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = reset.getText().toString().trim();
                        fauth.sendPasswordResetEmail(mail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Login.this, "Sent Reset Password Link To Your Email", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Invalid Email ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.show();
            }
        });
    }
}