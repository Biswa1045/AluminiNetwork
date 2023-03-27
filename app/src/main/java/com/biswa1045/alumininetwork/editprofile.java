package com.biswa1045.alumininetwork;

import static com.biswa1045.alumininetwork.MainActivityKt.ADDRESS;
import static com.biswa1045.alumininetwork.MainActivityKt.EMAIL;
import static com.biswa1045.alumininetwork.MainActivityKt.NAME;
import static com.biswa1045.alumininetwork.MainActivityKt.PHOTO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class editprofile extends AppCompatActivity {
    private String firebase_img_uri;
    private  static final String KEY_NAME="NAME";
    private  static final String KEY_EMAIL="EMAIL";
    private  static final String KEY_GENDER="GENDER";
    private  static final String KEY_BATCH="PASSOUT BATCH";
    private  static final String KEY_BRANCH="BRANCH";
    private  static final String KEY_ADD="ADDRESS";
    private  static final String KEY_ID="UID";
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    Map<String,Object> note=new HashMap<>();
    ImageView backbtn;
    ImageView profile;
    EditText name,gmail;
    RadioGroup gender;
    Spinner branch,batch;
    EditText address;
    Button update;
    private final int PICK_IMAGE_REQUEST = 22;
    private final String SAMPLE_CROPPED_IMG = "CropPost";
    private static final int IMAGE_CODE=400;
    private ProgressDialog progressDialog;

    private String PHOTO_PROFILE;
    Uri filePath,imguri_crped;
    String gender_s;
    String branch_spn;
    String batch_spn;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    String[] branch_arr={"computer science","electronics and telecommunication",
            "mechanical","electrical","civil","mettalurgy","chemical","production"};
    String[] year_arr={"1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993"
            ,"1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014"
            ,"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        backbtn=findViewById(R.id.backbtn);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        profile= findViewById(R.id.profile);
        findViewById(R.id.profile_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        name=findViewById(R.id.name_edit);
        gmail=findViewById(R.id.email_edit);
        gender=findViewById(R.id.gender_group);
        update=findViewById(R.id.update);
        branch=findViewById(R.id.branch);
        batch=findViewById(R.id.batch);
        address=findViewById(R.id.address_edit);
try {
    name.setText(NAME);
    gmail.setText(EMAIL);
    address.setText(ADDRESS);
    PHOTO_PROFILE=PHOTO;
    Glide.with(getApplicationContext()).load(PHOTO_PROFILE)
            .centerCrop()
            .error(R.drawable.person)
            .into(profile);

}catch (Exception e){
    Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
}

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,branch_arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter);
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                 branch_spn=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,year_arr);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batch.setAdapter(adapter3);
        batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                 batch_spn=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);
                gender_s = (String) radioButton.getText();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NAME=name.getText().toString();
                String EMAIL=gmail.getText().toString();
                String ADDRESS=address.getText().toString();
                String GENDER= gender_s;
                String BATCH=batch_spn;
                String BRANCH= branch_spn;
                if(imguri_crped==null) {
                    if(!NAME.equals("") &&!EMAIL.equals("") && !ADDRESS.equals("") && GENDER != null &&!BATCH.equals("") &&!BRANCH.equals("") ){
                        Map<String,Object> note=new HashMap<>();
                        note.put(KEY_NAME,NAME);
                        note.put(KEY_EMAIL,EMAIL);
                        note.put(KEY_ADD,ADDRESS);
                        note.put(KEY_BATCH,BATCH);
                        note.put(KEY_BRANCH,BRANCH);
                        note.put(KEY_GENDER,GENDER);
                        String uid = firebaseUser.getUid().toString();
                        note.put(KEY_ID,uid);
                        note.put("PHOTO", PHOTO_PROFILE +"");

                        db.collection("User").document(uid)
                                .update("search",NAME.toLowerCase())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(editprofile.this, "information updated", Toast.LENGTH_SHORT).show();
                                        // startActivity(new Intent(editprofile.this,ProfileFragment.class));
                                        // finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(editprofile.this, "Please try again later", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }else{
                        Toast.makeText(editprofile.this, "Fill all details!!!", Toast.LENGTH_SHORT).show();

                    }
                }else{

                    if(!NAME.equals("") &&!EMAIL.equals("") && !ADDRESS.equals("") && GENDER != null &&!BATCH.equals("") &&!BRANCH.equals("") ){
                        upload_img(imguri_crped);

                        note.put(KEY_NAME,NAME);
                        note.put(KEY_EMAIL,EMAIL);
                        note.put(KEY_ADD,ADDRESS);
                        note.put(KEY_BATCH,BATCH);
                        note.put(KEY_BRANCH,BRANCH);
                        note.put(KEY_GENDER,GENDER);
                        String uid = firebaseUser.getUid().toString();
                        note.put(KEY_ID,uid);


                    }else{
                        Toast.makeText(editprofile.this, "Fill all details!!!", Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });

    }
    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG;
        destinationFileName +=".jpg";
        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        uCrop.withAspectRatio(1,1);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(editprofile.this);
    }

    private UCrop.Options getCropOptions(){

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
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
                profile.setImageURI(imguri_crped);
            }
        }else if(requestCode ==UCrop.RESULT_ERROR){
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(editprofile.this, "please try again later", Toast.LENGTH_SHORT).show();
        }
    }
    private void upload_img(Uri imguri_crped){

        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference ref = storageReference.child("uploads").child(firebaseUser.getUid()+"_"+currentDateandTime+".jpg");


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
                                        String uid = firebaseUser.getUid().toString();
                                        note.put("PHOTO", firebase_img_uri);
                                        db.collection("User").document(uid).set(note)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(editprofile.this, "information updated", Toast.LENGTH_SHORT).show();
                                                        // startActivity(new Intent(editprofile.this,ProfileFragment.class));
                                                        // finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(editprofile.this, "Please try again later", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                        progressDialog.dismiss();

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
                      //  Toast.makeText(uploadedActivity.this, "Image Upload Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

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
    }

        }
