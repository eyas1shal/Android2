package com.example.assignment2_eyas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;

public class RaceActivity extends AppCompatActivity {

    private ImageView track;
    private TextView txt ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        txt = findViewById(R.id.txt);

        Intent intent = getIntent();
        txt.setText("Name "+intent.getExtras().get("race_name").toString()
                +"\nDate:"+intent.getExtras().get("race_date").toString()
                +"\nTime:"+intent.getExtras().get("race_time").toString()
                +"\nRound:"+intent.getExtras().get("race_round").toString()

        );
        track=findViewById(R.id.track);
        track.setImageDrawable(LoadImageFromWebOperations(intent.getExtras().getString("track").toString()));


    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}