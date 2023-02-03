package com.biswa1045.alumininetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class clubs extends AppCompatActivity {

    CardView cardView;
    ImageButton image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);
        cardView=findViewById(R.id.cardView);
        image1=findViewById(R.id.image1);
        image1.setVisibility(View.GONE);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.setVisibility(View.VISIBLE);
                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       gotUrl("https://www.instagram.com/drishya.official/");
                    }
                });
            }
        });

    }
    private void gotUrl(String s) {
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    public void back_clubs(View view) {
        onBackPressed();
    }
}
