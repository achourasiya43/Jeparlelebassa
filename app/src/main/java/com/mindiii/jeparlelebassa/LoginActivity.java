package com.mindiii.jeparlelebassa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mindiii.jeparlelebassa.Util.MySnackBar;
import com.mindiii.jeparlelebassa.Util.SetFont;
import com.mindiii.jeparlelebassa.Util.Util;
import com.mindiii.jeparlelebassa.Util.Validation;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.UserInfo;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView tvtrobleInLogin;
    private EditText edEmail,edPassword;
    private ImageButton ivBtnLogin;
    private ImageView ivFBLogin;
    private CallbackManager callbackManager;
    private LinearLayout LyLogin,lydontHaveAccount;
    private SessionManager sessionManager;
    private String  deviceToken, sEmail="", deviceId="N/A";
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        findoutVeriable();
        setUpVeriable();
        sessionManager = new SessionManager(this);


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final String sSocialId = loginResult.getAccessToken().getUserId();
                Log.d("LOGIN:", "sSocialId = " + sSocialId);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),

                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                             //   Log.e("response: ", response + "");
                              //  Log.e("responseJSON: ", object.toString() + "");
                                try {
                                    if(object.has("email")){
                                        sEmail = object.getString("email");}else {
                                        sEmail = "N/A";
                                    }
                                    String sFullName = object.getString("first_name");
                                    String sLastName = object.getString("last_name");
                                    String sUserImageUrl = "https://graph.facebook.com/"+sSocialId+"/picture?type=large";
                                    deviceToken = "Android+XYZ";  //FirebaseInstanceId.getInstance().getToken();

                                    Map<String,String> params = new HashMap<>();
                                    params.put("userName", sSocialId);
                                    params.put("fullName", sFullName + " " +sLastName);
                                    params.put("email", sEmail);
                                    // params.put("contact", "+"+countrycode+" "+contact);
                                    params.put("profileImage", sUserImageUrl);
                                    params.put("password", "");
                                    params.put("userType", "1");
                                    params.put("deviceId", deviceId);
                                    params.put("deviceType", "2");
                                    params.put("socialId", sSocialId);
                                    params.put("socialType", "facebook");
                                    doSocialRegistration(params);
                                } catch (Exception e) {
                                    MySnackBar.show(getResources().getString(R.string.error_in_facebook_login));
                                }
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, picture");
                parameters.putString("type", "large");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                MySnackBar.show(getResources().getString(R.string.facebook_login_cancelled));
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                MySnackBar.show(getResources().getString(R.string.error_in_facebook_login));
            }
        });

    }


    private void findoutVeriable(){
        TextView tvLogin = (TextView) findViewById(R.id.tv_login);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edPassword = (EditText) findViewById(R.id.ed_password);
        ivBtnLogin = (ImageButton) findViewById(R.id.iv_btn_login);
        ivFBLogin = (ImageView) findViewById(R.id.iv_fb_btn);
        LyLogin = (LinearLayout) findViewById(R.id.ly_login_liner);
        tvtrobleInLogin = (TextView) findViewById(R.id.trouble_in_login);
        TextView tvLoginwithfb = (TextView) findViewById(R.id.login_with_facbook);
        TextView tvdontHaveAccount = (TextView) findViewById(R.id.dont_have_account);
        lydontHaveAccount = (LinearLayout) findViewById(R.id.ly_dont_have_account);
        MySnackBar.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_snackbar);
        SetFont font = new SetFont();

        font.browau(this,edEmail);
        font.browau(this,edPassword);
        font.browaub(this, tvLogin);
        font.browaub(this,tvtrobleInLogin);
        font.browaub(this, tvLoginwithfb);
        font.browaub(this, tvdontHaveAccount);
    }

    private void setUpVeriable(){

        ivBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyboard(LyLogin,LoginActivity.this);
                if(isValidField()){
                    LoginToServer();
                }
            }
        });

        ivFBLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.isConnectingToInternet(LoginActivity.this)){
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_friends"));
                }
                else {
                    MySnackBar.show(getResources().getString(R.string.please_check_internet_connection));
                }

            }
        });

        LyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyboard(LyLogin,LoginActivity.this);
            }
        });

        lydontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvtrobleInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyboard(LyLogin,LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this,ForgotPassActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValidField(){
        Validation v = new Validation(this);

        if (!v.isNullValue(edEmail)) {
            MySnackBar.show(getResources().getString(R.string.enter_email_address));
            edEmail.requestFocus();
            return false;

        }
        else if (!v.isEmailValid(edEmail)) {
            MySnackBar.show(getResources().getString(R.string.enter_valid_email));
            return false;

        } else if (!v.isNullValue(edPassword)) {
            MySnackBar.show(getResources().getString(R.string.enter_password));
            edPassword.requestFocus();
            return false;

        } else if (!v.isPasswordValid(edPassword)) {
            MySnackBar.show(getResources().getString(R.string.Atleast_4_characters_required));
            return false;
        }
        return true;
    }

    private void LoginToServer() {
        if(Util.isConnectingToInternet(LoginActivity.this)) {

            Util.showProgressDialog(this, getResources().getString(R.string.loading));
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.Login_Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           System.out.println("#"+response);
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                if(status.equals("success")){
                                    Util.dismissProgressDialog();

                                    JSONObject object = jsonObject.getJSONObject("userData");
                                    UserInfo info = new UserInfo();
                                    info.setUserId(Integer.parseInt(object.getString("id")));
                                    info.setFullName(object.getString("fullName"));
                                    info.setEmail(object.getString("email"));
                                    info.setUserImage(object.getString("profileImage"));
                                    info.setAuthToken(object.getString("authToken"));
                                    info.setLanguageType(Integer.valueOf(object.getString("languageType")));
                                    info.setTotalScore(object.getString("totalScore"));
                                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                    sessionManager.createSession(info);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Util.dismissProgressDialog();
                                    MySnackBar.show(message);
                                }

                            } catch (JSONException e) {
                                Util.dismissProgressDialog();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Util.dismissProgressDialog();
                            MySnackBar.show(error.getMessage());

                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    String email = edEmail.getText().toString().trim();
                    String password = edPassword.getText().toString().trim();
                    deviceToken = "Android_XYZ"; //FirebaseInstanceId.getInstance().getToken();
                    params.put("email",email);
                    params.put("password", password);
                    params.put("deviceId", ""+deviceToken);
                    params.put("deviceType","2");
                    params.put("socialId", "");
                    params.put("socialType", "");
                    return params;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else{
            MySnackBar.show(getResources().getString(R.string.please_check_internet_connection));
        }
    }

    private void doSocialRegistration(final Map<String,String> params) {
        if(Util.isConnectingToInternet(LoginActivity.this)) {

            Util.showProgressDialog(this, getResources().getString(R.string.loading));

            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.Registration_Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                          //  System.out.println("#"+response);
                            Util.dismissProgressDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                if(status.equals("success")){

                                    JSONObject object = jsonObject.getJSONObject("userDetail");
                                    UserInfo info = new UserInfo();
                                    info.setUserId(object.getInt("id"));
                                    info.setFullName(object.getString("fullName"));
                                    info.setEmail(object.getString("email"));
                                    info.setUserImage(object.getString("profileImage"));
                                    info.setAuthToken(object.getString("authToken"));
                                    info.setTotalScore(object.getString("totalScore"));
                                    info.setLanguageType(object.getInt("languageType"));


                                    int langvalue  = object.getInt("languageType");
                                    if(langvalue==0){
                                         mode="1";
                                    }else {
                                        mode = String.valueOf(langvalue);
                                    }

                                    SharedPreferences.Editor editor = getSharedPreferences("language", MODE_PRIVATE).edit();
                                    editor.putString("mode", mode);
                                    editor.apply();

                                    sessionManager = new SessionManager(LoginActivity.this);
                                    sessionManager.createSession(info);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Util.dismissProgressDialog();
                                    MySnackBar.show(message);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Util.dismissProgressDialog();
                                MySnackBar.show(getResources().getString(R.string.somthing_went_wrong));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Util.dismissProgressDialog();
                            MySnackBar.show(getResources().getString(R.string.somthing_went_wrong));
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else{

            MySnackBar.show(getResources().getString(R.string.please_check_internet_connection));

        }
    }



}
