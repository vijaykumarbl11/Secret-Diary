package com.app.secretdiary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.secretdiary.databinding.AddintodayBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddOldAdapter extends RecyclerView.Adapter<AddOldAdapter.AddTodayViewHolder> {
    ArrayList<Users> list;
    Context context;
    ArrayList<Users>list1;
    public AddOldAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public AddTodayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.addintoday, viewGroup, false);
        return new AddTodayViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull  AddOldAdapter.AddTodayViewHolder holder, int i) {
        Users data=list.get(i);
        String TT=data.getTitle();
        String DD=data.getDescroption();
        String topdate=data.getToptime();
        holder.binding.T.setText(data.getTitle());
        holder.binding.D.setText(data.getDescroption());
        holder.binding.Time.setText(data.getNotetime());
        if (data.getAddedimage().isEmpty()){
            holder.binding.oneimage.setVisibility(View.GONE);

        }else {
            holder.binding.oneimage.setVisibility(View.VISIBLE);
            Picasso.get().load(data.getAddedimage()).placeholder(R.drawable.plh).into(holder.binding.oneimage);

        }
        if (data.getLastupdate().equals("")){
            holder.binding.updateT.setVisibility(View.GONE);

        }else {
            holder.binding.updateT.setVisibility(View.VISIBLE);
            holder.binding.updateTime.setText(data.getLastupdate());
        }

        holder.binding.oneimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ImageShowActivity.class);
                intent.putExtra("image",data.getAddedimage());
                context.startActivity(intent);
            }
        });
        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = v.findViewById(android.R.id.content);

                TextView title,des;
                Button add,addimage;
                TextView edit1,edit2;
                ImageView image1;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.separate_note, viewGroup, false);
                builder.setCancelable(true);
                builder.setView(view);
                image1 = view.findViewById(R.id.image1);
                add = view.findViewById(R.id.add);
                title = view.findViewById(R.id.notetitle);
                des = view.findViewById(R.id.notedescription);
                edit1 = view.findViewById(R.id.edit1);
                edit2 = view.findViewById(R.id.edit2);
                edit1.setVisibility(View.VISIBLE);
                edit2.setVisibility(View.VISIBLE);
                add.setBackgroundColor(Color.GREEN);
                add.setText("edit");
                title.setText(TT);
                des.setText(DD);
                title.setText(TT);
                des.setText(DD);
                addimage = view.findViewById(R.id.addimage);
                addimage.setVisibility(View.GONE);
                if (data.getAddedimage().isEmpty()){
                    image1.setVisibility(View.INVISIBLE);

                }else {
                    image1.setVisibility(View.VISIBLE);
                    Picasso.get().load(data.getAddedimage()).placeholder(R.drawable.plh).into(image1);

                }
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date=new Date();
                        String Title=title.getText().toString();
                        String Des=des.getText().toString();
                        Calendar calendar1=Calendar.getInstance();
                        String notetime =DateFormat.getInstance().format(calendar1.getTime());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("title",Title);
                        hashMap.put("descroption",Des);
                        hashMap.put("lastupdate",notetime);
                        FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(topdate)
                                .child(data.getPostkey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "data edit successfully", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();

                            }
                        });
                    }
                });


                alertDialog.show();
            }
        });
        holder.binding.deletenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.binding.deletenote);
                popupMenu.getMenuInflater().inflate(R.menu.delete_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.deletee) {
                            FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(topdate)
                                    .child(data.getPostkey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance().getReference().child("AddedImage").child(FirebaseAuth.getInstance().getUid()).child(topdate)
                                                    .child(data.getPostkey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                        }
                                                    });
                                        }
                                    });
                        }

                        return false;

                    }
                });
                popupMenu.show();
            }
        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AddTodayViewHolder extends RecyclerView.ViewHolder{
        AddintodayBinding binding;
        public AddTodayViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=AddintodayBinding.bind(itemView);
        }
    }
}
