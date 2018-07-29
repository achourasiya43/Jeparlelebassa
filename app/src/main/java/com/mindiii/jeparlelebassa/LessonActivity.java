package com.mindiii.jeparlelebassa;


import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindiii.jeparlelebassa.Util.MySnackBar;
import com.mindiii.jeparlelebassa.Util.Util;
import com.mindiii.jeparlelebassa.adapter.CustomPagerAdapter;
import com.mindiii.jeparlelebassa.helper.Constant;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.AllLessonInfo;
import com.mindiii.jeparlelebassa.model.EducationInfo;
import com.mindiii.jeparlelebassa.model.LessonInfo;
import com.mindiii.jeparlelebassa.model.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LessonActivity extends AppCompatActivity {

    private static int CHECK_CODE = 1002;

    CustomPagerAdapter customPagerAdapter;
    ImageView ivBack;
    ArrayList<LessonInfo> lessonList;
    SessionManager sessionManager;
    UserInfo userInfo;
    String lessonId;
    TextView TvNoQuestionFound,TvLessonSerial;
    ViewPager viewPager;
    String lessonscore;

    EducationInfo educationInfo;

    public static boolean isTTSisAvailable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_layout);

        lessonList = new ArrayList<>();

        sessionManager = new SessionManager(this);
        userInfo = sessionManager.getUserInfo();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
           // lessonId = bundle.getString("lessonId");
            educationInfo = (EducationInfo) bundle.getSerializable("lesson");
            lessonId = String.valueOf(educationInfo.LessonId);
            if(!lessonId.equals("")){
                if(Util.isConnectingToInternet(this)) {
                    questionList();
                }else {
                    MySnackBar.show(Constant.NO_INTERNET_CONNECTION_FOUND);
                }
            }

        }

        findoutveriable();
        viewPager = (ViewPager) findViewById(R.id.viewpager);

       // viewPager.setCurrentItem(0, true);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                onBackPressed();
            }
        });

        checkTTS();
    }


    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                Log.d("Result","OK");
                isTTSisAvailable = true;
            }else {
                isTTSisAvailable = false;
                Toast.makeText(this, "Google text to speech is not install", Toast.LENGTH_SHORT).show();
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    public int getItemPosition(){
        return viewPager.getCurrentItem();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void findoutveriable(){
        ivBack = (ImageView) findViewById(R.id.iv_back);
        TvNoQuestionFound = (TextView) findViewById(R.id.tv_no_question_found);
        TvLessonSerial = (TextView) findViewById(R.id.tv_lesson_serial);
        MySnackBar.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_snackbar);
        TvLessonSerial.setText(getResources().getString (R.string.lesson)+" "+(educationInfo.serialNo+1));

    }

    private void questionList() {
            Util.showProgressDialog(this, getResources().getString(R.string.loading));
            StringRequest stringRequest = new StringRequest(Request.Method.GET, WebServices.QuestionList+"?lessonId="+lessonId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.dismissProgressDialog();
                            System.out.println("#"+response);
                            JSONObject jsonObject = null;

                            try {
                                jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                   // MySnackBar.show(message);

                                    if(status.equals("fail")){
                                        TvNoQuestionFound.setVisibility(View.VISIBLE);

                                    }
                                    if(status.equals("success")){
                                        TvNoQuestionFound.setVisibility(View.GONE);


                                        JSONObject object = jsonObject.getJSONObject("questionList");

                                         JSONArray jsonArray = object.getJSONArray("QuestionData");

                                        AllLessonInfo allLessonInfo = new AllLessonInfo();
                                        int j = 0;
                                        for(int i = 0; i<jsonArray.length(); i++){

                                            allLessonInfo.lessonInfo = new LessonInfo();
                                           JSONObject object2 = jsonArray.getJSONObject(i);

                                            allLessonInfo.lessonInfo.questionId = object2.getString("id");
                                            allLessonInfo.lessonInfo.question = object2.getString("question");

                                            if(object2.has("option")) {
                                                allLessonInfo.lessonInfo.ansType = 1;
                                                JSONObject jsonObject1 = object2.getJSONObject("option");
                                                allLessonInfo.lessonInfo.option1 = jsonObject1.getString("option1");
                                                allLessonInfo.lessonInfo.option2 = jsonObject1.getString("option2");
                                                allLessonInfo.lessonInfo.option3 = jsonObject1.getString("option3");
                                                allLessonInfo.lessonInfo.option4 = jsonObject1.getString("option4");
                                            }else {
                                                allLessonInfo.lessonInfo.ansType = 2;
                                            }
                                            allLessonInfo.lessonInfo.answer = object2.getString("answer");
                                            allLessonInfo.lessonInfo.useranswer = object2.getString("userAnswer");
                                            lessonList.add(allLessonInfo.lessonInfo);

                                        }
                                        allLessonInfo.totalScore = object.getString("totalScore");
                                        lessonscore = object.getString("lessonScore");
                                        customPagerAdapter = new CustomPagerAdapter(lessonList,LessonActivity.this,viewPager, lessonId,lessonscore);
                                        viewPager.setAdapter(customPagerAdapter);

                                        sessionManager.setScore(allLessonInfo.totalScore);


                                    }else{
                                        Util.dismissProgressDialog();
                                        MySnackBar.show(message);
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
                public Map<String,String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("authToken",""+userInfo.authToken);
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


    }
}
