package com.teephopk.pma;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.SwitchPreference;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by floatkeera on 8/4/17.
 */

public class CustomSwitchPreference extends SwitchPreference{

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";


    AttributeSet thisAttrs;

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        thisAttrs = attrs;
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        thisAttrs = attrs;
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        thisAttrs = attrs;
    }

    public CustomSwitchPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindView(View view){

        super.onBindView(view);

        Typeface customFont = selectTypeface(getContext(), Typeface.NORMAL);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTypeface(customFont);
        titleView.setTextSize(18);

        TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
        summaryView.setTypeface(customFont);

    }



    private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */

        if(Locale.getDefault().getLanguage().equals("th")){
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Umpush-Bold.ttf");
                case Typeface.NORMAL: // regular
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Umpush-Book.ttf");
                case Typeface.ITALIC: // light
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Umpush-Light.ttf");
                default:
                    return Typeface.createFromAsset(context.getAssets(), "fonts/Umpush-Book.ttf");
            }
        }else{
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                case Typeface.NORMAL: // regular
                    return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                case Typeface.ITALIC: // light
                    return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
                default:
                    return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
            }
        }
    }
}
