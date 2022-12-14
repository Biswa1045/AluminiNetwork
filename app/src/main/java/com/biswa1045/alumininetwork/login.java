package com.biswa1045.alumininetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class login extends AppCompatActivity {

    CountryCodePicker cpp;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        cpp=findViewById(R.id.cpp);
        cpp.registerCarrierNumberEditText(editText);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")){
                    Intent intent=new Intent(login.this,manageotp.class);
                    intent.putExtra("mobile",cpp.getFullNumberWithPlus().replace(" ",""));
                    startActivity(intent);
                }else{
                    Toast.makeText(login.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}