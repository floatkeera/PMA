package com.teephopk.pma;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by floatkeera on 8/24/17.
 */

public class CheckoutAdapter extends ArrayAdapter<CartProduct> {

   Context context;
    int resource;
    ArrayList<CartProduct> cartProducts;



    public CheckoutAdapter(Context context, int resource, ArrayList<CartProduct> objects) {
        super(context, resource, objects);
        this.cartProducts = objects;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null, true);
        }

        CartProduct cproduct = cartProducts.get(position);

        CustomTextView productdetails = (CustomTextView) convertView.findViewById(R.id.productdetail);
        CustomTextView subtotal = (CustomTextView) convertView.findViewById(R.id.subtotal);

        productdetails.setText(String.valueOf(cproduct.qty) + " x " + cproduct.productName);
        subtotal.setText(NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(cproduct.subTotal));











        return convertView;


    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
