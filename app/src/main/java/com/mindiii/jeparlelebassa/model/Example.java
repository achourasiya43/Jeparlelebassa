package com.mindiii.jeparlelebassa.model;

import java.util.List;

/**
 * Created by mindiii on 22/4/17.
 */

public class Example {
    public String totalScore;
    public String lessonScore;
    public List<LessonInfo> lessonInfo;

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

    public List<LessonInfo> getLessonInfo() {
        return lessonInfo;
    }

    public void setLessonInfo(List<LessonInfo> lessonInfo) {
        this.lessonInfo = lessonInfo;
    }
}
