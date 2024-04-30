package com.app.secretdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.secretdiary.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    String personName, personEmail,personalNumber;
    Uri personPhoto;
    String otpid, number;
    Users users, userss;
    ProgressDialog dialog;
    ProgressDialog progressDialog;
    ProgressDialog dialog1;
    ProgressDialog progressDialog1;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 100;
    String image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Secret Diary");
        dialog.setMessage("fetching your gmail account...");
        dialog.setCancelable(false);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account...");
        progressDialog.setCancelable(false);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setTitle("Creating Account");
        progressDialog1.setMessage("We are creating your account...");
        progressDialog1.setCancelable(false);
        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("sending otp...");
        dialog1.setCancelable(false);
        binding.ccp.registerCarrierNumberEditText(binding.tin);
        image1="https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png";

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        
        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anomousAuth();
                binding.progressBar4.setVisibility(View.VISIBLE);


            }
            
        });

        binding.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.NumberView.setVisibility(View.VISIBLE);

            }

        });
        binding.card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.NumberView.setVisibility(View.GONE);
                dialog.show();
                signIn();
            }
        });

        binding.sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.tin.getText().toString().isEmpty()) {
                    binding.tin.setError("please enter number");
                    Toast.makeText(SignUpActivity.this, "please enter number", Toast.LENGTH_SHORT).show();
                } else {
                    dialog1.show();
                    number = binding.ccp.getFullNumberWithPlus().replace(" ", "");
                    initiateotp();
                }

            }
        });
        binding.botpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!binding.t2.getText().toString().isEmpty()) {

                    progressDialog1.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, binding.t2.getText().toString());
                    signInWithPhoneAuthCredential(credential);


                } else {
                    binding.tin.setError("please enter number");
                    Toast.makeText(SignUpActivity.this, "please enter number", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void anomousAuth() {
        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            binding.progressBar4.setVisibility(View.GONE);
                            Intent intent = new Intent(SignUpActivity.this, SplashActivity2.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initiateotp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                otpid = s;
                                dialog1.dismiss();
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                dialog1.dismiss();
                                progressDialog1.show();
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                dialog1.dismiss();
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference().child("Users Info").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(auth.getUid())){
                                        Intent intent = new Intent(SignUpActivity.this, SplashActivity2.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    }else {
                                        Users user = new Users("number","",number);
                                        FirebaseDatabase.getInstance().getReference().child("Users Info").child(FirebaseAuth.getInstance().getUid()).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        dialog1.dismiss();
                                                        progressDialog1.dismiss();
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(SignUpActivity.this, SplashActivity2.class);
                                                        startActivity(intent);
                                                        finishAffinity();
                                                    }
                                                });

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            progressDialog1.dismiss();
                            dialog1.dismiss();
                            Toast.makeText(SignUpActivity.this, "signin code error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
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
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignUpActivity.this);
                            if (acct != null) {
                                personName = user.getDisplayName();
                                personEmail = user.getEmail();
                                personalNumber = user.getPhoneNumber();
                                personPhoto = acct.getPhotoUrl();
                                Toast.makeText(SignUpActivity.this, personEmail, Toast.LENGTH_SHORT).show();
                            }
                            FirebaseDatabase.getInstance().getReference().child("Users Info").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(auth.getUid())){
                                        Intent intent = new Intent(SignUpActivity.this, SplashActivity2.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        finishAffinity();
                                    }else {
                                        Users user = new Users("google",personEmail,"");
                                        FirebaseDatabase.getInstance().getReference().child("Users Info").child(FirebaseAuth.getInstance().getUid()).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        dialog1.dismiss();
                                                        progressDialog1.dismiss();
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(SignUpActivity.this, SplashActivity2.class);
                                                        startActivity(intent);
                                                        finishAffinity();
                                                    }
                                                });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            dialog.dismiss();
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
