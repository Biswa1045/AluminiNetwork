package com.biswa1045.alumininetwork;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class addVideo extends AppCompatActivity {

    private ActionBar actionBar;
    private EditText title;
    private VideoView videoView;
    private Button uploadVideo;
    private FloatingActionButton pickVideo;

    private static final int VIDEO_GALLERY=100;
    private static final int VIDEO_CAMERA=101;
    private static final int CAMERA_REQUEST=102;

    private String[] cameraPermissions;
    private Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        actionBar=getSupportActionBar();
        actionBar.setTitle("add new video");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        title=findViewById(R.id.title);
        videoView=findViewById(R.id.videoView);
        uploadVideo=findViewById(R.id.uploadVideo);
        pickVideo=findViewById(R.id.pickVideo);

        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPickMeth();
            }
        });
    }

    private void videoPickMeth() {
        String[] options={"camera","gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("pick video from")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i==0){
                            if (!checkCamera()){
                                cameraPermission();
                            }else{
                                videoPickCamera();
                            }
                        }
                        else if(i==1){
                            videoPickGallery();
                        }
                    }
                })
                .show();
    }

    private void cameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST);
    }

    private boolean checkCamera(){
        boolean result1= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean result2= ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK)== PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
    }

       private void videoPickGallery(){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityIfNeeded(Intent.createChooser(intent,"select videos"),VIDEO_GALLERY);

    }

    private void videoPickCamera(){
        Intent intent =new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityIfNeeded(intent,VIDEO_CAMERA);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}