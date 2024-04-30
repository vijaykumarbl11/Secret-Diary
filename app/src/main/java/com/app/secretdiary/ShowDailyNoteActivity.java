package com.app.secretdiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.secretdiary.databinding.ActivityShowDailyNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ShowDailyNoteActivity extends AppCompatActivity {
    ActivityShowDailyNoteBinding binding;
    ArrayList<Users> list;
    String senderid, key,timeee;
    long a ='A';
    ImageView image1;

    Uri selectedImage;
    String todaydate1;
    int aa;
    EditText title, des;
    ArrayList<Uri>imageList=new ArrayList<Uri>();
    private Uri ImageUri;
    private int uploadCount=0;
    ProgressDialog dialog;
    private static final int PICK_IMAGE = 1;
    String addedpostkey;
    Button add,addimage;
    TextView write1, write2,snofiText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowDailyNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar2.setVisibility(View.VISIBLE);
        dialog=new ProgressDialog(this);
        dialog.setMessage("adding data ....");
        dialog.setCancelable(false);
        list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        todaydate1 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        senderid = FirebaseAuth.getInstance().getUid();
        String todaydate = getIntent().getStringExtra("todaydate");
        binding.time.setText(todaydate);
        if (!todaydate.equals(todaydate1)){
            binding.separate.setVisibility(View.VISIBLE);

        }else {
            binding.separate.setVisibility(View.GONE);

        }

        AddOldAdapter addTodayAdapter = new AddOldAdapter(list, ShowDailyNoteActivity.this);
        binding.TodayRec.setAdapter(addTodayAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowDailyNoteActivity.this);
        binding.TodayRec.setLayoutManager(layoutManager);

        FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(todaydate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    list.add(users);
                }
                binding.progressBar2.setVisibility(View.GONE);
                binding.totalnumbern.setText("( "+list.size()+" Notes )");
                addTodayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.separate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);

                TextView title, des;
                Button add;
                TextView write1, write2;

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowDailyNoteActivity.this);
                View view = LayoutInflater.from(ShowDailyNoteActivity.this).inflate(R.layout.separate_note, viewGroup, false);
                builder.setCancelable(true);
                builder.setView(view);
                add = view.findViewById(R.id.add);
                title = view.findViewById(R.id.notetitle);
                des = view.findViewById(R.id.notedescription);
                add.setBackgroundColor(Color.GREEN);
                write1 = view.findViewById(R.id.write1);
                write2 = view.findViewById(R.id.write2);
                write1.setVisibility(View.VISIBLE);
                write2.setVisibility(View.VISIBLE);
                addimage = view.findViewById(R.id.addimage);
                image1 = view.findViewById(R.id.image1);


                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                addimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 45);
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (title.getText().toString().isEmpty() && des.getText().toString().isEmpty()) {
                            Toast.makeText(ShowDailyNoteActivity.this, "please fill some data", Toast.LENGTH_SHORT).show();
                        }
                        else if (selectedImage==null && (!title.getText().toString().isEmpty() || !des.getText().toString().isEmpty())) {
                            dialog.show();

                            String Title = title.getText().toString();
                            String Des = des.getText().toString();
                            Calendar calendar1 = Calendar.getInstance();
                            String notetime = DateFormat.getInstance().format(calendar1.getTime());
                            Users users = new Users("",Title, Des, "", notetime, "", todaydate);
                            FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(todaydate)
                                    .push().setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ShowDailyNoteActivity.this, "data added in your today note", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    dialog.dismiss();
                                    FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid())
                                            .child(todaydate).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                key = dataSnapshot.getRef().getKey();
                                            }
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("postkey", key);
                                            FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(todaydate)
                                                    .child(key).updateChildren(hashMap);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }
                        else if (selectedImage != null && (!title.getText().toString().isEmpty() || !des.getText().toString().isEmpty())) {
                            dialog.show();
                            Calendar calendar1 = Calendar.getInstance();
                            StorageReference reference2 = FirebaseStorage.getInstance().getReference().child("addedImage").child(FirebaseAuth.getInstance().getUid()).child(todaydate)
                                    .child(calendar1.getTimeInMillis()+"");
                            Bitmap bmp = null;
                            try {
                                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                            byte[] data = baos.toByteArray();
                            //uploading the image
                            UploadTask uploadTask2 = reference2.putBytes(data);
                            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri urib) {
                                            String image = urib.toString();
                                            String Title = title.getText().toString();
                                            String Des = des.getText().toString();
                                            Calendar calendar1 = Calendar.getInstance();
                                            String notetime = DateFormat.getInstance().format(calendar1.getTime());
                                            Users users = new Users(image,Title, Des, "", notetime, "", todaydate);
                                            FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(todaydate)
                                                    .push().setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(ShowDailyNoteActivity.this, "data added in your today note", Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    dialog.dismiss();
                                                    FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid())
                                                            .child(todaydate).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                key = dataSnapshot.getRef().getKey();
                                                            }
                                                            HashMap<String, Object> hashMap = new HashMap<>();
                                                            hashMap.put("postkey", key);
                                                            FirebaseDatabase.getInstance().getReference().child("DailyNote").child(FirebaseAuth.getInstance().getUid()).child(todaydate)
                                                                    .child(key).updateChildren(hashMap);

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });


                                }
                            });


                        }

                    }
                });

                alertDialog.show();
            }
        });
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                if (data.getData() != null) {
                    image1.setVisibility(View.VISIBLE);
                    image1.setImageURI(data.getData());
                    selectedImage = data.getData();
                }
            }
        }

}



