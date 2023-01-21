package com.biswa1045.alumininetwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class addVideo extends AppCompatActivity {
    private Dialog dialog;
    private EditText caption;
    private VideoView videoView;
    private Button uploadVideo;
    private static final int VIDEO_GALLERY=100;
    String name;
    String firebase_video_uri;
    private Uri videoUri=null;
    private FirebaseFirestore db;
    StorageReference storageReference;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        dialog = new Dialog(this);
        db = FirebaseFirestore.getInstance();
        videoView = findViewById(R.id.video_upload_view);
        uploadVideo = findViewById(R.id.post_video_activity);
        caption = findViewById(R.id.caption_video);
        videoPickGallery();
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadVideo();
            }
        });
    }
    private void videoPickGallery(){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityIfNeeded(Intent.createChooser(intent,"select video"),VIDEO_GALLERY);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == VIDEO_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            MediaController vidControl = new MediaController(addVideo.this);
            vidControl.setAnchorView(videoView);
            videoView.setMediaController(vidControl);
            videoView.start();
        }
    }
    private void uploadVideo() {
        if (videoUri != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            Calendar cal= Calendar.getInstance();
            SimpleDateFormat month = new SimpleDateFormat("MMM");
            SimpleDateFormat date = new SimpleDateFormat("dd");
            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            String monthString = month.format(cal.getTime());
            String dayString = date.format(cal.getTime());
            String yearString = year.format(cal.getTime());
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference ref = storageReference.child("posts").child("posts_video").child(firebaseUser.getUid()+"_"+currentDateandTime+".mp4");
            ref.putFile(videoUri).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    DocumentReference docRef = db.collection("User").document(firebaseUser.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null) {
                                                    name = document.getString("NAME");

                                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            firebase_video_uri = uri.toString();
                                                            //count existing posts
                                                            //upload url in next count
                                                            String databaseReference_id= FirebaseDatabase.getInstance().getReference("Post").push().getKey();
                                                            assert databaseReference_id != null;
                                                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Post").child(databaseReference_id);
                                                            Map<String,Object> data = new HashMap<>();
                                                            data.put("Post_url", firebase_video_uri);
                                                            data.put("Uploader_uid",firebaseUser.getUid().toString());
                                                            data.put("Uploader_img","q");
                                                            data.put("Uploader_name",name);
                                                            data.put("Caption",caption.getText().toString());
                                                            data.put("Post_id",databaseReference_id);
                                                            data.put("Post_time",dayString+" "+monthString+" "+yearString);
                                                            data.put("Post_type","video");
                                                            databaseReference.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    final String upload = db.collection("User").document(firebaseUser.getUid())
                                                                            .collection("uploads").document().getId();

                                                                    FirebaseFirestore.getInstance().collection("User")
                                                                            .document(firebaseUser.getUid())
                                                                            .collection("uploads")
                                                                            .document(upload)
                                                                            .set(data)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    progressDialog.dismiss();
                                                                                    Toast.makeText(addVideo.this, "Video Uploaded!!",Toast.LENGTH_SHORT).show();
                                                                                    uploadVideo.setEnabled(false);
                                                                                    showpopup_complete();

                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getApplicationContext(), "check network connection", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            } else {
                                                Log.d("LOGGER", "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(addVideo.this, "Image Upload Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }else {

        }
    }
    private void showpopup_complete(){
        dialog.setContentView(R.layout.uploaded_dailog);
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
                finish();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        finish();
    }
}