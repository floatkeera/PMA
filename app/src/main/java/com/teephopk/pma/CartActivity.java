package com.teephopk.pma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends BaseActivityWithToolbar {


    FirebaseAuth mAuth;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> cart;

    UserTest test;
    ListView lvtest;
    CartAdapter adapter;
    View listFooter;
    int qty;
    double total;

    CustomTitleTextView totalText;
    CustomTitleTextView totalPrice;
    CustomButton checkoutButton;



    View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);
        initializeToolbar(getString(R.string.cart));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        cart = new ArrayList<String>();



        empty = findViewById(R.id.noitemview);

        DatabaseReference mUserRef = mRef.child("usersList");
        mAuth = FirebaseAuth.getInstance();
        lvtest = (ListView) findViewById(R.id.lvtest);

        showEmpty(true);

        adapter = new CartAdapter(CartActivity.this, R.layout.cart_row, cartProducts);
        lvtest.setAdapter(adapter);

        listFooter = getLayoutInflater().inflate(R.layout.cart_footer, null, false);

        totalText = (CustomTitleTextView) listFooter.findViewById(R.id.totaltext);
        totalPrice = (CustomTitleTextView) listFooter.findViewById(R.id.totalprice);
        checkoutButton = (CustomButton) listFooter.findViewById(R.id.checkoutbtn);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(CartActivity.this, CheckoutActivity.class);
                a.putExtra("ITEMS", qty);
                a.putExtra("TOTAL", total);
                a.putParcelableArrayListExtra("CARTPRODUCTS", cartProducts);
                startActivity(a);
                animateIntent();
            }
        });


        lvtest.addFooterView(listFooter);





        mUserRef.orderByKey().equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // If there is nothing in the cart
                if(!dataSnapshot.hasChildren()){
                    showEmpty(true);
                    cartProducts.clear();
                    adapter.notifyDataSetChanged();

                    if(lvtest.getFooterViewsCount() != 0){
                        lvtest.removeFooterView(listFooter);
                    }
                }


                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    UserTest test = ds.getValue(UserTest.class);

                    qty = 0;
                    total = 0;


                    if(test.cart!= null){

                        if(lvtest.getFooterViewsCount() != 1){
                            lvtest.addFooterView(listFooter);
                        }

                        cartProducts.clear();
                        showEmpty(false);

                        for (CartProduct cp : test.cart.values()){
                            cartProducts.add(cp);
                            total = total+ cp.subTotal;
                            qty = qty+ cp.qty;

                        }





                        adapter.notifyDataSetChanged();




                    } else{
                        showEmpty(true);
                        cartProducts.clear();
                        adapter.notifyDataSetChanged();

                        if(lvtest.getFooterViewsCount() != 0){
                            lvtest.removeFooterView(listFooter);
                        }
                    }

                    totalText.setText(getString(R.string.totaltext) + " (" + String.valueOf(qty) + " " + getString(R.string.items) + "): ");
                    totalPrice.setText(NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(total));

                    totalPrice.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop));






                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_activity_menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.cart_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_close) {
            finish();
        }

        return true;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    void showEmpty(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            lvtest.setVisibility(show ? View.GONE : View.VISIBLE);
            lvtest.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    lvtest.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            empty.setVisibility(show ? View.VISIBLE : View.GONE);

            empty.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    empty.setVisibility(show ? View.VISIBLE : View.GONE);

                }
            });

        }
    }

}
