package com.teephopk.pma;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by floatkeera on 8/21/17.
 */

public class CartAdapter extends ArrayAdapter<CartProduct> {

    ArrayList<CartProduct> cartProducts;
    ArrayList<Product> products;
    Product product;
    Context context;
    int resource;

    View parentLayout;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public CartAdapter(Context context, int resource, ArrayList<CartProduct> objects) {
        super(context, resource, objects);
        this.cartProducts = objects;
        this.context = context;
        this.resource = resource;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        final Product[] productArray = new Product[1];

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null, true);
        }

        final ImageView productImage = (ImageView) convertView.findViewById(R.id.productImage);
        final CustomTitleTextView productName = (CustomTitleTextView) convertView.findViewById(R.id.productName);
        final CustomTextView productPrice = (CustomTextView) convertView.findViewById(R.id.productPrice);
        final CustomTextView productQty = (CustomTextView) convertView.findViewById(R.id.productQty);
        final CustomTextView discountView = (CustomTextView) convertView.findViewById(R.id.discount);



        final CartProduct cartProduct = cartProducts.get(position);


        ImageButton addQty = (ImageButton) convertView.findViewById(R.id.qtyplus);
        final ImageButton minusQty = (ImageButton) convertView.findViewById(R.id.qtyminus);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.btnDelete);

        products = new ArrayList<Product>();

        DatabaseReference mProductList = mRootRef.child("productList");
        mProductList.orderByKey().equalTo(cartProduct.productUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    products.add(ds.getValue(Product.class));
                    productArray[0] = ds.getValue(Product.class);

                }


                productName.setText(productArray[0].name);
                productPrice.setText(NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(productArray[0].price));
                productQty.setText(String.valueOf(cartProduct.qty));



                GlideApp.with(context)
                        .load(productArray[0].photoURL)
                        .fitCenter()
                        .transition(withCrossFade())
                        .into(productImage);

                if (cartProduct.qty == 1) {
                    minusQty.setImageDrawable(context.getDrawable(R.drawable.ic_minus_disabled));
                    minusQty.setClickable(false);
                } else {
                    minusQty.setImageDrawable(context.getDrawable(R.drawable.ic_minus));
                    minusQty.setClickable(true);
                }

                if(productArray[0].discount != 0){
                    productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    discountView.setVisibility(View.VISIBLE);
                    discountView.setText( NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(productArray[0].price - (productArray[0].price*(productArray[0].discount/100))));
                } else{
                    productPrice.setPaintFlags(productPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    discountView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final DatabaseReference mCartProductRef = mRootRef.child("usersList").child(mAuth.getCurrentUser().getUid()).child("cart").child(cartProduct.productUID);
        final DatabaseReference mProductQtyRef = mCartProductRef.child("qty");


        addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product product = productArray[0];

                mCartProductRef.setValue(new CartProduct(cartProduct.productUID, cartProduct.qty + 1, (product.price - (product.price*(product.discount/100))) * (cartProduct.qty + 1), product.name), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Snackbar.make(parent, "Quantity changed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        minusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product =  productArray[0];
                mCartProductRef.setValue(new CartProduct(cartProduct.productUID, cartProduct.qty - 1, (product.price - (product.price*(product.discount/100))) * (cartProduct.qty - 1), product.name), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Snackbar.make(parent, "Quantity changed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Product product =  productArray[0];

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(context.getString(R.string.delete));
                alert.setMessage(context.getString(R.string.areyousure) + product.name);
                alert.setPositiveButton(context.getString(R.string.yesdelete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        mCartProductRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Snackbar.make(parent, product.name + " " + context.getString(R.string.deleted), Snackbar.LENGTH_LONG).setAction(context.getString(R.string.undo), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mCartProductRef.setValue(cartProduct);
                                    }
                                }).show();

                            }
                        });

                    }


                });
                alert.setNegativeButton(context.getString(R.string.nokeepit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = alert.show();




            }
        });


        return convertView;

    }



}
