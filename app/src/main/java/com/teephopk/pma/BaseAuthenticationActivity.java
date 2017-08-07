package com.teephopk.pma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

/**
 * Created by floatkeera on 7/31/17.
 */

public class BaseAuthenticationActivity extends BaseActivityWithToolbar {

    CustomEditText mDateTxt;
    DatePickerDialog datePickerDialog;
    CustomEditText mEmailView;
    CustomEditText mPasswordView;
    CustomEditText mRepeatPasswordView;
    CustomEditText mNameView;


    CustomButton goBtn;
    CustomButton secondaryBtn;

    View mLoginFormView;
    ProgressBar mProgressView;
    CustomTitleTextView mLoadingText;

    DatabaseReference mUserRef;

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingText.setVisibility(show? View.VISIBLE: View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mLoadingText.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingText.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    void initializeDatePicker(){
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePickerDialog.dismiss();
                mDateTxt.setText(String.valueOf(i2) + "/"+String.valueOf(i1+1) + "/" + String.valueOf(i));

            }
        }, 2000, 0, 1);

        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        long a = now - 410240038000L;


        datePickerDialog.getDatePicker().setMaxDate(a);

        mDateTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    datePickerDialog.show();
            }
        });

        mDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog.show();
            }
        });
    }

    void initializeLoader(){
        cubeGrid = new CubeGrid();
        cubeGrid.setColor(getResources().getColor(R.color.colorAccent));
        mProgressView.setIndeterminateDrawable(cubeGrid);
    }



}
