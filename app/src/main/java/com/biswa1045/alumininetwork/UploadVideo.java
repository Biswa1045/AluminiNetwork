package com.biswa1045.alumininetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadVideo extends AppCompatActivity {

    FloatingActionButton addVideoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        setTitle("videos");

        addVideoBtn=findViewById(R.id.addVideoBtn);
        addVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadVideo.this,addVideo.class);
                startActivity(intent);
            }
        });
    }
}