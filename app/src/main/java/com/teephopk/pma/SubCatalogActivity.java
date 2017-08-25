package com.teephopk.pma;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.blurry.Blurry;


import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SubCatalogActivity extends BaseActivityWithToolbar implements ProductDisplayAdapter.ItemClickListener {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    Category thisCategory;

    RecyclerView recyclerView;
    ProductDisplayAdapter myAdapter;
    ArrayList<Product> productArrayList;
    ImageView toolbarImage;
    CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catalog);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.getLayoutParams().height += getStatusBarHeight();
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);

        String catName = getIntent().getStringExtra("NAME");
        String UID = getIntent().getStringExtra("UID");
        String image = getIntent().getStringExtra("IMAGE");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setExpandedTitleTypeface(chooseFont(true));
        collapsingToolbarLayout.setCollapsedTitleTypeface(chooseFont(false));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.md_white_1000));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.md_white_1000));

        collapsingToolbarLayout.setTitle(catName);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        productArrayList = new ArrayList<Product>();


        /*
        DatabaseReference mCategoryList = mRootRef.child("categoryList");
        mCategoryList.orderByKey().equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                productArrayList.clear();



                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot dsa : ds.child("productList").getChildren())
                    productArrayList.add(dsa.getValue(Product.class));
                }
                myAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        toolbarImage = (ImageView)findViewById(R.id.toolbarimage);


        RequestOptions opt = new RequestOptions();



        GlideApp.with(this)
                .load(image)

                .centerCrop()
                .transition(withCrossFade())
                .into(toolbarImage);

        toolbarImage.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);



        DatabaseReference mProductList = mRootRef.child("productList");
        mProductList.orderByChild("category").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Product a = ds.getValue(Product.class);
                    a.UID = ds.getKey();
                    productArrayList.add(a);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myAdapter = new ProductDisplayAdapter(this, productArrayList, false);
        myAdapter.setClickListener(this);

        recyclerView.setAdapter(myAdapter);
        RecyclerView.LayoutManager recyclerLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(dividerItemDecoration1);


    }



    @Override
    public void onItemClick(View view, int position) {
        Product thisproduct = myAdapter.getItem(position);
        Intent a = new Intent(this, ProductActivity.class);
        a.putExtra("CHOSEN_PRODUCT", thisproduct);
        startActivity(a);
        animateIntent();
    }

    public Typeface chooseFont(Boolean expanded){
        if (Locale.getDefault().getLanguage().equals("th")) {

            if(expanded){
                return Typeface.createFromAsset(this.getAssets(), "fonts/Kanit-ExtraLight.ttf");
            } else{
                return Typeface.createFromAsset(this.getAssets(), "fonts/Kanit-Light.ttf");
            }

        } else{
            if(expanded){
                return Typeface.createFromAsset(this.getAssets(), "fonts/Montserrat-ExtraLight.ttf");
            } else{
                return Typeface.createFromAsset(this.getAssets(), "fonts/Montserrat-Regular.ttf");
            }
        }
    }
}
