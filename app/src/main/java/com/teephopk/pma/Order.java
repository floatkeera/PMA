package com.teephopk.pma;

import java.util.List;

/**
 * Created by floatkeera on 7/25/17.
 */

public class Order {

    long timeCreated;
    double totalAmount; //THB
    String orderStatus;
    boolean paymentCompleted;
    List<OrderProduct> orderProductList;

    public Order(){
        //Empty constructor for Firebase
    }


}
