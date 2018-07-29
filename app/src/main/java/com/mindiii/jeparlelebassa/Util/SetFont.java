package com.mindiii.jeparlelebassa.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mindiii on 14/4/17.
 */

public class SetFont {
    Context context;
    TextView textView;



    public void browau(Activity  context, TextView textView) {

        Typeface face= Typeface.createFromAsset(context.getResources().getAssets(), "fonts/browau.ttf");
        textView.setTypeface(face);
        this.context = context;
        this.textView = textView;
    }

    public void browaub(Context context, TextView textView) {

        Typeface face= Typeface.createFromAsset(context.getResources().getAssets(), "fonts/browaub.ttf");
        textView.setTypeface(face);
        this.context = context;
        this.textView = textView;
    }

    public void browaui(Context context, TextView textView) {

        Typeface face= Typeface.createFromAsset(context.getResources().getAssets(), "fonts/browaui.ttf");
        textView.setTypeface(face);
    }

    public void browauz(Context context, TextView textView) {

        Typeface face= Typeface.createFromAsset(context.getResources().getAssets(), "fonts/browauz.ttf");
        textView.setTypeface(face);
    }

}