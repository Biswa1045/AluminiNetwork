package com.biswa1045.alumininetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splash extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (user != null) {
                        String uid = user.getUid().toString();
                        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                        DocumentReference docIdRef = rootRef.collection("User").document(uid);
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        startActivity(new Intent(splash.this,MainActivity.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(splash.this,GetUserInfoActivity.class));
                                        finish();

                                    }
                                } else {
                                    Toast.makeText(splash.this, "Please try again later", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }else{
                        Intent i = new Intent(getApplicationContext(), login.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, 1000);


            }

    @Override
    public void onBackPressed() {

    }
}