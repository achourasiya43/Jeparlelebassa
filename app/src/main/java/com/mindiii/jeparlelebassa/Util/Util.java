package com.mindiii.jeparlelebassa.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mindiii on 13/4/17.
 */

public class Util {
    //Context context;
    private static ProgressDialog progress;

   /* *//*..........method for findout hash key for fb........................*//*
    public void printHashKey(Context pContext) {

        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(
                    "com.mindiii.jeparlelebassa",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }
    }*/


    /*.........for keyboard hiding..........................................*/
    public static  void hideKeyboard(View view,Context context) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /*.........for keyboard Showing..........................................*/
    public static  void showKeyboard(Context context) {
        ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /*......................check internate isenable..............................*/
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for(NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    /*...........progress dialog.......................................................*/
    public static void showProgressDialog(Activity activity , String loadingTxt) {
        progress = new ProgressDialog(activity);
        progress.setCancelable(false);
        progress.setMessage(loadingTxt);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    public static void dismissProgressDialog(){
        if(progress!=null)
            progress.dismiss();
    }

}
