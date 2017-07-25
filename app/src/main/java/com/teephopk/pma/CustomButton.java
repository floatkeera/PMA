package com.teephopk.pma;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by floatkeera on 7/25/17.
 */

public class CustomButton extends Button {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);

    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }



    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */

        if(Locale.getDefault().getLanguage().equals("th")) {
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
