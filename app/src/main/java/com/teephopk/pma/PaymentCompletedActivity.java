package com.teephopk.pma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PaymentCompletedActivity extends BaseActivityWithToolbar {


    View view;
    CustomButton continuebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_completed);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);
        initializeToolbar("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        view = findViewById(R.id.view1);

        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_fade);
        view.startAnimation(animFadein);
        view.setVisibility(View.VISIBLE);

        continuebtn =  (CustomButton) findViewById(R.id.continuebtn);
        Animation animPopin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popcompletebtn);
        continuebtn.startAnimation(animPopin);
        animPopin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                continuebtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentCompletedActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.up);
            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_activity_menu; this adds items to the action bar if it is present.


        return true;

    }

    @Override
    public void onBackPressed() {

    }
}
