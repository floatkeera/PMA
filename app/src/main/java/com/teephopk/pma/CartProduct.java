package com.teephopk.pma;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by floatkeera on 8/18/17.
 */

public class CartProduct implements Parcelable {

    public String productUID;
    public int qty;
    public double subTotal;
    public String productName;

    public CartProduct(){

    }

    public CartProduct(String UID, int qty, double subTotal, String productName){
        this.productUID = UID;
        this.qty = qty;
        this.subTotal = subTotal;
        this.productName = productName;
    }

    protected CartProduct(Parcel in) {
        productUID = in.readString();
        qty = in.readInt();
        subTotal = in.readDouble();
        productName = in.readString();
    }

    public static final Creator<CartProduct> CREATOR = new Creator<CartProduct>() {
        @Override
        public CartProduct createFromParcel(Parcel in) {
            return new CartProduct(in);
        }

        @Override
        public CartProduct[] newArray(int size) {
            return new CartProduct[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productUID);
        parcel.writeInt(qty);
        parcel.writeDouble(subTotal);
        parcel.writeString(productName);
    }
}
