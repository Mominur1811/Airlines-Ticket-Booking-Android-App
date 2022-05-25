package com.example.easybook_airlines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class historyAdapter extends FirebaseRecyclerAdapter<UserNotificationRetreiver,historyAdapter.myviewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public historyAdapter(@NonNull FirebaseRecyclerOptions<UserNotificationRetreiver> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull UserNotificationRetreiver model) {

        holder.id.setText(model.getFlightID());
        holder.seat.setText(model.getTicket_count());
        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());
        holder.time.setText(model.getDepart());

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_history_list,parent,false);
        return new historyAdapter.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView from,to,id,seat,time;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            from=(itemView).findViewById(R.id.q1);
            to=(itemView).findViewById(R.id.q2);
            id=(itemView).findViewById(R.id.q3);
            seat=(itemView).findViewById(R.id.q4);
            time=(itemView).findViewById(R.id.q5);
        }
    }
}
