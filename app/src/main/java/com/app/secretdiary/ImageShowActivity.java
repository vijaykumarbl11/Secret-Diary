package com.app.secretdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.secretdiary.databinding.ActivityImageShowBinding;
import com.squareup.picasso.Picasso;

public class ImageShowActivity extends AppCompatActivity {
    ActivityImageShowBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String image=getIntent().getStringExtra("image");
        Picasso.get().load(image).placeholder(R.drawable.plh).into(binding.proshow);
    }
}