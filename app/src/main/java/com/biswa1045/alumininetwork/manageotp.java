package com.biswa1045.alumininetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class manageotp extends AppCompatActivity {
    EditText edit;
    Button butt;
    String phonenum;
    FirebaseAuth mAuth;
    String otpid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);
        phonenum=getIntent().getStringExtra("mobile").toString();
        edit=findViewById(R.id.edit);
        butt=findViewById(R.id.butt);
        TextView textView=findViewById(R.id.textView4);
        textView.setText("Enter the verification code that we just sent to your "+phonenum);
        mAuth= FirebaseAuth.getInstance();
        initiateotp();

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "blank space can't be processed", Toast.LENGTH_SHORT).show();
                }
                else if ((edit.getText().toString().length()!=6)){
                    Toast.makeText(getApplicationContext(), "invalid otp", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otpid,edit.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
    }

    private void initiateotp() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonenum)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid=s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(manageotp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null){
                                String uid = firebaseUser.getUid().toString();
                                data_check(uid);
                            }else{
                                Toast.makeText(manageotp.this, "signin error", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(manageotp.this, "signin error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void data_check(String uid){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection("User").document(uid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        startActivity(new Intent(manageotp.this,MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(manageotp.this,GetUserInfoActivity.class));
                        finish();
                    //    Log.d(TAG, "Document does not exist!");
                    }
                } else {
                    Toast.makeText(manageotp.this, "Please try again later", Toast.LENGTH_SHORT).show();
                 //   Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });

    }
}