package com.teephopk.pma;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by floatkeera on 7/25/17.
 */

public class Product implements Parcelable {

    public String name;
    public String description;
    public String photoURL;
    public Double price;
    public float rating;
    public String UID;
    public double discount;


    public Product(){
        //Empty constructor for Firebase
    }

    protected Product(Parcel in) {
        name = in.readString();
        description = in.readString();
        photoURL = in.readString();
        price = in.readDouble();
        rating = in.readFloat();
        UID = in.readString();
        discount = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(photoURL);
        parcel.writeDouble(price);
        parcel.writeFloat(rating);
        parcel.writeString(UID);
        parcel.writeDouble(discount);
    }
}
