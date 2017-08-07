package com.teephopk.pma;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by floatkeera on 7/27/17.
 */

public class BaseActivityWithToolbar extends AppCompatActivity {

    Toolbar toolbar;
    CustomTitleTextView mToolbarTxt;
    CubeGrid cubeGrid;
    View view;

    FirebaseAuth mAuth;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);






    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_right, R.anim.exit_right);

    }

    public void initializeToolbar(String title){





        setSupportActionBar(toolbar);

        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTxt.setText(title);


    }

    public void changeTitle(String title){

        mToolbarTxt.setText(title);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public int getStatusBarHeight() {
        int result = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }


    void animateIntent(){
        this.overridePendingTransition(R.anim.enter_left, R.anim.exit_left);
    }
}
