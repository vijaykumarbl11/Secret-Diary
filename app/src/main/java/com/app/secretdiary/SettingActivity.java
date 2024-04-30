package com.app.secretdiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.secretdiary.databinding.ActivitySettingBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.BuildConfig;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    String type;
    String senderid;
    Uri image;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    ReviewInfo reviewInfo;
    ReviewManager manager;
    DatePickerDialog.OnDateSetListener setListener;
    private MaterialTimePicker picker;
    Calendar calendar, calNow;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    String personName, personEmail, personalNumber;
    Uri personPhoto;
    ProgressDialog dialog;
    ProgressDialog progressDialog;
    ProgressDialog dialog1;
    ProgressDialog progressDialog1;
    int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        senderid = FirebaseAuth.getInstance().getUid();
        type = getIntent().getStringExtra("type");
        dialog = new ProgressDialog(this);
        dialog.setTitle("Secret Diary");
        dialog.setMessage("fetching your gmail account...");
        dialog.setCancelable(false);
        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("Updating data ...");
        dialog1.setCancelable(false);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account...");
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        if (type.equals("signin")) {
            binding.signincard.setVisibility(View.VISIBLE);
            binding.signincard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                    dialog.show();
                }
            });


        }

       
        if (type.equals("moreapps")) {
            binding.moreappcard.setVisibility(View.VISIBLE);
            binding.moreapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=ResiEasy-+Buy/Rent/Sell+Residency")));

                }
            });
        }

        if (type.equals("share")) {
            binding.sharecard.setVisibility(View.VISIBLE);
            binding.sharee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Secret Diary");
                        String applink="https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName();
                        intent.putExtra(Intent.EXTRA_TEXT,applink);
                        startActivity(Intent.createChooser(intent,"Share Secret Diary Application"));

                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(SettingActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (type.equals("rating")) {
            binding.ratecard.setVisibility(View.VISIBLE);
            binding.ratee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(SettingActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }                }
            });

        }
        if (type.equals("contactus")){
            binding.contacusview.setVisibility(View.VISIBLE);
        }
        if (type.equals("aboutus")){
            binding.aboutusview.setVisibility(View.VISIBLE);
        }
        if (type.equals("privacy")){
            binding.privacypolicyview.setVisibility(View.VISIBLE);
        }
        if (type.equals("term")){
            binding.termconditionview.setVisibility(View.VISIBLE);
        }

        binding.contactuscontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7028297606"));
                startActivity(intent);

            }
        });

        binding.contactuswhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wn="https://wa.me/+917028297606?text= Hi is anyone available?";
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wn));
                startActivity(intent);

            }
        });

        binding.contactusemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "help@resieasy.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "I have problem in ResiEasy Application");
                    intent.putExtra(Intent.EXTRA_TEXT, "Write your problem");
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    Toast.makeText(SettingActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (type.equals("logout")) {
            binding.logcard.setVisibility(View.VISIBLE);
            binding.logoutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 45) {
            if (data != null) {
                if (data.getData() != null) {
                    binding.ii.setImageURI(data.getData());
                    image = data.getData();
                }
            }
        }

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                dialog.dismiss();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                dialog.dismiss();
                Log.w("TAG", "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SettingActivity.this);
                            if (acct != null) {
                                personName = user.getDisplayName();
                                personEmail = user.getEmail();
                                personalNumber = user.getPhoneNumber();
                                personPhoto = acct.getPhotoUrl();
                                Toast.makeText(SettingActivity.this, personEmail, Toast.LENGTH_SHORT).show();
                            }
                            Users user12 = new Users("google", personEmail, "");
                            FirebaseDatabase.getInstance().getReference().child("Users Info").child(FirebaseAuth.getInstance().getUid()).setValue(user12)
                                    .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(SettingActivity.this, SplashActivity2.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SettingActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        FirebaseDatabase.getInstance().getReference("Users Info")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(auth.getUid())) {
                            FirebaseDatabase.getInstance().getReference("Users Info").child(auth.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String type = snapshot.child("signuptype").getValue(String.class);
                                                if (type.equals("number")) {

                                                    auth.signOut();
                                                    Intent intent01 = new Intent(SettingActivity.this, SignUpActivity.class);
                                                    startActivity(intent01);
                                                    finishAffinity();

                                                } else if (type.equals("google")) {

                                                    mGoogleSignInClient.signOut()
                                                            .addOnCompleteListener(SettingActivity.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    auth.signOut();
                                                                    Intent intent02 = new Intent(SettingActivity.this, SignUpActivity.class);
                                                                    startActivity(intent02);
                                                                    finishAffinity();
                                                                }
                                                            });
                                                } else {

                                                    auth.signOut();
                                                    Intent intent01 = new Intent(SettingActivity.this, SignUpActivity.class);
                                                    startActivity(intent01);
                                                    finishAffinity();


                                                }


                                            } else {

                                                auth.signOut();
                                                Intent intent01 = new Intent(SettingActivity.this, SignUpActivity.class);
                                                startActivity(intent01);
                                                finishAffinity();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                        } else {
                            auth.signOut();
                            Intent intent01 = new Intent(SettingActivity.this, SignUpActivity.class);
                            startActivity(intent01);
                            finishAffinity();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void profile() {
        dialog1.show();
        StorageReference childRef2 = FirebaseStorage.getInstance().getReference().child("profiles").child(auth.getUid());
        //storageRef.child(UserDetails.username+"profilepic.jpg");
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        //uploading the image
        UploadTask uploadTask2 = childRef2.putBytes(data);
        uploadTask2.addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot task) {
                childRef2.getDownloadUrl().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String imageUrl = uri.toString();
                        String name = binding.nn.getText().toString();
                        String email = binding.ee.getText().toString();
                        String number = binding.nono.getText().toString();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", name);
                        hashMap.put("image", imageUrl);
                        hashMap.put("email", email);
                        hashMap.put("number", number);
                        FirebaseDatabase.getInstance().getReference().child("Users Info").child(FirebaseAuth.getInstance().getUid()).updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog1.dismiss();
                                        Toast.makeText(SettingActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });

            }
        });

    }


}