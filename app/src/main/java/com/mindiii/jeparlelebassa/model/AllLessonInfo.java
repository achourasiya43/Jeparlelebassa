package com.mindiii.jeparlelebassa.model;

/**
 * Created by mindiii on 21/4/17.
 */

public class AllLessonInfo {

    public String totalScore;
    public String lessonScore;
    public LessonInfo lessonInfo;

    public void AllEducationInfo(){
        this.totalScore = "";
        this.lessonScore = "";

    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getLessonScore() {
        return lessonScore;
    }

    public void setLessonScore(String lessonScore) {
        this.lessonScore = lessonScore;
    }
}
