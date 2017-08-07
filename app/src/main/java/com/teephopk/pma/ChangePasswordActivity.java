package com.teephopk.pma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class ChangePasswordActivity extends BaseActivityWithToolbar {

    CustomEditText oldpw;
    CustomEditText newpw;
    CustomEditText verifypw;

    ProgressDialog dialog;

    View fbnoform;
    View changepwform;

    ImageView imageView;
    CustomTitleTextView title;
    CustomTextView body;

    FirebaseUser user;

    Menu thismenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);
        initializeToolbar(getString(R.string.action_changepw));

        fbnoform = findViewById(R.id.fbnoform);
        changepwform = findViewById(R.id.changepwform);

        imageView = (ImageView) findViewById(R.id.fbimg);
        title = (CustomTitleTextView) findViewById(R.id.fbtitle);
        body = (CustomTextView) findViewById(R.id.fbbody);

        if (facebookLoggedIn()) {
            changepwform.setVisibility(View.GONE);
            fbnoform.setVisibility(View.VISIBLE);

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(facebookLoggedIn()) {
                    Log.d("CHANGEPW", "CLICKED");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/settings/security/password"));
                    startActivity(intent);
                }
            }
        });



        oldpw = (CustomEditText) findViewById(R.id.oldpassword);
        newpw = (CustomEditText) findViewById(R.id.newpassword);
        verifypw = (CustomEditText) findViewById(R.id.verifypassword);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!facebookLoggedIn()) {
            // Inflate the main_activity_menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.save_menu, menu);

        }

        thismenu = menu;

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        super.onOptionsItemSelected(item);


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            // Reset errors.
            oldpw.setError(null);
            newpw.setError(null);
            verifypw.setError(null);


            // Store values at the time of the login attempt.

            final String oldpassword = oldpw.getText().toString();
            final String newpassword = newpw.getText().toString();
            final String repeatPassword = verifypw.getText().toString();

            boolean cancel = false;
            View focusView = null;


            Zxcvbn zxcvbn = new Zxcvbn();
            Strength pwStrength = zxcvbn.measure(newpassword);

            if (TextUtils.isEmpty(oldpassword)) {
                oldpw.setError(getString(R.string.error_field_required));
                focusView = oldpw;
                cancel = true;
            }
            if (TextUtils.isEmpty(newpassword)) {
                newpw.setError(getString(R.string.error_field_required));
                focusView = newpw;
                cancel = true;
            }

            if (pwStrength.getScore() < 2) {
                newpw.setError(getString(R.string.error_invalid_password));
                focusView = newpw;
                cancel = true;
            }

            if (!newpassword.equals(repeatPassword)) {
                verifypw.setError(getString(R.string.error_passwordnotsame));
                focusView = verifypw;
                cancel = true;
            }

            if (!cancel) {
                attemptChange(oldpassword, newpassword);


            } else {
                focusView.requestFocus();
            }


        }

        return true;
    }

    public boolean facebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    void attemptChange(final String oldPassword, final String newPassword) {
        dialog = new ProgressDialog(ChangePasswordActivity.this);
        dialog.setTitle(getString(R.string.pleasewait));
        dialog.setMessage(getString(R.string.changingpw));
        CubeGrid cubeGrid = new CubeGrid();
        cubeGrid.setColor(getResources().getColor(R.color.colorAccent));

        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setIndeterminateDrawable(cubeGrid);

        dialog.show();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        changepwform.setVisibility(View.GONE);
                        fbnoform.setVisibility(View.VISIBLE);
                        dialog.dismiss();

                        imageView.setImageDrawable(getDrawable(R.drawable.done));
                        title.setText(getString(R.string.changepwdonetitle));
                        body.setText(getString(R.string.changepwdonebody));

                        thismenu.findItem(R.id.action_save).setVisible(false);


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                oldpw.setError(getString(R.string.error_incorrect_password));
                oldpw.requestFocus();
            }
        });


    }
}

