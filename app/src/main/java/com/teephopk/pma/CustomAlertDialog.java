package com.teephopk.pma;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by floatkeera on 8/22/17.
 */

public class CustomAlertDialog extends AlertDialog {

    Context context;


    protected CustomAlertDialog(@NonNull Context context) {
        super(context);

        this.context = context;




    }

    protected CustomAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected void changeTypeface(){

        TextView message = (TextView) findViewById(android.R.id.message);
        TextView title = (TextView) findViewById(android.R.id.title);
        Button btnpositive = (Button) findViewById(android.R.id.button1);
        Button btnnegative = (Button) findViewById(android.R.id.button2);


        Typeface typeface = selectTypeface(context, 0);

        message.setTypeface(typeface);
        title.setTypeface(typeface);
        btnpositive.setTypeface(typeface);
        btnnegative.setTypeface(typeface);


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
