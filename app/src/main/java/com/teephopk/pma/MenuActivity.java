package com.teephopk.pma;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MenuActivity extends BaseActivityWithToolbar implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;

    ProfileFragment profileFragment;
    HighlightsFragment promotionFragment;
    CatalogFragment categoryFragment;

    SettingsFragment settingsFragment;
    MyOrdersFragment myOrdersFragment;

    ImageView headerImageView;
    CustomTitleTextView headerFirstNameView;
    CustomTitleTextView headerLastNameView;

    StorageReference storageRef;
    StorageReference imageRef;
    Uri imageUri;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer;

    boolean cartPressed = false;

    Menu menu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);
        initializeToolbar("My Profile");


        mAuth = FirebaseAuth.getInstance();


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mUser = mAuth.getCurrentUser();

        profileFragment = new ProfileFragment();
        promotionFragment = new HighlightsFragment();
        categoryFragment = new CatalogFragment();

        settingsFragment = new SettingsFragment();
        myOrdersFragment = new MyOrdersFragment();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageRef = storageRef.child("users");

        String UID = mUser.getUid();
        imageRef = imageRef.child(UID + "/dp.png");

        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, promotionFragment, "HIGHLIGHTS");
        fragmentTransaction.commit();

        getFragmentManager().executePendingTransactions();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        headerImageView = (ImageView) header.findViewById(R.id.headerimageView);
        headerFirstNameView = (CustomTitleTextView) header.findViewById(R.id.firstNameTxt);
        headerLastNameView = (CustomTitleTextView) header.findViewById(R.id.lastNameTxt);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, profileFragment, "PROFILE");
                fragmentTransaction.commit();

                drawer.closeDrawer(GravityCompat.START);
            }
        });


        Glide.with(this)
                .load(R.drawable.usericon)
                .apply(new RequestOptions().fitCenter())
                .into(headerImageView);

        updateHeaderName();
        updateHeaderPicture();


    }

    @Override
    protected void onStart() {

        super.onStart();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    finish();


                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mAuthListener);
    }


    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (cartPressed) {
            super.onBackPressed();
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_cart_white));
            cartPressed = false;
        }


    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();


        if (id == R.id.nav_profile) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, profileFragment, "PROFILE");
            ft.commit();

           /* if (getFragmentManager().findFragmentByTag("PROFILE") != null) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.show(profileFragment);
                fragmentTransaction.hide(categoryFragment);
                fragmentTransaction.hide(promotionFragment);
                fragmentTransaction.commit();

                setTitle("My Profile");
            } else {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, profileFragment, "PROFILE");
                fragmentTransaction.hide(promotionFragment);
                fragmentTransaction.hide(categoryFragment);
                fragmentTransaction.commit();

                setTitle("My Profile");
            }*/
        } else if (id == R.id.nav_products) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, categoryFragment, "CATEGORY");
            ft.commit();

            /*if (getFragmentManager().findFragmentByTag("CATEGORY") != null) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.hide(profileFragment);
                fragmentTransaction.show(categoryFragment);
                fragmentTransaction.hide(promotionFragment);
                fragmentTransaction.commit();
                setTitle("Product Catalog");
            } else {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, categoryFragment, "CATEGORY");
                fragmentTransaction.hide(promotionFragment);
                fragmentTransaction.hide(profileFragment);
                fragmentTransaction.commit();
                setTitle("Product Catalog");
            }*/



        } else if (id == R.id.nav_promotions) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, promotionFragment, "HIGHLIGHTS");
            ft.commit();


        } else if (id == R.id.nav_settings) {

            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, settingsFragment, "SETTINGS");
            ft.commit();

        }
     /*        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.material_drawer_open, R.string.material_drawer_close){

                *//** Called when a drawer has settled in a completely closed state. *//*
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    animateIntent();
                    drawer.removeDrawerListener(mDrawerToggle);
                }

                *//** Called when a drawer has settled in a completely open state. *//*
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // Do whatever you want here
                }
            };
// Set the drawer toggle as the DrawerListener
            drawer.addDrawerListener(mDrawerToggle);
*/




        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();


    }

    public void updateHeaderName() {

        String fullName = mUser.getDisplayName();


        if (fullName != null) {
            String[] nameArray = fullName.split("\\s+", 2);

            if (nameArray.length > 0)
                headerFirstNameView.setText(nameArray[0]);
            if (nameArray.length > 1)
                headerLastNameView.setText(nameArray[1]);
        }

    }

    public void updateHeaderPicture() {

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                imageUri = uri;

                GlideApp.with(MenuActivity.this)
                        .load(imageUri)
                        .apply(new RequestOptions().circleCrop())
                        .placeholder(R.drawable.progress_animation)
                        .into(headerImageView);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

    }


}


