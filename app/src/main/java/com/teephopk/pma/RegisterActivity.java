package com.teephopk.pma;

import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class RegisterActivity extends BaseAuthenticationActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) toolbar.findViewById(R.id.toolbar_title);
        initializeToolbar(getString(R.string.registertitle));

        mEmailView = (CustomEditText) findViewById(R.id.email);
        mPasswordView = (CustomEditText) findViewById(R.id.password);
        mRepeatPasswordView = (CustomEditText) findViewById(R.id.repeatpassword);
        mDateTxt = (CustomEditText) findViewById(R.id.datetxt);
        mNameView = (CustomEditText) findViewById(R.id.name);
        mLoadingText = (CustomTitleTextView) findViewById(R.id.txtRegistering);
        mProgressView = (ProgressBar) findViewById(R.id.register_progress);
        mLoginFormView = findViewById(R.id.registerform);
        goBtn = (CustomButton) findViewById(R.id.email_register_button);

        mUserRef = mRootRef.child("userList");
        mAuth = FirebaseAuth.getInstance();

        initializeDatePicker();
        initializeLoader();

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateAccount();
            }
        });


    }


    private void attemptCreateAccount(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRepeatPasswordView.setError(null);
        mNameView.setError(null);
        mDateTxt.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String repeatPassword = mRepeatPasswordView.getText().toString();
        final String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        Zxcvbn zxcvbn = new Zxcvbn();
        Strength pwStrength = zxcvbn.measure(password);

        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (pwStrength.getScore() < 2) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if(!password.equals(repeatPassword)){
            mRepeatPasswordView.setError(getString(R.string.error_passwordnotsame));
            focusView = mRepeatPasswordView;
            cancel = true;
        }

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

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                final FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    mUserRef = mUserRef.child(user.getUid()).child("birthday");
                                                    mUserRef.setValue(mDateTxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                                                            startActivity(intent);
                                                            showProgress(false);
                                                            finish();
                                                        }
                                                    });




                                                }
                                            }
                                        });

                            } else {
                                showProgress(false);
                                    //If user alreadyAw exist
                                mEmailView.setError(getString(R.string.accountexists));
                                mEmailView.requestFocus();

                                }

                            }
                    });

        }
    }


}
