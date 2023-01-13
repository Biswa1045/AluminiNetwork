package com.biswa1045.alumininetwork;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class addVideo extends AppCompatActivity {

    private ActionBar actionBar;
    private EditText title;
    private VideoView videoView;
    private Button uploadVideo;
    private FloatingActionButton pickVideo;
    private String title2;

    private static final int VIDEO_GALLERY=100;
    private static final int VIDEO_CAMERA=101;
    private static final int CAMERA_REQUEST=102;

    private String[] cameraPermissions;
    private Uri videoUri=null;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        actionBar=getSupportActionBar();
        actionBar.setTitle("add new video");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        title=findViewById(R.id.title);
         title2=title.getText().toString().trim();
        videoView=findViewById(R.id.videoView);
        uploadVideo=findViewById(R.id.uploadVideo);
        pickVideo=findViewById(R.id.pickVideo);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setMessage("uploading video");
        progressDialog.setCanceledOnTouchOutside(false);


        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title1=title.getText().toString().trim();
                if(TextUtils.isEmpty(title1)) {
                    Toast.makeText(addVideo.this, "title is required", Toast.LENGTH_SHORT).show();
                }else if(videoUri==null){
                    Toast.makeText(addVideo.this, "pick a video first", Toast.LENGTH_SHORT).show();
                }
                else{
                uploadVideoFirebase();
            }}
        });
        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPickMeth();
            }
        });
    }

    private void uploadVideoFirebase() {
        progressDialog.show();

        String timestamp=""+System.currentTimeMillis();
        //timestamp
        String filePathAndName="videos/"+"video_"+timestamp;
         //storage reference
        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
         //upload video
        storageReference.putFile(videoUri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                           while (!uriTask.isSuccessful());
                           Uri downloadUri=uriTask.getResult();
                           if(uriTask.isSuccessful()){
                               //adding video details
                               HashMap<String,Object> hashMap=new HashMap<>();
                               hashMap.put("id",""+timestamp);
                               hashMap.put("title",""+title2);
                               hashMap.put("timestamp",""+timestamp);
                               hashMap.put("videourl",""+downloadUri);

                               DatabaseReference reference= FirebaseDatabase.getInstance().getReference("videos");
                               reference.child(timestamp)
                                       .setValue(hashMap)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               progressDialog.dismiss();
                                               Toast.makeText(addVideo.this, "video uploaded...", Toast.LENGTH_SHORT).show();
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {

                                               progressDialog.dismiss();
                                               Toast.makeText(addVideo.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       });
                           }
                       }
                   })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed in uploading
                        progressDialog.dismiss();
                        Toast.makeText(addVideo.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setVideoToVideoview(){
        MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.pause();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST:
                if (grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted && storageAccepted){
                    videoPickCamera();
                }
                else {
                    Toast.makeText(this, "camera and storage permission are required", Toast.LENGTH_SHORT).show();
                }
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==RESULT_OK){
            if (requestCode==VIDEO_GALLERY){
                videoUri=data.getData();
                setVideoToVideoview();
            }
            else if(requestCode==VIDEO_CAMERA){
                videoUri= data.getData();
                setVideoToVideoview();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}