package com.teephopk.pma;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.NumberFormat;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProductActivity extends BaseActivityWithToolbar {

    Product thisproduct;
    CustomTitleTextView productTitleView;
    CustomTextView productPriceView;
    CustomTextView productDescView;
    ImageView productImgView;
    RatingBar ratingBar;
    CustomTextView ratingtxt;
    Button btnAddCart;
    NumberPicker numberPicker;
    View parentLayout;
    CustomTextView discountPriceView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);

        thisproduct = getIntent().getParcelableExtra("CHOSEN_PRODUCT");
        initializeToolbar(thisproduct.name);

        productTitleView = (CustomTitleTextView) findViewById(R.id.product_title);
        productDescView = (CustomTextView) findViewById(R.id.product_description);
        productImgView = (ImageView) findViewById(R.id.img_product);
        productPriceView = (CustomTextView) findViewById(R.id.product_price);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingtxt = (CustomTextView) findViewById(R.id.ratingtxt);
        btnAddCart = (CustomButton) findViewById(R.id.btn_cart);
        numberPicker = (NumberPicker) findViewById(R.id.numpicker);
        discountPriceView = (CustomTextView) findViewById(R.id.discount);

        parentLayout = findViewById(android.R.id.content);

        productTitleView.setText(thisproduct.name);
        productPriceView.setText(NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(thisproduct.price));
        productDescView.setText(thisproduct.description);
        ratingBar.setRating(thisproduct.rating);

        double discount = thisproduct.discount;

        if(discount != 0){
            productPriceView.setPaintFlags(productPriceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discountPriceView.setVisibility(View.VISIBLE);
            discountPriceView.setText( NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(thisproduct.price - (thisproduct.price*(discount/100))) + " (-" + String.valueOf((int)Math.round(discount))+"%)");
        }


        GlideApp.with(this)
                .load(thisproduct.photoURL)
                .fitCenter()
                .transition(withCrossFade())
                .into(productImgView);

        ratingtxt.setText(String.valueOf(thisproduct.rating));

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final double effectivePrice = (thisproduct.price - (thisproduct.price*(thisproduct.discount/100)));

                badgeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop));
                final CartProduct tba = new CartProduct(thisproduct.UID, numberPicker.getValue(), (effectivePrice * numberPicker.getValue()), thisproduct.name);
                DatabaseReference mCartRef = mRootRef.child("usersList").child(mAuth.getCurrentUser().getUid()).child("cart");

                final DatabaseReference mCartProductRef = mCartRef.child(tba.productUID);

                mCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(thisproduct.UID).exists()) {

                            int originalVal = dataSnapshot.child(thisproduct.UID).child("qty").getValue(Integer.class);
                            mCartProductRef.setValue(new CartProduct(thisproduct.UID, originalVal + numberPicker.getValue(), effectivePrice * (originalVal + numberPicker.getValue()), thisproduct.name), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        addToCartSuccess();
                                    }
                                }
                            });
                        } else {

                            mCartProductRef.setValue(tba, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        addToCartSuccess();
                                    }
                                }


                            });
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


    }

    public void addToCartSuccess() {
        Snackbar.make(parentLayout, thisproduct.name + " " + getString(R.string.addedtocart), Snackbar.LENGTH_LONG).setAction(getString(R.string.viewcart), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(a);
                overridePendingTransition(R.anim.down, R.anim.fade_out);
            }
        }).show();
    }


}
