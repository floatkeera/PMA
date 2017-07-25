package com.teephopk.pma;

import java.util.List;

/**
 * Created by floatkeera on 7/25/17.
 */

public class User {

    String UID;
    String displayName;
    String photoURL;
    int roleID;
    String birthday;
    List<Order> orderList;

    public User(){
        //Empty constructor for Firebase
    }

}
