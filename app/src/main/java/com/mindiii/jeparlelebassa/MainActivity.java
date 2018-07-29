package com.mindiii.jeparlelebassa;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindiii.jeparlelebassa.Util.MySnackBar;
import com.mindiii.jeparlelebassa.Util.Util;
import com.mindiii.jeparlelebassa.adapter.MainListAdapter;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.MainListInfo;
import com.mindiii.jeparlelebassa.model.UserInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView navProfilePic;
    TextView navUserName,tvNocategory,tvNoInternet;
    SessionManager sessionManager;
    UserInfo userInfo;
    RecyclerView recyclerView;
    ArrayList<MainListInfo> mainArrayListInfo;
    MainListAdapter mainListAdapter;
    ProgressBar progressBar;
    Runnable runnable;
    boolean doubleBackToExitPressedOnce;
    int languageId = 1;
    TextView tvScore;
    int mode=1;
    ImageView ivEnglishToBassa,ivFranchToBassa;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
        tvNocategory = (TextView) findViewById(R.id.tv_nocategory_found);
        tvNoInternet = (TextView) findViewById(R.id.tv_no_internate_found);
        SharedPreferences prefs = getSharedPreferences("language", MODE_PRIVATE);
        String restoredText = prefs.getString("mode", null);
        if (restoredText != null)
            mode = Integer.parseInt(prefs.getString("mode", ""));//"No name defined" is the default value.
        mainArrayListInfo = new ArrayList<>();
        mainListAdapter = new MainListAdapter(MainActivity.this, mainArrayListInfo);
        getAllCategory(mode);

        sessionManager = new SessionManager(this);
        userInfo = sessionManager.getUserInfo();

        if(!sessionManager.isShowdialog()){
            customDialog();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        MySnackBar.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_snackbar);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
       /* SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);*/
        recyclerView.setLayoutManager(linearLayoutManager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View   headerview   =   navigationView.getHeaderView(0);
        navProfilePic = (ImageView) headerview.findViewById(R.id.profile_image);
        navUserName = (TextView) headerview.findViewById(R.id.nav_user_name);
        tvScore = (TextView) headerview.findViewById(R.id.tv_score);

        Picasso.with(this)
                .load(sessionManager.getUserInfo().getUserImage())
                .into(navProfilePic, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        navUserName.setText(sessionManager.getUserInfo().getFullName());

        if(sessionManager.getUserInfo().getTotalScore().equals("")){
            tvScore.setText("0");
        }else {
            String temp = sessionManager.getUserInfo().totalScore;
            tvScore.setText(temp);

        }
        toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(final View drawerView) {
                super.onDrawerOpened(drawerView);
                if(sessionManager!=null) {
                    navUserName.setText(sessionManager.getUserInfo().getFullName());
                    navProfilePic.setImageDrawable(null);

               /*     Picasso.with(MainActivity.this).load(sessionManager.getUserInfo().getUserImage())
                            .placeholder(R.drawable.profileimg).into(navProfilePic);*/
                    String temp = sessionManager.getUserInfo().totalScore;
                    tvScore.setText(temp);
                    Picasso.with(MainActivity.this)
                            .load(sessionManager.getUserInfo().getUserImage()).placeholder(R.drawable.profileimg)
                            .into(navProfilePic, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });

                }
                // Do whatever you want here
            }
        };
