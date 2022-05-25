package com.example.easybook_airlines;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NotificationAdminAdapter extends FirebaseRecyclerAdapter<Passenger,NotificationAdminAdapter.myviewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotificationAdminAdapter(@NonNull FirebaseRecyclerOptions<Passenger> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Passenger model) {

        holder.pId.setText(model.getPassengerID());
        holder.sNo.setText(model.getPurchaseSeat());
        holder.fId.setText(model.getFlightID());
        holder.amount.setText(model.getAmount());
        holder.accountNo.setText(model.getAccount());
        String review=model.getFrom();
        holder.reveiwed_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder deleteFlight=new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                deleteFlight.setTitle("Notification");
                deleteFlight.setMessage("Are you sure to delete?");
                deleteFlight.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("Request").child(model.id).removeValue();
                    }
                });
                deleteFlight.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                deleteFlight.show();
            }
        });
        if(review.equals("true")){
              holder.review_text.setVisibility(View.VISIBLE);
              holder.reveiwed_delete.setVisibility(View.VISIBLE);
              holder.control.setVisibility(View.INVISIBLE);
        }
        else {
            holder.review_text.setVisibility(View.INVISIBLE);
            holder.reveiwed_delete.setVisibility(View.INVISIBLE);
            holder.control.setVisibility(View.VISIBLE);
            holder.control.setOnClickListener(v -> {
                AlertDialog.Builder admin_permission = new AlertDialog.Builder(v.getContext());
                admin_permission.setTitle("Accept Purchase Request ?");

                DatabaseReference db = FirebaseDatabase.getInstance().getReference("UserNotification").child(model.passengerID);
                String s2 = db.push().getKey();
                DatabaseReference not=FirebaseDatabase.getInstance().getReference("NotificationCount").child(model.passengerID);

                admin_permission.setPositiveButton("Accept", (dialog, which) -> {
                    //Date c=Calendar.getInstance().getTime();
                    //SimpleDateFormat dateFormat=new SimpleDateFormat("d/M/yyyy");
                    //Date date=dateFormat.format(c);
                    long cutoff=new Date().getTime()- TimeUnit.MILLISECONDS.convert(0,TimeUnit.DAYS);
                    UserNotificationRetreiver hashMap = new UserNotificationRetreiver(model.flightID, model.amount, "ACCEPTED", model.account, model.purchaseSeat, model.from, model.to, model.depart,s2,cutoff);
                    db.child(s2).setValue(hashMap);
                    not.addListenerForSingleValueEvent(new ValueEventListener() {
                        String s;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!=null){
                                s=snapshot.getValue(String.class);
                                int count=Integer.parseInt(s);
                                count=count+1;
                                s=String.valueOf(count);
                            }
                            else{
                                s="1";
                            }
                            not.setValue(s);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    String s3 = FirebaseDatabase.getInstance().getReference("History").child(model.getPassengerID()).push().getKey();
                    FirebaseDatabase.getInstance().getReference("History").child(model.getPassengerID()).child(s3).setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    model.setFrom("true");
                                    Passenger p=new Passenger(model.passengerID,model.flightID,model.purchaseSeat,model.amount,model.account,"true",model.to,model.depart,model.id);
                                    FirebaseDatabase.getInstance().getReference("Request").child(model.getId()).setValue(model);
                                }
                            });

                    //check the user having bought the ticket eralier
                    DatabaseReference d = FirebaseDatabase.getInstance().getReference("FlightDetails").child(model.flightID).child(model.passengerID);
                    d.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String ticket_count = model.purchaseSeat;
                            if (snapshot.getValue() != null) {
                                Passenger p = snapshot.getValue(Passenger.class);
                                String a = p.purchaseSeat;
                                int a1 = Integer.parseInt(model.purchaseSeat) + Integer.parseInt(a);
                                ticket_count = String.valueOf(a1);
                            }

                            d.setValue(new Passenger(model.passengerID, model.flightID, ticket_count, model.amount, model.account, model.from, model.to, model.depart,model.id));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    DatabaseReference dx = FirebaseDatabase.getInstance().getReference("Flightid").child(model.flightID);
                    dx.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String f = Objects.requireNonNull(snapshot.getValue(Post.class)).seat;
                            int cur = Integer.parseInt(f) - Integer.parseInt(model.purchaseSeat);
                            f = String.valueOf(cur);
                            dx.child("seat").setValue(f);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                });
                admin_permission.setNegativeButton("Reject", (dialog, which) -> {

                    not.addListenerForSingleValueEvent(new ValueEventListener() {
                        String s;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!=null){
                                s=snapshot.getValue(String.class);
                                int count=Integer.parseInt(s);
                                count=count+1;
                                s=String.valueOf(count);
                            }
                            else{
                                s="1";
                            }
                            not.setValue(s);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Date c=Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("d/M/yyyy");
                    String date=dateFormat.format(c);
                    long cutoff=new Date().getTime()- TimeUnit.MILLISECONDS.convert(0,TimeUnit.DAYS);
                    UserNotificationRetreiver hashMap = new UserNotificationRetreiver(model.flightID, model.amount, "REJECTED", model.account, model.purchaseSeat, model.from, model.to, model.depart,s2,cutoff);
                    db.child(s2).setValue(hashMap);
                    model.setFrom("true");
                    Passenger p=new Passenger(model.passengerID,model.flightID,model.purchaseSeat,model.amount,model.account,"true",model.to,model.depart,model.id);
                    FirebaseDatabase.getInstance().getReference("Request").child(model.getId()).setValue(model);
                });
                admin_permission.show();

            });
        }
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_admin,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{

        TextView pId,sNo,fId,amount,accountNo,review_text;
        ImageView control,reveiwed_delete;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            pId=(itemView).findViewById(R.id.passengerRequestNotificationId);
            sNo=(itemView).findViewById(R.id.requstedNotificationSeatNo);
            fId=(itemView).findViewById(R.id.requestNotificationFlightId);
            control=(itemView).findViewById(R.id.adminControlButton);
            amount=(itemView).findViewById(R.id.customerPaidAmount);
            accountNo=(itemView).findViewById(R.id.customerAccountNo);
            reveiwed_delete=(itemView).findViewById(R.id.review_delete);
            review_text=(itemView).findViewById(R.id.review_text);
        }
    }
}