package com.teephopk.pma;


/**
 * Created by floatkeera on 8/22/17.
 */

public class RecyclerViewDataModel {

    public static final int SLIDER = 1;
    public static final int PRODUCT = 2;
    public static final int TITLE = 3;
    public static final int PROGRESSBAR = 4;
    public static final int EMPTY = 5;
    public static final int CATEGORY = 6;



    public int vType;
    public Object data;

    public RecyclerViewDataModel(int a, Object b){
        vType = a;
        data = b;
    }

}
