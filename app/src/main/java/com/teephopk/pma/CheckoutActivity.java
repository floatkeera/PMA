package com.teephopk.pma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import co.omise.Client;
import co.omise.android.models.Token;
import co.omise.android.ui.CreditCardActivity;
import co.omise.models.Charge;
import co.omise.models.OmiseError;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CheckoutActivity extends BaseActivityWithToolbar {


    private static final String OMISE_PKEY = "pkey_test_58i6gpxzxpuw7vlybb3";
    private static final String OMISE_SKEY = "skey_test_58i6gpy08hzwpdyz684";
    private static final int REQUEST_CC = 100;

    CustomButton creditCardBtn;
    View footerView;
    View creditCardView;
    ImageView creditCardBrand;
    CustomTextView creditCardDetails;
    Client client;
    ListView checkOutList;
    Token token;
    CustomTitleTextView totalText;
    CustomTitleTextView totalPrice;
    int qty;
    double total;
    CheckoutAdapter myAdapter;
    View headerView;
    CustomButton paynowbtn;


    CustomEditText name;
    CustomEditText line1;
    CustomEditText line2;
    CustomEditText changwat;
    CustomEditText zipcode;


    boolean addressEntered = false;
    boolean paymentEntered = false;


    Charge charge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        qty = getIntent().getIntExtra("ITEMS", 0);
        total = getIntent().getDoubleExtra("TOTAL", 0);
        cartProducts = getIntent().getParcelableArrayListExtra("CARTPRODUCTS");

        client = new Client(OMISE_PKEY, OMISE_SKEY);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTxt = (CustomTitleTextView) findViewById(R.id.toolbar_title);
        initializeToolbar(getString(R.string.checkout));


        checkOutList = (ListView) findViewById(R.id.listview);


        footerView = getLayoutInflater().inflate(R.layout.checkout_footer, null, false);
        headerView = getLayoutInflater().inflate(R.layout.checkout_header, null, false);


        creditCardBrand = (ImageView) footerView.findViewById(R.id.creditcardbrand);
        creditCardDetails = (CustomTextView) footerView.findViewById(R.id.creditcardtext);
        creditCardBtn = (CustomButton) footerView.findViewById(R.id.creditcardbtn);
        totalText = (CustomTitleTextView) footerView.findViewById(R.id.totaltext);
        totalPrice = (CustomTitleTextView) footerView.findViewById(R.id.totalprice);
        creditCardView = footerView.findViewById(R.id.creditcardview);
        paynowbtn = (CustomButton) footerView.findViewById(R.id.checkoutbtn);

        paynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPaynow();
            }
        });

        name = (CustomEditText) footerView.findViewById(R.id.name);
        line1 = (CustomEditText) footerView.findViewById(R.id.line1);
        line2 = (CustomEditText) footerView.findViewById(R.id.line2);
        changwat = (CustomEditText) footerView.findViewById(R.id.changwat);
        zipcode = (CustomEditText) footerView.findViewById(R.id.zipcode);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        creditCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, CreditCardActivity.class);
                intent.putExtra(CreditCardActivity.EXTRA_PKEY, OMISE_PKEY);
                startActivityForResult(intent, REQUEST_CC);
            }
        });


        totalText.setText(getString(R.string.totaltext) + " (" + String.valueOf(qty) + " " + getString(R.string.items) + "): ");
        totalPrice.setText(NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(total));


        myAdapter = new CheckoutAdapter(this, R.layout.simplerow_noarrow, cartProducts);
        checkOutList.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        checkOutList.addFooterView(footerView, null, false);
        checkOutList.addHeaderView(headerView, null, false);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_activity_menu; this adds items to the action bar if it is present.

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CC:


                if (resultCode == CreditCardActivity.RESULT_CANCEL) {
                    return;
                }
                creditCardBtn.setVisibility(View.GONE);

                creditCardView.setVisibility(View.VISIBLE);
                paynowbtn.setVisibility(View.VISIBLE);

                token = data.getParcelableExtra(CreditCardActivity.EXTRA_TOKEN_OBJECT);

                paymentEntered = true;

                String ccbrand = token.card.brand;

                if (ccbrand.toLowerCase().equals("visa")) {
                    GlideApp.with(CheckoutActivity.this)
                            .load("https://lh6.ggpht.com/Lk28s4pmJ9hrei0Scb5dNS1cJeSRK5t5eOd1m1xSFxN-NBieBUG-rM9TGCfSelVu6S7mLlLz")
                            .fitCenter()
                            .transition(withCrossFade())
                            .into(creditCardBrand);

                } else if (ccbrand.toLowerCase().equals("mastercard")) {
                    GlideApp.with(CheckoutActivity.this)
                            .load("https://lh6.ggpht.com/F7T3Syh_DXuJ_0cdsFU_fZ5sBFO0Hnqdkl3IuDU7b7yuO5O9CAHxGFB-bnC6DPr9diQMwxqn")
                            .fitCenter()
                            .transition(withCrossFade())
                            .into(creditCardBrand);
                }

                creditCardDetails.setText(ccbrand + " ••••-" + token.card.lastDigits);


                // process your token here.

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void attemptPaynow() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Reset errors.
        name.setError(null);
        line1.setError(null);
        line2.setError(null);
        changwat.setError(null);
        zipcode.setError(null);


        // Store values at the time of the login attempt.
        final String addressName = name.getText().toString();
        final String addressLine1 = line1.getText().toString();
        final String addressLine2 = line1.getText().toString();
        final String addressChangwat = changwat.getText().toString();
        final String addressZipcode = zipcode.getText().toString();


        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(addressName)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(addressLine1)) {
            line1.setError(getString(R.string.error_field_required));
            focusView = line1;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(addressLine2)) {
            line2.setError(getString(R.string.error_field_required));
            focusView = line2;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(addressChangwat)) {
            changwat.setError(getString(R.string.error_field_required));
            focusView = changwat;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(addressZipcode)) {
            zipcode.setError(getString(R.string.error_field_required));
            focusView = zipcode;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            try {
                new CompletePayment().execute("");
            }catch (Exception e){
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckoutActivity.this);
                alertDialog.setTitle("Payment Failed").setMessage(charge.getFailureMessage()).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }

        }
    }

    class CompletePayment extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        boolean success = false;



        void showDialog(final OmiseError error){

            CheckoutActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    progressDialog.dismiss();
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckoutActivity.this);
                    alertDialog.setTitle("Payment Failed").setMessage(error.getMessage()).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

                }
            });
        }

        void showDialog(Exception error){

            progressDialog.dismiss();
            CheckoutActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckoutActivity.this);
                    alertDialog.setTitle("Payment Failed").setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

                }
            });
        }




        @Override
        protected String doInBackground(String... params) {

            try {

                charge = client.charges()
                        .create(new Charge.Create()
                                .amount((long) total * 100) // 1,000 THB
                                .currency("thb")
                                .card(token.id));

                if(charge.isPaid()){
                    success = true;

                } else{
                    progressDialog.dismiss();
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckoutActivity.this);
                    alertDialog.setTitle("Payment Failed").setMessage(charge.getFailureMessage()).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }




            } catch (OmiseError e) {
                showDialog(e);


            } catch( Exception e){
                showDialog(e);
            }

            return "Success";

        }

        protected void onPreExecute() {
            cubeGrid = new CubeGrid();
            cubeGrid.setColor(getResources().getColor(R.color.colorAccent));


            progressDialog =  ProgressDialog.show(CheckoutActivity.this, "",
                    "Confirming payment. Please wait...", true);

            progressDialog.setIndeterminateDrawable(cubeGrid);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub




            super.onPostExecute(result);

            if(success) {

                DatabaseReference mCartRef = mRootRef.child("usersList").child(mAuth.getCurrentUser().getUid()).child("cart");

                mCartRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressDialog.dismiss();

                        Intent a = new Intent(CheckoutActivity.this, PaymentCompletedActivity.class);
                        startActivity(a);
                        animateIntent();


                    }
                });
            }

        }
    }





}
