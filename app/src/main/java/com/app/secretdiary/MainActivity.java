package com.app.secretdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.secretdiary.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;



public class MainActivity extends AppCompatActivity  {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    Vibrator vibrator;
    ArrayList<Users> list;
    String currentdate;
    MenuItem menuItem;
    SearchView searchView;
    int REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list = new ArrayList<>();
        binding.progressBar.setVisibility(View.VISIBLE);
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        Calendar calendar = Calendar.getInstance();
        currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        AddDailyAdapter addTodayAdapter = new AddDailyAdapter(list, MainActivity.this);
        binding.dailyRec.setAdapter(addTodayAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        binding.dailyRec.setLayoutManager(layoutManager);

        Query query = FirebaseDatabase.getInstance().getReference().child("WriterInfo").child(FirebaseAuth.getInstance().getUid()).orderByChild("time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.dialogBox.setVisibility(View.GONE);
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Users users = dataSnapshot.getValue(Users.class);
                        list.add(users);
                    }
                    Collections.reverse(list);
                    binding.progressBar.setVisibility(View.GONE);
                    addTodayAdapter.notifyDataSetChanged();
                } else {
                    binding.dialogBox.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.searchview1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Users> list1 = new ArrayList<>();
                for (Users object : list) {
                    if (object.getFirsttime().toLowerCase().contains(newText.toLowerCase())) {
                        list1.add(object);
                    }
                }
                AddDailyAdapter adapter = new AddDailyAdapter(list1, MainActivity.this);
                binding.dailyRec.setAdapter(adapter);
                return true;

            }

        });

        binding.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });
        binding.setting1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        binding.search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.search11.setVisibility(View.VISIBLE);

            }
        });
        binding.closesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.search11.setVisibility(View.GONE);
            }
        });


        //update

       /* inAppUpdateManager.checkForAppUpdate();
        AppUpdateManager appUpdateManager= AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask=appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE&&result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.FLEXIBLE,MainActivity.this,REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/




    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show();

            if (resultCode!=RESULT_OK){
                Log.d(" ","Update flow failed"+resultCode);
            }
        }
    }
*/



}


