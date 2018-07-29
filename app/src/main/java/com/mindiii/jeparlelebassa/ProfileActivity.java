package com.mindiii.jeparlelebassa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.mindiii.jeparlelebassa.Util.MySnackBar;
import com.mindiii.jeparlelebassa.Util.Util;
import com.mindiii.jeparlelebassa.helper.Constant;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.UserInfo;
import com.mindiii.jeparlelebassa.volleymultipart.AppHelper;
import com.mindiii.jeparlelebassa.volleymultipart.VolleyMultipartRequest;
import com.mindiii.jeparlelebassa.volleymultipart.VolleySingleton;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ImageView ivBack;
    Button btndone;
    ImageView profileImage,profileImageBG;
    TextView tvScore;
    EditText edUserName,edUserEmail;
    SessionManager sessionManager;
    UserInfo userInfo;
    RelativeLayout view;
    ProgressBar progressBar;
    Bitmap profileImageBitmap;
    boolean ischange,isEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findoutVeriable();

        sessionManager =  new SessionManager(this);
        userInfo = sessionManager.getUserInfo();
        setVeriable();
        ischange = false;

    }

    private void findoutVeriable(){
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btndone = (Button) findViewById(R.id.btn_done);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        profileImageBG = (ImageView) findViewById(R.id.profile_image_bg);
        edUserName = (EditText) findViewById(R.id.tv_user_name);
        edUserEmail = (EditText) findViewById(R.id.tv_user_email);
        tvScore = (TextView) findViewById(R.id.tv_user_score);
        view = (RelativeLayout) findViewById(R.id.ly_profile);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_profile);

    }

    private void setVeriable(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        Picasso.with(this).load(userInfo.getUserImage()).placeholder(R.drawable.profileimg).into(profileImage);
        Picasso.with(this).load(userInfo.getUserImage()).placeholder(R.drawable.profileimg).into(profileImageBG);

        if(userInfo.totalScore.equals("")){
            tvScore.setText("0");
        }else {
            tvScore.setText(userInfo.totalScore);

        }

       edUserName.setText(userInfo.fullName);
       edUserEmail.setText(userInfo.email);

        edUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ischange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ischange = true;

            }
        });

        edUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ischange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ischange = true;
            }
        });


        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEdit) {
                    edUserName.setEnabled(true);
                    edUserEmail.setEnabled(true);
                    edUserName.requestFocus();
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btndone.getLayoutParams();
                    params.width = 130;
                    btndone.setLayoutParams(params);
                    btndone.setBackgroundColor(0);
                    btndone.setText("Done");
                    Util.showKeyboard(ProfileActivity.this);

                    profileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getPermissionAndPicImage();
                           Util.hideKeyboard(view,ProfileActivity.this);
                        }
                    });
                    ischange=false;
                    isEdit = true;
                }
                else{
                    edUserName.setEnabled(false);
                    edUserEmail.setEnabled(false);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btndone.getLayoutParams();
                    params.width = 75;
                    btndone.setLayoutParams(params);

                    btndone.setBackgroundResource(R.drawable.edir_icon);
                    btndone.setText("");
                    Util.hideKeyboard(view,ProfileActivity.this);

                    profileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    if(ischange) {
                        updateUserProfile(ProfileActivity.this);
                    }
                    isEdit = false;
                }

            }
        });


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
                    Toast.makeText(ProfileActivity.this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(ProfileActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(ProfileActivity.this);
                } else {
                    Toast.makeText(ProfileActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    public void getPermissionAndPicImage() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY);
            } else {
                ImagePicker.pickImage(ProfileActivity.this);
            }
        } else {
            ImagePicker.pickImage(ProfileActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 234) {
                profileImageBitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
                if (profileImageBitmap != null)
                    profileImage.setImageBitmap(profileImageBitmap);
                    profileImageBG.setImageBitmap(profileImageBitmap);
                    ischange = true;
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void updateUserProfile(Activity context) {

        if (Util.isConnectingToInternet(context)) {

           progressBar.setVisibility(View.VISIBLE);

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebServices.UpdateProfile, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    progressBar.setVisibility(View.GONE);
                    try {

                        JSONObject jsonObject = new JSONObject(resultResponse);


                        if (jsonObject != null) {

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                JSONObject object = jsonObject.getJSONObject("userData");
                                UserInfo info = new UserInfo();
                                info.setUserId(userInfo.getUserId());
                                info.setFullName(object.getString("fullName"));
                                info.setEmail(object.getString("email"));
                                info.setUserImage(object.getString("profileImage"));
                                info.setAuthToken(userInfo.authToken);

                                sessionManager = new SessionManager(ProfileActivity.this);
                                sessionManager.createSession(info);


                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("authToken", userInfo.authToken);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    String fullname = edUserName.getText().toString().trim();
                    String email = edUserEmail.getText().toString().trim();

                    params.put("fullName", fullname);
                    params.put("email", email);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<String, DataPart>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    if (profileImageBitmap != null) {
                        params.put("profileImage", new VolleyMultipartRequest.DataPart("profileImage.jpg", AppHelper.getFileDataFromBitmap(profileImageBitmap), "image/jpeg"));
                    }
                        return params;
                }

            };

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

        } else {
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(ProfileActivity.this, "Please Check internet connection.!", Toast.LENGTH_SHORT).show();
            MySnackBar.show(getResources().getString(R.string.please_check_internet_connection));


        }

    }

}
