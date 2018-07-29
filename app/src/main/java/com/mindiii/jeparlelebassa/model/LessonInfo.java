package com.mindiii.jeparlelebassa.model;

import java.io.Serializable;

/**
 * Created by mindiii on 18/4/17.
 */

public class LessonInfo implements Serializable {


    public int ansType;
    public String question;
    public String questionId;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public String answer;
    public String useranswer;

    public LessonInfo() {
        this.question = "";
        this.questionId = "";
        this.option1 = "";
        this.option2 = "";
        this.option3 = "";
        this.option4 = "";
        this.answer = "";
        this.useranswer = "";

    }
}
