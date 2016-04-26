package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivitySelectionView extends AppCompatActivity {

    public static final String SELECTED_WORKOUT = "selected workout";
    public static final String SELECTED_CHARITY = "selected charity";
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_selection_view);

        //Get message from intent
        Intent intent = getIntent();
        message = intent.getStringExtra(CharitySelectionView.SELECTED_CHARITY);

        //Create the text view
        TextView textView = (TextView) findViewById(R.id.charityText);

        assert textView != null;
        textView.setText(message);


        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        assert mToolBar != null;
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.charityImage);

        if (message.equals(getString(R.string.charity_water_name_txt))) {
            assert imageView != null;
            imageView.setImageResource(R.drawable.charity_water);
        } else if (message.equals(getString(R.string.habitat_name_txt))) {
            assert imageView != null;
            imageView.setImageResource(R.drawable.habitat_for_humanity_small);
        } else if (message.equals(getString(R.string.humane_name_txt))) {
            assert imageView != null;
            imageView.setImageResource(R.drawable.the_humane_society_small);
        } else if (message.equals(getString(R.string.stand_up_name_txt))) {
            assert imageView != null;
            imageView.setImageResource(R.drawable.stand_up_to_cancer_small);
        }

    }

    public void runSelection(View view) {
        finish();
        Intent intent = new Intent(this, RunWorkoutView.class);
        intent.putExtra(SELECTED_WORKOUT, getString(R.string.run_txt));
        intent.putExtra(SELECTED_CHARITY, message);
        startActivity(intent);
    }
}
