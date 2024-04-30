package com.app.secretdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.secretdiary.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressview.setVisibility(View.VISIBLE);


        FirebaseDatabase.getInstance().getReference().child("Users Info").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.signupview.setVisibility(View.GONE);
                    binding.exist.setVisibility(View.VISIBLE);
                    binding.progressview.setVisibility(View.GONE);

                    Users users=snapshot.getValue(Users.class);
                    if (!users.getEmail().isEmpty()){
                        binding.mail.setVisibility(View.VISIBLE);
                        binding.num.setVisibility(View.GONE);

                        binding.mail.setText(users.getEmail());

                    }else {
                        binding.mail.setVisibility(View.GONE);
                        binding.num.setVisibility(View.VISIBLE);
                        binding.num.setText(users.getNumber());

                    }

                }else {
                    binding.progressview.setVisibility(View.GONE);

                    binding.signupview.setVisibility(View.VISIBLE);
                    binding.exist.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","signin");
                startActivity(intent);

            }
        });



        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","share");
                startActivity(intent);
            }
        }); binding.contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","contactus");
                startActivity(intent);
            }
        });binding.aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","aboutus");
                startActivity(intent);
            }
        });binding.moreapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","moreapps");
                startActivity(intent);
            }
        });

        binding.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","rating");
                startActivity(intent);
            }
        }); binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","share");
                startActivity(intent);
            }
        });
        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","privacy");
                startActivity(intent);
            }
        }); binding.termscondi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","term");
                startActivity(intent);
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                intent.putExtra("type","logout");
                startActivity(intent);

            }
        });
    }
}