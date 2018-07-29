package com.mindiii.jeparlelebassa;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.mindiii.jeparlelebassa.Util.MySnackBar;
import com.mindiii.jeparlelebassa.Util.SetFont;
import com.mindiii.jeparlelebassa.Util.Util;
import com.mindiii.jeparlelebassa.Util.Validation;
import com.mindiii.jeparlelebassa.helper.Constant;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.UserInfo;
import com.mindiii.jeparlelebassa.volleymultipart.AppHelper;
import com.mindiii.jeparlelebassa.volleymultipart.VolleyMultipartRequest;
import com.mindiii.jeparlelebassa.volleymultipart.VolleySingleton;
import com.mvc.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edFullName, edEmail, edPassword;
    private TextView tvSignUp;
    private String fullname, email, password;
    private ImageView ivBack, ivProfileImage;
    private ImageButton ivBtnRegister;
    LinearLayout LyRegister;
    Bitmap profileImageBitmap;
    String deviceToken;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        findoutVariable();
        setupVeriable();
    }

    private void findoutVariable() {
        edFullName = (EditText) findViewById(R.id.ed_full_name);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edPassword = (EditText) findViewById(R.id.ed_password);
        ivBtnRegister = (ImageButton) findViewById(R.id.iv_register);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivProfileImage = (ImageView) findViewById(R.id.profile_image);
        LyRegister = (LinearLayout) findViewById(R.id.ly_register_linear);
        tvSignUp = (TextView) findViewById(R.id.tv_singn_up);
        MySnackBar.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_snackbar);
        deviceToken = "android_XYZ";//FirebaseInstanceId.getInstance().getToken();
    }


    private void setupVeriable() {

        ivBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidField()) {
                    doRegistration(RegistrationActivity.this);
                }
            }
        });

        LyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyboard(LyRegister, RegistrationActivity.this);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermissionAndPicImage();
            }
        });

        SetFont font = new SetFont();
        font.browau(this,edFullName);
        font.browau(this,edEmail);
        font.browau(this,edPassword);
        font.browaub(this,tvSignUp);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constant.SELECT_FILE);
                } else {
                    Toast.makeText(RegistrationActivity.this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(RegistrationActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(RegistrationActivity.this);
                } else {
                    Toast.makeText(RegistrationActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    public void getPermissionAndPicImage() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY);
            } else {
                ImagePicker.pickImage(RegistrationActivity.this);
            }
        } else {
            ImagePicker.pickImage(RegistrationActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 234) {
                profileImageBitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
                if (profileImageBitmap != null)
                    ivProfileImage.setImageBitmap(profileImageBitmap);
            }
        }
    }

    private boolean isValidField() {
        Validation v = new Validation(this);
        if (profileImageBitmap == null) {
            MySnackBar.show(getResources().getString(R.string.select_profile_picture));
            edFullName.requestFocus();
            return false;

        } else if (!v.isNullValue(edFullName)) {
            MySnackBar.show(getResources().getString(R.string.enter_full_name));
            edFullName.requestFocus();
            return false;

        } else if (!v.isNullValue(edEmail)) {
            MySnackBar.show(getResources().getString(R.string.enter_email_address));
            edEmail.requestFocus();
            return false;

        } else if (!v.isEmailValid(edEmail)) {
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


    public void doRegistration(final Activity context) {

        if (Util.isConnectingToInternet(context)) {

            Util.showProgressDialog(this, getResources().getString(R.string.loading));

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebServices.Registration_Url, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    Util.dismissProgressDialog();
                    try {

                        JSONObject jsonObject = new JSONObject(resultResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(message.equals("User already registered")){
                            MySnackBar.show(getResources().getString(R.string.user_already_registered));
                        }

                        if (status.equals("success")) {

                            JSONObject object = jsonObject.getJSONObject("userDetail");
                            UserInfo info = new UserInfo();
                            info.setUserId(Integer.parseInt(object.getString("id")));
                            info.setFullName(object.getString("fullName"));
                            info.setEmail(object.getString("email"));
                            info.setUserImage(object.getString("profileImage"));
                            info.setAuthToken(object.getString("authToken"));
                            info.setLanguageType(object.getInt("languageType"));

                            sessionManager = new SessionManager(RegistrationActivity.this);
                            sessionManager.createSession(info);

                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            MySnackBar.show(message);

                        }

                    } catch (JSONException e) {
                        Util.dismissProgressDialog();
                        MySnackBar.show(getResources().getString(R.string.somthing_went_wrong));
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    Util.dismissProgressDialog();
                    MySnackBar.show(getResources().getString(R.string.somthing_went_wrong));
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", "" + status);
                            Log.e("Error Message", message);

                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = message + " Please login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = message + " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = message + " Something is getting wrong";
                            }
                            MySnackBar.show(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    fullname = edFullName.getText().toString().trim();
                    email = edEmail.getText().toString().trim();
                    password = edPassword.getText().toString().trim();
                    params.put("fullName", fullname);
                    params.put("deviceId", deviceToken);
                    params.put("deviceType", "2");
                    params.put("email", email);
                    params.put("password", password);
                    params.put("socialId", "");
                    params.put("socialType", "");
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    if (profileImageBitmap != null)
                        params.put("profileImage", new VolleyMultipartRequest.DataPart("profileImage.jpg", AppHelper.getFileDataFromBitmap(profileImageBitmap), "image/jpeg"));
                    return params;
                }

            };

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

        } else {
            //Snackbar.make(view, "Please Check internet connection.!", Snackbar.LENGTH_LONG).setAction("ok", null).show();
            MySnackBar.show(getResources().getString(R.string.please_check_internet_connection));

        }

    }
}