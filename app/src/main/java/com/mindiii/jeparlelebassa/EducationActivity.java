package com.mindiii.jeparlelebassa;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mindiii.jeparlelebassa.adapter.EducationListAdapter;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.AllEducationInfo;
import com.mindiii.jeparlelebassa.model.EducationInfo;
import com.mindiii.jeparlelebassa.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EducationActivity extends AppCompatActivity {
    ImageView ivBack;
    RecyclerView recyclerView;
    EducationListAdapter educationListAdapter;
    ArrayList<EducationInfo> eduactionList;
    ProgressBar progressBar;
    TextView tvNoLessonFound;
    int CategoryId;
    SessionManager sessionManager;
    UserInfo userInfo;
    int lessonSerial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        findoutVeriable();
        setAlVeriable();
        eduactionList = new ArrayList<>();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            CategoryId = Integer.parseInt(bundle.getString("ID"));
            getAllLesson();
        }
        sessionManager = new SessionManager(this);
        userInfo = sessionManager.getUserInfo();

       /* Intent intent = getIntent();
        CategoryId = intent.getIntExtra("ID",0);
        getAllLesson();*/

    }
    private  void findoutVeriable(){
        ivBack = (ImageView) findViewById(R.id.iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_education);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_lesson);
        MySnackBar.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_snackbar);
        tvNoLessonFound = (TextView) findViewById(R.id.tv_no_lesson_found);

    }
    private void setAlVeriable(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();

            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    private void getAllLesson() {
        if(Util.isConnectingToInternet(EducationActivity.this)) {

            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, WebServices.AllLesson+"?categoryId="+CategoryId,
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
                                        tvNoLessonFound.setVisibility(View.GONE);
                                        eduactionList.clear();
                                        progressBar.setVisibility(View.GONE);

                                        JSONObject object = jsonObject.getJSONObject("lessonDetail");

                                        JSONArray array = object.getJSONArray("lessonData");

                                        if(array.length()==0){
                                            tvNoLessonFound.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        AllEducationInfo allEducationInfo = new AllEducationInfo();


                                        for(int i= 0;i<array.length();i++){


                                            allEducationInfo.educationInfo = new EducationInfo();
                                            JSONObject object1 = array.getJSONObject(i);
                                            allEducationInfo.educationInfo.LessonId = object1.getInt("id");
                                            allEducationInfo.educationInfo.LessonName = object1.getString("lessonName");
                                            allEducationInfo.educationInfo.serialNo = i;
                                           // allEducationInfo.educationInfo.SerialNum = (++lessonSerial);

                                            eduactionList.add( allEducationInfo.educationInfo);

                                        }
                                        allEducationInfo.totalScore = object.getString("totalScore");


                                        educationListAdapter = new EducationListAdapter(eduactionList,EducationActivity.this);
                                        recyclerView.setAdapter(educationListAdapter);

                                    }else{
                                        tvNoLessonFound.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        MySnackBar.show(message);
                                    }
                                }

                            } catch (JSONException e) {
                                progressBar.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            MySnackBar.show(getResources().getString(R.string.somthing_went_wrong));
                        }
                    }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("authToken", userInfo.authToken);
                    return headers;
                }

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
               // params.put("categoryId",CategoryId+"");
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
}
