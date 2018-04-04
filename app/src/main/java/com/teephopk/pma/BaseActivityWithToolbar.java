package com.teephopk.pma;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by floatkeera on 7/27/17.
 */

public class BaseActivityWithToolbar extends AppCompatActivity {

    Toolbar toolbar;
    CustomTitleTextView mToolbarTxt;
    CubeGrid cubeGrid;
    View view;

    RelativeLayout badgeLayout;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("usersList");
    ArrayList<CartProduct> cartProducts = new ArrayList<CartProduct>();
    int cartQty;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_activity_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));




        MenuItem itemCart = menu.findItem(R.id.action_cart);
         badgeLayout = (RelativeLayout) itemCart.getActionView();
        final TextView itemCartTextView = (TextView) badgeLayout.findViewById(R.id.badge_textView);
        itemCartTextView.setVisibility(View.GONE); // initially hidden

        mUserRef.orderByKey().equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cartQty = 0;

                // If nothing in the cart we hide the notification bubble
                if(!dataSnapshot.hasChildren()){
                    itemCartTextView.setVisibility(View.GONE);
                }

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    UserTest test  = ds.getValue(UserTest.class);
                    if(test.cart != null) {
                        Log.i("CART", "Cart display");
                        cartProducts = new ArrayList<CartProduct>(test.cart.values());

                        for (CartProduct a : cartProducts) {
                            cartQty = cartQty + a.qty;
                        }

                        itemCartTextView.setText("" + String.valueOf(cartQty));
                        itemCartTextView.setVisibility(View.VISIBLE);

                    } else {
                        itemCartTextView.setVisibility(View.GONE);
                        Log.i("CART", "Cart gone");
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(BaseActivityWithToolbar.this,CartActivity.class);
                startActivity(a);
                overridePendingTransition(R.anim.down, R.anim.fade_out);
            }
        });





        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            //noinspection SimplifiableIfStatement
            case R.id.action_cart :

            /*
            if (cartFragment.isVisible() && cartPressed) {
                onBackPressed();
            } else if (!cartPressed && !cartFragment.isVisible()) {
                cartPressed = true;

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.animator.down, R.animator.fade_out, R.animator.fade_in, R.animator.up);

                ft.replace(R.id.fragment_container, cartFragment, "CART");
                ft.addToBackStack(null);
                ft.commit();
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
            } else {

                onBackPressed();


            }
            */

                Intent a = new Intent(this,CartActivity.class);
                startActivity(a);
                overridePendingTransition(R.anim.down, R.anim.fade_out);


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

    boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
       return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
