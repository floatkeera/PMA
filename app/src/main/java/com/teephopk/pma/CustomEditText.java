package com.teephopk.pma;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by floatkeera on 7/27/17.
 */

public class CustomEditText extends EditText {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";


    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        if(Locale.getDefault().getLanguage().equals("th")){
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansThaiUI-Bold.ttf");
                case Typeface.NORMAL: // regular
                    return Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansThaiUI-Regular.ttf");
                case Typeface.ITALIC: // light
                    return Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansThaiUI-Regular.ttf");
                default:
                    return Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansThaiUI-Regular.ttf");
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
