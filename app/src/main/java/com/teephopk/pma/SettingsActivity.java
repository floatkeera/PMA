package com.teephopk.pma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends BaseActivityWithToolbar {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);
        initializeToolbar(getString(R.string.action_settings));

        




    }
}
