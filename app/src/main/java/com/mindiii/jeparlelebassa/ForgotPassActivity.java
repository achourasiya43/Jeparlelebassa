package com.mindiii.jeparlelebassa;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindiii.jeparlelebassa.Util.MySnackBar;
import com.mindiii.jeparlelebassa.Util.SetFont;
import com.mindiii.jeparlelebassa.Util.Util;
import com.mindiii.jeparlelebassa.Util.Validation;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassActivity extends AppCompatActivity {

    ImageView ivBack;
    ImageButton ibSendMail;
    EditText edEmail;
    TextView tvforgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        setVeriaable();
        setAllVeriable();
    }

    private void setVeriaable(){
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ibSendMail = (ImageButton) findViewById(R.id.iv_send_mail);
        edEmail = (EditText) findViewById(R.id.ed_email);
        tvforgotpassword = (TextView) findViewById(R.id.tv_forgot_pass);
        MySnackBar.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_snackbar);

        SetFont font = new SetFont();
        font.browau(this,edEmail);
        font.browaub(this,tvforgotpassword);
    }

    private void setAllVeriable(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
        ibSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidField()){
                    forgotUserPassWord();
                }
            }
        });
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

        }
        return true;
    }

    private void forgotUserPassWord() {
        if(Util.isConnectingToInternet(ForgotPassActivity.this)) {

            Util.showProgressDialog(this, "Loading...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.ForgotPassWord,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("#"+response);
                            JSONObject jsonObject = null;

                            try {

                                jsonObject = new JSONObject(response);

                                if(jsonObject!=null){

                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");

                                    if(status.equals("success")){
                                        Util.dismissProgressDialog();
                                        MySnackBar.show(message);


                                    }else{
                                        Util.dismissProgressDialog();
                                        MySnackBar.show(message);
                                    }
                                }

                            } catch (JSONException e) {
                                Util.dismissProgressDialog();
                                e.printStackTrace();
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
                    Map<String,String> params = new HashMap<String, String>();
                    String email = edEmail.getText().toString().trim();
                    params.put("email",email);

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
