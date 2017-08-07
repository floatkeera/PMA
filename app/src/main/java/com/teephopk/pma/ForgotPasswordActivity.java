package com.teephopk.pma;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends BaseAuthenticationActivity {


    ImageView mImgDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) toolbar.findViewById(R.id.toolbar_title);
        initializeToolbar(getString(R.string.forgotpasswordtitle));


        mEmailView = (CustomEditText) findViewById(R.id.email);
        mDateTxt = (CustomEditText) findViewById(R.id.datetxt);
        mLoginFormView = findViewById(R.id.forgot_form);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mLoadingText = (CustomTitleTextView) findViewById(R.id.txtrequestingreset);
        goBtn = (CustomButton) findViewById(R.id.pwreset_button);
        mImgDone = (ImageView) findViewById(R.id.img_done);

        initializeDatePicker();
        initializeLoader();

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPassword();
            }
        });

        mAuth = FirebaseAuth.getInstance();


    }

    private void requestPassword() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String bday = mDateTxt.getText().toString();


        boolean cancel = false;
        View focusView = null;

        mEmailView.setError(null);
        mDateTxt.setError(null);


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        mUserRef = mRootRef.child("userList");


        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);


            mUserRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {


                            Log.d("FORGOTPW", "Achieved");


                            if (ds.child("birthday").getValue(String.class).equals(bday)) {
                                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mProgressView.setVisibility(View.GONE);
                                        mImgDone.setVisibility(View.VISIBLE);
                                        mLoadingText.setText(getString(R.string.pwsent));

                                    }
                                });

                            } else {
                                showProgress(false);
                                mDateTxt.setError(getString(R.string.error_incorrect_bday));

                            }
                        }
                    } else {
                        showProgress(false);
                        mEmailView.setError(getString(R.string.error_invalid_email));
                        mEmailView.requestFocus();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }


}
