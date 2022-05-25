package com.example.easybook_airlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {

    EditText adminEmail,adminPassword;
    Button adminLogin;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        String s1="admin@gmail.com";
        String s2="admin";

        adminEmail=findViewById(R.id.adminemail);
        adminPassword=findViewById(R.id.adminpassword);
        adminLogin=findViewById(R.id.adminlogin);
        progressBar=findViewById(R.id.adminLoginProgress);

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String email=adminEmail.getText().toString().trim();
                    String password=adminPassword.getText().toString().trim();
                    progressBar.setVisibility(View.VISIBLE);

                    if(s1.equals(email) && s2.equals(password)){
                        Intent intet=new Intent(AdminLogin.this,Admin_menu.class);
                        startActivity(intet);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else {
                        return;
                    }


            }
        });

    }
}