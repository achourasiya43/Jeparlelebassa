package com.mindiii.jeparlelebassa.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindiii.jeparlelebassa.LessonActivity;
import com.mindiii.jeparlelebassa.R;
import com.mindiii.jeparlelebassa.ScoreActivity;
import com.mindiii.jeparlelebassa.Util.MySnackBar;
import com.mindiii.jeparlelebassa.Util.Util;
import com.mindiii.jeparlelebassa.helper.SessionManager;
import com.mindiii.jeparlelebassa.helper.WebServices;
import com.mindiii.jeparlelebassa.model.AllLessonInfo;
import com.mindiii.jeparlelebassa.model.LessonInfo;
import com.mindiii.jeparlelebassa.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.mindiii.jeparlelebassa.LessonActivity.isTTSisAvailable;

/**
 * Created by mindiii on 19/4/17.
 */

// Custom pager adapter not using fragments
public  class CustomPagerAdapter extends PagerAdapter implements TextToSpeech.OnInitListener {

    private ViewPager viewPager;
    private ImageView ivSpeak;
    private TextToSpeech tts;
    private TextView tvQuetion,tvSkip,tvquestionNum,tvShowScore;
    private ImageButton ibSubmit;
    private RadioGroup  radioGroup;
    private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    private String lessonId;

    private RelativeLayout relativesecandlayout;

    String radiovalue = null;
    String ans;
    LessonActivity activity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<LessonInfo> pages;
    private UserInfo userInfo;
    EditText edAnswer;
    int speekStatus;
    int TotalQuesion,pos;
    SessionManager  sessionManager;
    String lessonScore;



    public CustomPagerAdapter(ArrayList<LessonInfo> pages, LessonActivity activity, ViewPager viewPager, String lessonId,String lessonScore) {
        this.activity = activity;
        this.viewPager = viewPager;
        this.pages = pages;
        this.lessonId = lessonId;
        this.lessonScore = lessonScore;
        mLayoutInflater = LayoutInflater.from(activity);
        SessionManager sessionManager = new SessionManager(activity);
        userInfo = sessionManager.getUserInfo();
        if(isTTSisAvailable = true){
            tts = new TextToSpeech(activity,this);
        }

        speakOut("");
    }
    // Returns the number of pages to be displayed in the ViewPager.
    @Override
    public int getCount() {
        return pages.size();
    }

    // Returns true if a particular object (page) is from a particular page
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // This method should create the page for the given position passed to it as an argument.
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        // Inflate the layout for the page
       final View itemView = mLayoutInflater.inflate(R.layout.activity_lesson, container, false);
        // Find and populate data into the page (i.e set the image)
        final  LessonInfo lessonInfo = pages.get(position);
         radiovalue = "";
        sessionManager = new SessionManager(activity);

