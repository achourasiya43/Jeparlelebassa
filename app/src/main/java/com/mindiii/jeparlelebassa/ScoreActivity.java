package com.mindiii.jeparlelebassa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    TextView tvLessonScore,tvTotalQuestions;
    ImageButton  ivDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_score_layout);
        tvLessonScore = (TextView) findViewById(R.id.tv_lesson_score1);
        tvTotalQuestions = (TextView) findViewById(R.id.total_questions);
        ivDone  = (ImageButton) findViewById(R.id.iv_done);


        Intent intent = getIntent();
        String lessonScore = intent.getStringExtra("lessonScore");
        String totalQuestion= intent.getStringExtra("totalqwesion");

        if(lessonScore.equals("null") || lessonScore.equals("")){
           tvLessonScore.setText("0");
        }
        else {
            tvLessonScore.setText(lessonScore);

        }

        if(totalQuestion.equals("null") || totalQuestion.equals("")){
            tvTotalQuestions.setText("0");
        }
        else {
            tvTotalQuestions.setText(totalQuestion);

        }

        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
