package com.app.secretdiary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.secretdiary.databinding.AddindailyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddDailyAdapter extends RecyclerView.Adapter<AddDailyAdapter.AddTodayViewHolder> {
    ArrayList<Users> list;
    Context context;
    String timeee;
    Long ttt;
    public AddDailyAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public AddTodayViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.addindaily,viewGroup,false);
        return new AddTodayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  AddDailyAdapter.AddTodayViewHolder holder, int i) {
        Users data=list.get(i);
        holder.binding.datee.setText(data.getFirsttime());

        holder.binding.viewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ShowDailyNoteActivity.class);
                intent.putExtra("todaydate",data.getFirsttime());
                context.startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(data.getFirsttime()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                holder.binding.totalnumbern.setText("( "+snapshot.getChildrenCount()+" Notes )");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class AddTodayViewHolder extends RecyclerView.ViewHolder{
        AddindailyBinding binding;
        public AddTodayViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=AddindailyBinding.bind(itemView);
        }
    }
}
