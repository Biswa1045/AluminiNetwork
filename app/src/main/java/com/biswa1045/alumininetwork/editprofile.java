package com.biswa1045.alumininetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {

    private  static final String KEY_NAME="NAME";
    private  static final String KEY_EMAIL="EMAIL";
    private  static final String KEY_GENDER="GENDER";
    private  static final String KEY_BATCH="PASSOUT BATCH";
    private  static final String KEY_BRANCH="BRANCH";
    private  static final String KEY_ADD="ADDRESS";
    private  static final String KEY_ID="UID";
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    TextView ps1,ps2;
    ImageButton backbtn;
    ImageView profile;
    EditText name,gmail;
    RadioGroup gender;
    Spinner branch,batch;
    EditText address;
    Button update;
    private static final int IMAGE_CODE=400;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    String gender_s;
    String branch_spn;
    String batch_spn;
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

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

        firebaseAuth=FirebaseAuth.getInstance();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        name=findViewById(R.id.name);
        gmail=findViewById(R.id.gmail);
        gender=findViewById(R.id.gender_group);
        update=findViewById(R.id.update);
        branch=findViewById(R.id.branch);
        batch=findViewById(R.id.batch);
        address=findViewById(R.id.address);
        ps1=findViewById(R.id.ps1);
        ps2=findViewById(R.id.ps2);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("User");
            ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String NAME=""+snapshot.child("NAME").getValue();
                                    String EMAIL=""+snapshot.child("EMAIL").getValue();
                                    String ADDRESS=""+snapshot.child("ADDRESS").getValue();
                                    String BATCH=""+snapshot.child("BATCH").getValue();
                                    String BRANCH=""+snapshot.child("BRANCH").getValue();
                                    String GENDER=""+snapshot.child("GENDER").getValue();
                                    String UID=""+snapshot.child("uid").getValue();

                                    name.setText(NAME);
                                    gmail.setText(EMAIL);
                                    address.setText(ADDRESS);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,branch_arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter);
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ps2.setVisibility(View.GONE);
                String branch_spn=parent.getItemAtPosition(position).toString();
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
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ps1.setVisibility(View.GONE);
                String batch_spn=parent.getItemAtPosition(position).toString();

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
                    db.collection("User").document(uid).set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(editprofile.this, "information updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(editprofile.this,ProfileFragment.class));
                                    finish();
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

            }
        });

    }

        }
