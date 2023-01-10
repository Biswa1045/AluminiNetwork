package com.biswa1045.alumininetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.sax.Element;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


//import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

  public class GetUserInfoActivity extends AppCompatActivity {
    private  static final String TAG="GetUserInfo";
    private  static final String KEY_NAME="NAME";
    private  static final String KEY_EMAIL="EMAIL";
    private  static final String KEY_GENDER="GENDER";
    private  static final String KEY_BATCH="PASSOUT";
    private  static final String KEY_BRANCH="BRANCH";
    private  static final String KEY_ADD="ADDRESS";
    private  static final String KEY_SPE="SPECIALISATION";

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    EditText t2;
    EditText t3;
    EditText t4;
    RadioButton radioButton;
    RadioButton radioButton2;
    Button button2;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner4;
String[] branch={"computer science","electronics and telecommunication",
                   "mechanical","electrical","civil","mettalurgy","chemical","production"};
String[] spe={"management","web developer","android developer","data analyst","testing"
                ,"networking","other"};
    String[] year={"1991","1992","1993"
                 ,"1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005"
                ,"2006","2007","2008","2009","2010","2011","2012","2013","2014"
                 ,"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);
        button2=findViewById(R.id.button2);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);



        ArrayAdapter<String> adapter=new ArrayAdapter<String>(GetUserInfoActivity.this, android.R.layout.simple_spinner_item,branch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter3=new ArrayAdapter<String>(GetUserInfoActivity.this, android.R.layout.simple_spinner_item,year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(GetUserInfoActivity.this, android.R.layout.simple_spinner_item,spe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  public void saveNote(View v){
                    String NAME=t2.getText().toString();
                    String EMAIL=t3.getText().toString();
                    String ADDRESS=t4.getText().toString();
                    String gender= radioButton.getTransitionName();
                    String gender2=radioButton2.getTransitionName();
                    String BATCH=spinner.getTransitionName();
                    String BRANCH= spinner2.getTransitionName();
                    String spe= spinner4.getTransitionName();
                    Map<String,Object> note=new HashMap<>();
                    note.put(KEY_NAME,NAME);
                    note.put(KEY_EMAIL,EMAIL);
                    note.put(KEY_ADD,ADDRESS);
                    note.put(KEY_BATCH,BATCH);
                    note.put(KEY_BRANCH,BRANCH);
                    note.put(KEY_SPE,spe);
                    note.put(KEY_GENDER,gender);

                    db.collection("info").document("details").set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(GetUserInfoActivity.this, "information saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GetUserInfoActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG,e.toString());
                                }
                            });
                }

        });


    }
}