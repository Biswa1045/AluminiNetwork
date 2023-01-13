package com.biswa1045.alumininetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

import org.checkerframework.common.subtyping.qual.Bottom;

public class UploadImageActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 22;
    private final String SAMPLE_CROPPED_IMG = "CropPost";
    FirebaseStorage storage;
    private FirebaseFirestore db;
    StorageReference storageReference;
    private FirebaseUser firebaseUser;
    ImageView img;
    String firebase_img_uri;
    Uri filePath,imguri_crped;
    String name;
    EditText caption;
    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        SelectImage();
        db = FirebaseFirestore.getInstance();
        img = findViewById(R.id.image_upload);
        caption = findViewById(R.id.caption);
        post = findViewById(R.id.post_image);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }
    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG;
        destinationFileName +=".jpg";
        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        uCrop.withAspectRatio(2,3);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(UploadImageActivity.this);
    }

    private UCrop.Options getCropOptions(){

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(98);
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setStatusBarColor(getResources().getColor(R.color.blue));
        options.setToolbarColor(getResources().getColor(R.color.blue));
        options.setToolbarTitle("cropping");
        return options;
    }

    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            startCrop(filePath);
        }else if(requestCode == UCrop.REQUEST_CROP) {
            imguri_crped = UCrop.getOutput(data);
            if (imguri_crped != null) {
                img.setImageURI(imguri_crped);
            }
        }else if(requestCode ==UCrop.RESULT_ERROR){
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(UploadImageActivity.this, "please try again later", Toast.LENGTH_SHORT).show();
        }


    }
    private void uploadImage() {
        if (imguri_crped != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference ref = storageReference.child("posts").child(firebaseUser.getUid()+"_"+currentDateandTime+".jpg");
            ref.putFile(imguri_crped).addOnSuccessListener(
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
                                                            firebase_img_uri = uri.toString();
                                                            //count existing posts
                                                            //upload url in next count
                                                            String databaseReference_id= FirebaseDatabase.getInstance().getReference("Post").push().getKey();
                                                            assert databaseReference_id != null;
                                                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Post").child(databaseReference_id);
                                                            Map<String,Object> data = new HashMap<>();
                                                            data.put("Post_url",firebase_img_uri);
                                                            data.put("Uploader_uid",firebaseUser.getUid().toString());
                                                            data.put("Uploader_img","q");
                                                            data.put("Uploader_name",name);
                                                            data.put("Likes","0");
                                                            data.put("Caption",caption.getText().toString());
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
                                                                                    Toast.makeText(UploadImageActivity.this, "Image Uploaded!!",Toast.LENGTH_SHORT).show();
                                                                                    post.setEnabled(false);

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
                            Toast.makeText(UploadImageActivity.this, "Image Upload Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        finish();
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        finish();
    }
}