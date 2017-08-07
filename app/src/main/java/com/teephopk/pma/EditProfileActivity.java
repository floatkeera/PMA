package com.teephopk.pma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfileActivity extends BaseActivityWithToolbar {


    StorageReference storageRef;
    StorageReference imageRef;

    final static int PICK_IMAGE = 100;
    final static int IMAGE_CHANGED = 999;

    final static int NAME_CHANGED = 888;


    FirebaseUser mUser;

    ImageView imageView;

    Uri imageUri;

    ProgressDialog dialog;

    CustomButton btnConfirm;

    UserProfileChangeRequest profileUpdates;
    EditText mName;

    CustomTitleTextView txtChangePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mUser = FirebaseAuth.getInstance().getCurrentUser();




        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageRef = storageRef.child("users");


        String UID = mUser.getUid();
        imageRef = imageRef.child(UID+"/dp.png");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView)findViewById(R.id.toolbar_title);

        imageView = (ImageView) findViewById(R.id.dpImageView);

        txtChangePhoto = (CustomTitleTextView) findViewById(R.id.changephototxt);

        initializeToolbar("Update my profile");
        mName = (EditText)findViewById(R.id.name);

       if(getIntent().getStringExtra("IMAGEURI") != null){
           imageUri = Uri.parse(getIntent().getStringExtra("IMAGEURI"));

           GlideApp.with(EditProfileActivity.this)
                   .load(imageUri)
                   .apply(new RequestOptions().circleCrop())
                   .placeholder(R.drawable.progress_animation)
                   .into(imageView);
       }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, PICK_IMAGE);
            }
        });

        mName.setText(mUser.getDisplayName());

        txtChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, PICK_IMAGE);
            }
        });

        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if(i == EditorInfo.IME_ACTION_DONE)
                        mName.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mName.getWindowToken(), 0);

                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_activity_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setTitle("Please wait.");
            dialog.setMessage("We're updating your profile...");
            CubeGrid cubeGrid = new CubeGrid();
            cubeGrid.setColor(getResources().getColor(R.color.colorAccent));

            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setIndeterminateDrawable(cubeGrid);

            dialog.show();

            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mName.getText().toString()).build();


            if(!imageUri.toString().equals(getIntent().getStringExtra("IMAGEURI"))) {
                UploadTask uploadTask = imageRef.putFile(imageUri);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        setResult(IMAGE_CHANGED);
                        updateProfile(profileUpdates);

                    }
                });
            } else if(!mName.getText().toString().equals(mUser.getDisplayName())){
                setResult(NAME_CHANGED);
                updateProfile(profileUpdates);

            } else{
                dialog.dismiss();
                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions().circleCrop())
                    .into(imageView);



        }
    }

    public void updateProfile(UserProfileChangeRequest profileUpdates) {
        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            dialog.dismiss();



                            finish();


                        }
                    }
                });
    }
}
