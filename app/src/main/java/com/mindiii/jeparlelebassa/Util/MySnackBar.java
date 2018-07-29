package com.mindiii.jeparlelebassa.Util;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

/**
 * Created by mindiii on 12/4/17.
 */

public class MySnackBar {

    public static CoordinatorLayout coordinatorLayout;

    public static void show(String message){
        if(coordinatorLayout != null){
            Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG).setAction("ok",null).show();

        }

    }

}