// Set the drawer toggle as the DrawerListener
        drawer.addDrawerListener(toggle);

    }

    public void customDialog(){
        final Dialog openDialog = new Dialog(this);
        openDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        openDialog.setContentView(R.layout.language_select_popup);
        openDialog.setCanceledOnTouchOutside(false);
        openDialog.setCancelable(false);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ivEnglishToBassa = (ImageView) openDialog.findViewById(R.id.iv_english_bassa);
        ivFranchToBassa = (ImageView) openDialog.findViewById(R.id.iv_franch_bassa);
        TextView ibLearning = (TextView) openDialog.findViewById(R.id.iv_btn_learning);

       /* String language= Locale.getDefault().getLanguage();
        if(language.equals("en")) {
            ivEnglishToBassa.setImageResource(R.drawable.circle_images2);
            ivFranchToBassa.setImageResource(R.drawable.circle_images);

        }
        else if(language.equals("fr")){
            ivEnglishToBassa.setImageResource(R.drawable.circle_images);
            ivFranchToBassa.setImageResource(R.drawable.circle_images2);
        }*/

        if(mode == 1){

            ivEnglishToBassa.setImageResource(R.drawable.circle_images2);
            ivFranchToBassa.setImageResource(R.drawable.circle_images);

        }else if(mode== 2){

            ivEnglishToBassa.setImageResource(R.drawable.circle_images);
            ivFranchToBassa.setImageResource(R.drawable.circle_images2);
        }

        ivEnglishToBassa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivFranchToBassa.setImageResource(R.drawable.circle_images);
                ivEnglishToBassa.setImageResource(R.drawable.circle_images2);
                mode = 1;
                SharedPreferences.Editor editor = getSharedPreferences("language", MODE_PRIVATE).edit();
                editor.putString("mode", "1");
                editor.apply();
                sessionManager.setlanguage("en");
                ivEnglishToBassa.animate().rotationYBy(360).setDuration(500).start();


            }
        });
        ivFranchToBassa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ivEnglishToBassa.setImageResource(R.drawable.circle_images);
                ivFranchToBassa.setImageResource(R.drawable.circle_images2);
                mode = 2;

                SharedPreferences.Editor editor = getSharedPreferences("language", MODE_PRIVATE).edit();
                editor.putString("mode", "2");
                editor.commit();
                sessionManager.setlanguage("fr");
                ivFranchToBassa.animate().rotationYBy(360).setDuration(500).start();

            }
        });

        ibLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setShowdialog(true);
                openDialog.dismiss();
                getAllCategory(mode);
                selectLanguage();
               /* Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
        openDialog.show();
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                getAllCategory(mode);
                break;
            case R.id.nav_user_profile:
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);

                break;
            case R.id.nav_language:
                customDialog();
                break;
            case R.id.nav_logout:
                SessionManager sessionManager = new SessionManager(MainActivity.this);
                sessionManager.logout(this);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectLanguage() {
        if(Util.isConnectingToInternet(MainActivity.this)) {

            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.GetLanguage,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           // System.out.println("#"+response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                if(status.equals("success")){
                                    userInfo.setLanguageType(languageId);
                                    sessionManager.createSession(userInfo);
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    MySnackBar.show(message);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            MySnackBar.show(error.getMessage());
                            //Toast.makeText(LoginActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<>();
                    headers.put("authToken", userInfo.authToken);
                    return headers;
                }
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    SharedPreferences prefs = getSharedPreferences("language", MODE_PRIVATE);
                    String restoredText = prefs.getString("mode", null);
                    if (restoredText != null) {
                        mode = Integer.parseInt(prefs.getString("mode", ""));//"No name defined" is the default value.

                    }
                 // int nsnc=  SessionManager.readInteger(MainActivity.this,SessionManager.Language,0);
                    params.put("languageId",mode+"");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else{
            progressBar.setVisibility(View.GONE);
            MySnackBar.show(getResources().getString(R.string.please_check_internet_connection));

        }
    }

    private void getAllCategory(int mode) {
        if(Util.isConnectingToInternet(MainActivity.this)) {
            tvNoInternet.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, WebServices.AllCategory+"?languageId="+mode,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           // System.out.println("#"+response);
                            navigationView.getMenu().getItem(0).setChecked(true);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                mainArrayListInfo.clear();
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                if(status.equals("fail")){
                                   // mainListAdapter.notifyDataSetChanged();
                                    mainArrayListInfo.clear();
                                    tvNocategory.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                                else if(status.equals("success")){

                                    tvNocategory.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    JSONArray array = jsonObject.getJSONArray("categoryDetail");

                                   for(int i = 0;i<array.length();i++){
                                       MainListInfo mainListInfo = new MainListInfo();
                                       JSONObject object = array.getJSONObject(i);

                                       mainListInfo.MainListItemId = object.getInt("id");
                                       mainListInfo.MainListItemName = object.getString("name");
                                       mainListInfo.MainListItemImage = object.getString("logo");

                                       mainArrayListInfo.add(mainListInfo);
                                   }
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    MySnackBar.show(message);
                                }
                                recyclerView.setAdapter(mainListAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            MySnackBar.show(error.getMessage());
                            //Toast.makeText(LoginActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("authToken", userInfo.authToken);
                    return headers;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else{
            mainArrayListInfo.clear();
            mainListAdapter.notifyDataSetChanged();
            tvNoInternet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            MySnackBar.show(getResources().getString(R.string.please_check_internet_connection));

            }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
             /* Handle double click to finish activity*/
            Handler handler = new Handler();
            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                MySnackBar.show(getResources().getString(R.string.click_again_to_exit));
                //Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
                // Util.showSnakbar(container, "Click again to exit", font);
                handler.postDelayed(runnable = new Runnable()
                {@Override
                public void run() {
                    doubleBackToExitPressedOnce = false;}
                }, 2000);
            }
            else {/*super.onBackPressed();*/
                handler.removeCallbacks(runnable);
                finish();
            }

        }
    }
}