        initView(itemView);
        setIfUserAnswered(lessonInfo,position);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                pos = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
                switch (i){
                    case R.id.radioButton1:
                        radiovalue = pages.get(position).option1;
                        break;
                    case R.id.radioButton2:
                        radiovalue = pages.get(position).option2;
                        break;
                    case R.id.radioButton3:
                        radiovalue = pages.get(position).option3;
                        break;
                    case R.id.radioButton4:
                        radiovalue = pages.get(position).option4;
                        break;
                }
            }
        });
        TotalQuesion =  pages.size();

        if(pages.get(position).useranswer!=null) {
            Log.e("position", position + "");
            if ((pages.get(position).useranswer.equals("option1"))) {
                radioButton1.setChecked(true);
               /* radioButton1.setEnabled(false);
                radioButton2.setEnabled(false);
                radioButton3.setEnabled(false);
                radioButton4.setEnabled(false);*/

            } else if ((pages.get(position).useranswer.equals("option2"))) {
                radioButton2.setChecked(true);
               /* radioButton1.setEnabled(false);
                radioButton2.setEnabled(false);
                radioButton3.setEnabled(false);
                radioButton4.setEnabled(false);*/
            } else if ((pages.get(position).useranswer).equals("option3")) {
                radioButton3.setChecked(true);
             /*   radioButton1.setEnabled(false);
                radioButton2.setEnabled(false);
                radioButton3.setEnabled(false);
                radioButton4.setEnabled(false);*/

            } else if ((pages.get(position).useranswer).equals("option4")) {
                radioButton4.setChecked(true);
               /* radioButton1.setEnabled(false);
                radioButton2.setEnabled(false);
                radioButton3.setEnabled(false);
                radioButton4.setEnabled(false);*/

            }
        }

        tvSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(position+1, true);

                }
            });

        ivSpeak.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String text = pages.get(viewPager.getCurrentItem()).question.toString();

               speakOut(text);
            }
        });

        ibSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewPager.getCurrentItem();
                LessonInfo lessonInfo = pages.get(position);

                /*if(checkIsAnswerCurrentQuation(lessonInfo)){
                    MySnackBar.show(activity.getResources().getString(R.string.you_already_submitted_answer));
                    return;
                }*/

                if(lessonInfo.ansType == 1){

                    if(!radiovalue.equals("")){
                        answerList(position,lessonInfo.questionId, lessonId,null, radiovalue);
                    }else {
                        MySnackBar.show(activity.getResources().getString(R.string.please_select_answer_first));
                       // Toast.makeText(activity, "Please select answer first.", Toast.LENGTH_SHORT).show();
                    }

                }else if(lessonInfo.ansType == 2) {
                    edAnswer = (EditText) itemView.findViewById(R.id.ed_answer);
                    String tmpAns = edAnswer.getText().toString();
                    if (!tmpAns.equals("")) {
                        answerList(position,lessonInfo.questionId, lessonId, tmpAns, null);
                    } else {
                        MySnackBar.show(activity.getResources().getString(R.string.please_input_answer_first));
                    }
                }
            }
        });

        if(pages.size() == (position+1)){

            tvSkip.setVisibility(View.GONE);
            tvShowScore.setVisibility(View.VISIBLE);

        }else {tvSkip.setVisibility(View.VISIBLE);
            tvShowScore.setVisibility(View.GONE);}

        tvShowScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                Intent intent = new Intent(activity, ScoreActivity.class);
                intent.putExtra("lessonScore",lessonScore+"");
                intent.putExtra("totalqwesion",TotalQuesion+"");
                activity.startActivity(intent);
                //activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        container.addView(itemView);
        // Return the page
        return itemView;
    }

    private void initView(View itemView) {
        ivSpeak = (ImageView) activity.findViewById(R.id.iv_speak);
        tvQuetion = (TextView) itemView.findViewById(R.id.tv_qwesion);
        tvSkip = (TextView) itemView.findViewById(R.id.tv_skip);
        ibSubmit = (ImageButton) itemView.findViewById(R.id.iv_submit);
        tvquestionNum = (TextView) itemView.findViewById(R.id.tv_quesion_count);
        tvShowScore = (TextView) itemView.findViewById(R.id.tv_show_score);
        edAnswer = (EditText) itemView.findViewById(R.id.ed_answer);
        radioGroup = (RadioGroup) itemView.findViewById(R.id.radioGroup);
        relativesecandlayout = (RelativeLayout) itemView.findViewById(R.id.relativeSecandLayout);
        radioButton1 = (RadioButton) itemView.findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) itemView.findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) itemView.findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) itemView.findViewById(R.id.radioButton4);

        if (speekStatus == tts.SUCCESS) {

            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                ivSpeak.setEnabled(true);

            }
        }
    }

    private boolean checkIsAnswerCurrentQuation(LessonInfo lessonInfo){
        if(lessonInfo.useranswer.equals("")){
            return false;
        }
        return true;
    }

    private void setIfUserAnswered(LessonInfo lessonInfo, int position){

        tvquestionNum.setText(position+1+"");
        tvQuetion.setText(lessonInfo.question);
        if(lessonInfo.ansType==1){
            radioGroup.setVisibility(View.VISIBLE);
            relativesecandlayout.setVisibility(View.GONE);
            radioButton1.setText(lessonInfo.option1);
            radioButton2.setText(lessonInfo.option2);
            radioButton3.setText(lessonInfo.option3);
            radioButton4.setText(lessonInfo.option4);

        }else if(lessonInfo.ansType==2){


            radioGroup.setVisibility(View.GONE);
            relativesecandlayout.setVisibility(View.VISIBLE);
        }

        if(lessonInfo.useranswer!=null){

            if(!lessonInfo.useranswer.isEmpty()){
                edAnswer.setText(lessonInfo.useranswer);
                edAnswer.setEnabled(false);
            }
        }
    }
    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        /*if (tts != null) {
            tts.stop();
            tts.shutdown();
        }*/
        container.removeView((View) object);
    }

    @Override
    public void onInit(int status) {
       speekStatus = status;
    }


    private void speakOut(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text,TextToSpeech.QUEUE_FLUSH, null,null);
        }
    }

    private void answerList(final int position,final String questionId,final String lessonId, final String ans, final String radiovalue) {
        if(Util.isConnectingToInternet(activity)) {

            //Toast.makeText(activity, "api called.."+ans+" ## "+radiovalue, Toast.LENGTH_LONG).show();
            Util.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.AnswerList,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("#"+response);
                            JSONObject jsonObject = null;
                            Util.dismissProgressDialog();
                            try {
                                jsonObject = new JSONObject(response);

                                if(jsonObject!=null){

                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");

                                    if(status.equals("success")){
                                        Util.dismissProgressDialog();
                                        if(pages.size() == (position+1)){

                                            lessonScore = jsonObject.getString("lessonScore");
                                            String totalscore = jsonObject.getString("totalScore");
                                            sessionManager.setScore(totalscore);


                                            activity.finish();
                                            Intent intent = new Intent(activity, ScoreActivity.class);
                                            intent.putExtra("lessonScore",lessonScore+"");
                                            intent.putExtra("totalqwesion",TotalQuesion+"");
                                            activity.startActivity(intent);


                                        }

                                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                                        JSONArray jsonArray = jsonObject.getJSONArray("questionList");

                                    }else{
                                        Util.dismissProgressDialog();
                                        MySnackBar.show(message);
                                    }
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
                            MySnackBar.show(activity.getResources().getString(R.string.somthing_went_wrong));

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

                    params.put("questionId",questionId);
                    params.put("lessonId",lessonId);

                    if(ans!=null){
                        params.put("answer",ans);


                    }else if(radiovalue != null){
                        switch (pos){
                            case 0:params.put("answer","option1");
                                break;
                            case 2:params.put("answer","option2");
                                break;
                            case 4:params.put("answer","option3");
                                break;
                            case 6:params.put("answer","option4");
                                break;
                        }


                    }
                    Log.e("Params",radiovalue+" : "+questionId+":"+ans);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);

        }else{
                MySnackBar.show(activity.getResources().getString(R.string.please_check_internet_connection));
        }
    }




}


