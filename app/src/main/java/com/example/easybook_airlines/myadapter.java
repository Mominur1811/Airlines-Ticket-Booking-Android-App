package com.example.easybook_airlines;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class myadapter extends FirebaseRecyclerAdapter<Post,myadapter.myviewholder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Post model) {

        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());
        holder.seat.setText(model.getSeat());
        holder.flightid.setText(model.getFlightId());
        holder.price.setText(model.getPrice());
        String s=model.getTime()+" , "+model.getArrivalDate();
        holder.date.setText(s);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View dialogView=LayoutInflater.from(v.getContext()).inflate(R.layout.updatedialog,null);
                EditText dg1=dialogView.findViewById(R.id.dialog1);
                EditText dg2=dialogView.findViewById(R.id.dialog2);
                EditText dg3=dialogView.findViewById(R.id.dialog3);
                EditText dg4=dialogView.findViewById(R.id.dialog4);
                EditText dg5=dialogView.findViewById(R.id.dialog5);
                EditText dg6=dialogView.findViewById(R.id.dialog6);
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                dg5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog=new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month=month+1;
                                String d=dayOfMonth+"/"+month+"/"+year;
                                dg5.setText(d);
                            }
                        },year,month,day);
                        datePickerDialog.show();
                    }
                });

                dg1.setText(model.getFrom().toUpperCase());
                dg2.setText(model.getTo().toUpperCase());
                dg3.setText(model.getPrice());
                dg5.setText(model.getArrivalDate());
                dg4.setText(model.getSeat());
                dg6.setText(model.time);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.setTitle("Update INFO");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String s1=dg1.getText().toString().toUpperCase().trim();
                        String s2=dg2.getText().toString().toUpperCase().trim();
                        String s3=dg3.getText().toString().trim();
                        String s4=dg4.getText().toString().trim();
                        String s5=dg5.getText().toString().trim();
                        String s7=dg6.getText().toString().trim();
                        String s6=s1+s2+s5;
                        Post x=new Post(model.flightId,s1,s2,s3,s5,s4,s6,s7);
                        FirebaseDatabase.getInstance().getReference("Flightid").child(model.flightId).setValue(x);

                    }
                });
                builder.show();


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteFlight=new AlertDialog.Builder(v.getContext());
                deleteFlight.setTitle("Delete Flight Info ?");
                deleteFlight.setMessage("Are you sure to delete?");
                deleteFlight.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s=model.getFlightId();
                        FirebaseDatabase.getInstance().getReference("Flightid").child(s).removeValue();
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

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView from,to,seat,date,flightid,price;
        Button edit,delete;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            from=itemView.findViewById(R.id.abc2);
            to=itemView.findViewById(R.id.abc3);
            seat=itemView.findViewById(R.id.abc7);
            date=itemView.findViewById(R.id.abc9);
            flightid=itemView.findViewById(R.id.abc10);
            price=itemView.findViewById(R.id.abc11);
            edit=itemView.findViewById(R.id.editInfo);
            delete=itemView.findViewById(R.id.deleteInfo);
        }
    }
}
