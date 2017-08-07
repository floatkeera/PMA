package com.teephopk.pma;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by floatkeera on 8/4/17.
 */

public class CustomPreferenceCategory extends PreferenceCategory {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";


    AttributeSet thisAttrs;


    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        thisAttrs = attrs;
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        thisAttrs = attrs;
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        thisAttrs = attrs;
    }

    public CustomPreferenceCategory(Context context) {
        super(context);

    }

    @Override
    public void onBindView(View view){
        super.onBindView(view);
        int textStyle;

            textStyle = 0;


        Typeface customFont = selectTypeface(getContext(), textStyle);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTypeface(customFont);
        titleView.setTextSize(24);

    }



    private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */

        if(Locale.getDefault().getLanguage().equals("th")){
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Kanit-Medium.ttf");
                case Typeface.NORMAL: // regular
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Kanit-Light.ttf");
                case Typeface.ITALIC: // light
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Kanit-ExtraLight.ttf");
                default:
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Kanit-Light.ttf");
            }
        }else{
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.ttf");
                case Typeface.NORMAL: // regular
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
                case Typeface.ITALIC: // light
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-ExtraLight.ttf");
                default:
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            }
        }
    }


}
