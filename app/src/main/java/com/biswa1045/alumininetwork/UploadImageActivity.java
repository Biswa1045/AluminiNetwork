package com.biswa1045.alumininetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;
public class UploadImageActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 22;
    private final String SAMPLE_CROPPED_IMG = "CropFact";
    FirebaseStorage storage;
    private FirebaseFirestore db;
    StorageReference storageReference;
    private FirebaseUser firebaseUser;
    ImageView img;
    String firebase_img_uri;
    Uri filePath,imguri_crped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

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
  /*  private void uploadImage() {
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
            StorageReference ref = storageReference.child("posts").child(firebaseUser.getUid()+"_"+currentDateandTime+".jpg");
            ref.putFile(imguri_crped).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    //download url of post
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
                                            data.put("Uploader_img",);
                                            data.put("Uploader_name",);
                                            data.put("Likes",);
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
                                                                    startActivity(getIntent());
                                                                    finish();
                                                                    overridePendingTransition(0, 0);

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

                                    // Image uploaded successfully
                                    // Dismiss dialog
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
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }else {

        }
    }*/

}