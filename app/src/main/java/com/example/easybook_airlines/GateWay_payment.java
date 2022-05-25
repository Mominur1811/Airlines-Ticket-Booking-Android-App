package com.example.easybook_airlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener;

public class GateWay_payment extends AppCompatActivity implements SSLCTransactionResponseListener {

    EditText acc,pay;
    TextView typ,message;
    Button buy;
    int money;
    String key;
    Passenger user;
    SSLCommerzInitialization sslCommerzInitialization;
    SSLCCustomerInfoInitializer customerInfoInitializer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_way_payment);

        acc=findViewById(R.id.gateway_account);
        pay=findViewById(R.id.gateway_amount);
        typ=findViewById(R.id.transaction_type);
        buy=findViewById(R.id.getwayPay);
        user=(Passenger) getIntent().getSerializableExtra("Id1");
        acc.setText(user.getAccount());
        pay.setText(user.getAmount());
        message=findViewById(R.id.adminMessage);
        money= Integer.parseInt(user.getAmount());
        acc.setEnabled(false);
        pay.setEnabled(false);
        //key= FirebaseDatabase.getInstance().getReference("Request").push().getKey();;


        sslCommerzInitialization = new SSLCommerzInitialization("easyb60ccadfcb3a3d","easyb60ccadfcb3a3d@ssl", money,
                SSLCCurrencyType.BDT,"123456789098765",
                "Aeroplane Ticket", SSLCSdkType.TESTBOX);
        SSLCCustomerInfoInitializer customerInfoInitializer = new SSLCCustomerInfoInitializer("Sourov", "sourovsourov74@gmail.com",
                "address", "dhaka", "1214", "Bangladesh", "01980655515");
        // final SSLCProductInitializer productInitializer = new SSLCProductInitializer ("food", "food",
        //   new SSLCProductInitializer.ProductProfile.TravelVertical("Travel", "10",
        //        "A", "12", "Dhk-Syl"));
        // final SSLCShipmentInfoInitializer shipmentInfoInitializer = new SSLCShipmentInfoInitializer ("Courier",
        //       2, new SSLCShipmentInfoInitializer.ShipmentDetails("AA","Address 1",
        //     "Dhaka","1000","BD"));

      buy.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              call();
          }
      });


    }

    private void call(){
        IntegrateSSLCommerz
                .getInstance(GateWay_payment.this)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .addCustomerInfoInitializer(customerInfoInitializer)
                //.addProductInitializer(productInitializer)
                .buildApiCall(this);
    }

    @Override
    public void transactionSuccess(SSLCTransactionInfoModel sslcTransactionInfoModel) {

        typ.setText("Transaction was successfull");
        typ.setTextColor(Color.parseColor("#1FBA25"));
        message.setText("Please,wait 30 minutes to get confirm from authority");
        FirebaseDatabase.getInstance().getReference("Request").child(user.id).setValue(user);
        Toast.makeText(GateWay_payment.this,"Your Order has been placed. Wait For the confirm message.",Toast.LENGTH_SHORT).show();
        buy.setVisibility(View.INVISIBLE);

    }

    @Override
    public void transactionFail(String s) {
        typ.setText(s);
        message.setText("An error occured to buy the product");
    }

    @Override
    public void merchantValidationError(String s) {
        typ.setText(s);
        message.setText("Please,try again");
    }
}