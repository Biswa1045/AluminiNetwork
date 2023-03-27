package com.biswa1045.alumininetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class clubs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);

    }
    private void gourl(String s) {
        try{
            Uri uri=Uri.parse(s);
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    public void back_clubs(View view) {
        onBackPressed();
    }

    public void drishya_linkedin(View view) {
        gourl("");
    }

    public void drishya_youtube(View view) {
        gourl("");
    }

    public void drishya_insta(View view) {
        gourl("");
    }
}
