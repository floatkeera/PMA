package com.teephopk.pma;

import java.util.List;

/**
 * Created by floatkeera on 7/25/17.
 */

public class Product {

    String name;
    String description;
    String photoURL;
    double price;
    List<Stock> stockList;

    public Product(){
        //Empty constructor for Firebase
    }

}
