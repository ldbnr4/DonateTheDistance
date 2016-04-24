package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class CharityDetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_detail_view);
        Intent intent = getIntent();
        String charity = intent.getStringExtra("Charity");
        System.out.println(charity);
        /*
        ((TextView)findViewById(R.id.charName)).setText(charity.name);
        ((TextView)findViewById(R.id.charDesc)).setText(charity.description);
        ((ImageView)findViewById(R.id.charImg)).setImageResource(charity.img);
         */
    }
}
