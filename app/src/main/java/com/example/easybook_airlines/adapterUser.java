package com.example.easybook_airlines;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class adapterUser extends FirebaseRecyclerAdapter<USER,adapterUser.myviewholder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public adapterUser(@NonNull FirebaseRecyclerOptions<USER> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull adapterUser.myviewholder holder, int position, @NonNull USER model) {
        holder.name.setText(model.getFullname());
        holder.gender.setText(model.getGender());
        holder.birth.setText(model.getBirth_day());
        holder.email.setText(model.getEmail());
    }

    @NonNull
    @Override
    public adapterUser.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info,parent,false);
        return new adapterUser.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView name,birth,email,gender;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name=(itemView).findViewById(R.id.p1);
            birth=(itemView).findViewById(R.id.p3);
            gender=(itemView).findViewById(R.id.p2);
            email=(itemView).findViewById(R.id.p4);
        }
    }
}
