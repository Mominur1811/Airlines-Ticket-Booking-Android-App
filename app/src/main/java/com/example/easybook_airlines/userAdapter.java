package com.example.easybook_airlines;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class userAdapter extends FirebaseRecyclerAdapter<Post,userAdapter.MyViewHolder> {

    private Activity activity;
    private Context context;

    public userAdapter(@NonNull FirebaseRecyclerOptions<Post> options, Activity activity, Context context) {
        super(options);
        this.activity = activity;
        this.context = context;
    }

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public userAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Post model) {
        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());
        holder.seat.setText(model.getSeat());
        String time=model.time+", "+model.arrivalDate;
        holder.date.setText(time);
        holder.flightid.setText(model.getFlightId());
        String pr=model.price+"$";
        holder.price.setText(pr);
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                View dialogView= LayoutInflater.from(v.getContext()).inflate(R.layout.book_ticket,null);
                EditText id=dialogView.findViewById(R.id.bookFlightId);
                EditText purchase=dialogView.findViewById(R.id.bookTicketNumber);
                EditText ac=dialogView.findViewById(R.id.userAccountNumber);
                TextView pay=dialogView.findViewById(R.id.user_to_pay);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.setTitle("Booking");
                id.setEnabled(false);
                purchase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String x=purchase.getText().toString().trim();
                        if(x.equals("")){

                        }
                        else {
                            int ans=Integer.parseInt(x);
                            int a=ans;
                            if(a>Integer.parseInt(model.seat)){
                                purchase.setError("Available Ticket Limit exeed");
                                return;
                            }
                            ans=ans* Integer.parseInt(model.price);
                            x=String.valueOf(ans);
                            pay.setText(x);
                        }
                    }
                });
                id.setText(model.flightId);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key= FirebaseDatabase.getInstance().getReference("Request").push().getKey();
                        String s1=id.getText().toString().trim();
                        String s2=purchase.getText().toString().trim();
                        String s3= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String acNo=ac.getText().toString().trim();
                        String to_pay=pay.getText().toString().trim();
                        if(TextUtils.isEmpty(s1)){
                            Toast.makeText(v.getContext(),"Invalid Flight Id",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(s2)){
                            Toast.makeText(v.getContext(),"Invalid Buying number",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(s3)){
                            Toast.makeText(v.getContext(),"Invalid User",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(acNo)){
                            Toast.makeText(v.getContext(),"Invalid Account No",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(to_pay)){
                            Toast.makeText(v.getContext(),"Invalid paying Amount",Toast.LENGTH_SHORT).show();
                            return;
                        }


                        if(Integer.parseInt(model.seat)<Integer.parseInt(s2) ||Integer.parseInt(s2)<=0 || Integer.parseInt(s2)>4){
                            Toast.makeText(v.getContext(),"Buying Ticket Quantity is no valid",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String s5=model.time+", "+model.arrivalDate;
                            Intent intent=new Intent(context,GateWay_payment.class);
                            Passenger p=new Passenger(s3,s1,s2,to_pay,acNo,model.from,model.to,s5,key);
                            intent.putExtra("Id1",p);
                            activity.startActivity(intent);
                            //activity.finish();
                            //FirebaseDatabase.getInstance().getReference("Request").child(key).setValue(new Passenger(s3,s1,s2,to_pay,acNo,model.from,model.to,s5,key));
                            //Toast.makeText(v.getContext(),"Your Order has been placed. Wait For the confirm message.",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist,parent,false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView from,to,seat,date,flightid,price;
        Button book;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            from=itemView.findViewById(R.id.b1);
            to=itemView.findViewById(R.id.b2);
            seat=itemView.findViewById(R.id.b3);
            date=itemView.findViewById(R.id.b5);
            flightid=itemView.findViewById(R.id.b6);
            price=itemView.findViewById(R.id.b4);
            book=itemView.findViewById(R.id.user_bool_recycler);
        }
    }
}
