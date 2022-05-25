package com.example.easybook_airlines;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class NotificationUserAdapter extends FirebaseRecyclerAdapter<UserNotificationRetreiver,NotificationUserAdapter.myviewholder> {



    public NotificationUserAdapter(@NonNull FirebaseRecyclerOptions<UserNotificationRetreiver> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull UserNotificationRetreiver model) {
        holder.flightid.setText(model.getFlightID());
        holder.message.setText(model.getMessage());
        String s=model.getMessage();
        if(s.equals("ACCEPTED")){
            holder.message.setTextColor(Color.parseColor("#1FBA25"));

        }
        else{
           holder.visible2.setText("You have been refunded.Sorry for the trouble");
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key= FirebaseAuth.getInstance().getCurrentUser().getUid();

                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("DELETE");
                passwordResetDialog.setMessage("Press Confirm to delete the notification");
                passwordResetDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("UserNotification").child(key).child(model.id).removeValue();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.show();
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_user,parent,false);
        return new NotificationUserAdapter.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView flightid,message,amount,visible1,visible2;
        Button delete;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            flightid=(itemView).findViewById(R.id.userNotificationFlightID);
            message=(itemView).findViewById(R.id.adminPermission);
            delete=(itemView).findViewById(R.id.deleteNotification);
            visible2=(itemView).findViewById(R.id.PaymentAmount);
        }
    }
}
