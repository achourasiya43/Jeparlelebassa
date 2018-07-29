package com.mindiii.jeparlelebassa.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.facebook.login.LoginManager;
import com.mindiii.jeparlelebassa.LoginActivity;
import com.mindiii.jeparlelebassa.model.UserInfo;

import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class SessionManager {

    private SharedPreferences mypref ;
    private SharedPreferences language_pref ;
    private SharedPreferences lastLogin_pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editorLanguage;
    private SharedPreferences.Editor lastlogin_editor;
    private static final String PREF_NAME = "LeParle";
    private static final String PREF_NAME1 = "LoyalMe1";
    private static final String PREF_LAST_LOGIN = "LoyalMe_Lastlogin";
    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String IsShowdialog = "IsShowdialog";

    public static final String Language = "Language";
    Context _context;


    public SessionManager(Context context ){
        _context = context;
        mypref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = mypref.edit();
        editor.apply();

        language_pref = context.getSharedPreferences(PREF_NAME1, MODE_PRIVATE);
        editorLanguage = language_pref.edit();
        editorLanguage.apply();

        lastLogin_pref = context.getSharedPreferences(PREF_LAST_LOGIN, MODE_PRIVATE);
        lastlogin_editor = lastLogin_pref.edit();
        lastlogin_editor.apply();

    }

    public void createSession(UserInfo userInfo){

        editor.putInt(Constant.USER_ID, userInfo.getUserId());
        editor.putString(Constant.FULL_NAME, userInfo.getFullName());
        editor.putString(Constant.EMAIL, userInfo.getEmail());
        editor.putString(Constant.ADDRESS, userInfo.getAddress());
        editor.putString(Constant.USER_IMAGE, userInfo.getUserImage());
        editor.putString(Constant.USER_IMAGE_THUMB, userInfo.getUserImageThumb());
        editor.putString(Constant.AUTH_TOKEN, userInfo.getAuthToken());
        editor.putString(Constant.totalScore, userInfo.getTotalScore());
        editor.putInt(Constant.LanguageType, userInfo.getLanguageType());
        editor.putBoolean(IS_LOGGEDIN, true);
        editor.apply();
    }

    public void createSessionForLanguage(int  lng){
        editor.putInt(Constant.LanguageType, lng);
        editorLanguage.apply();
    }

    public void updateProfile(String name,String number, String image) {
        editor.putString("full_name", name);
        editor.putString("number", number);
        editor.putString("user_image",image);
        editor.apply();
    }


    public UserInfo getUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(mypref.getInt(Constant.USER_ID, -1));
        userInfo.setFullName(mypref.getString(Constant.FULL_NAME, ""));
        userInfo.setEmail(mypref.getString(Constant.EMAIL, ""));
        userInfo.setUserImage(mypref.getString(Constant.USER_IMAGE, ""));
        userInfo.setUserImageThumb(mypref.getString(Constant.USER_IMAGE_THUMB, ""));
        userInfo.setAddress(mypref.getString(Constant.ADDRESS, ""));
        userInfo.setAuthToken(mypref.getString(Constant.AUTH_TOKEN, ""));
        userInfo.setTotalScore(mypref.getString(Constant.totalScore, ""));
        userInfo.setLanguageType(mypref.getInt(Constant.LanguageType,0));
        return userInfo;
    }

    public void setlanguage(String language) {
        editorLanguage.putString("language", language);
        editorLanguage.apply();
        updateLanguage(language);
    }

    public void setScore(String totalscore) {
        editor.putString(Constant.totalScore, totalscore);
        editor.apply();
    }
   /* public void setlanhuageType(int i ){
        editor.putInt(Constant.LanguageType,i);
        editor.apply();

    }*/

    public String getLanguage(){
        return language_pref.getString("language", "en");
    }

    public void updateLanguage(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Resources resources = _context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public boolean isLoggedIn(){
        return mypref.getBoolean(IS_LOGGEDIN, false);
    }

    public boolean isShowdialog(){
        return mypref.getBoolean(IsShowdialog, false);
    }

    public void setShowdialog(boolean bool){
        editor.putBoolean(IsShowdialog, bool);
        editor.apply();
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }
    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }


    public void logout(Activity activity){
        editor.clear();
        editor.apply();
        Intent intent = new Intent(activity, LoginActivity.class);
        LoginManager.getInstance().logOut();
        activity.startActivity(intent);
        activity.finish();
    }

    public void setLastLogin(HashMap<String,String> loginDetail){

        lastlogin_editor.putString("userName", loginDetail.get("userName"));
        lastlogin_editor.putString("password", loginDetail.get("password"));
        lastlogin_editor.commit();
    }

    public HashMap<String,String> getLastLogin(){

        HashMap<String,String> loginDetail = new HashMap<String, String>();
        loginDetail.put("userName", lastLogin_pref.getString("userName", ""));
        loginDetail.put("password", lastLogin_pref.getString("password", ""));
        return loginDetail;
    }



}
