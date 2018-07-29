package com.mindiii.jeparlelebassa.model;

import java.io.Serializable;

/**
 * Created by mindiii on 17/4/17.
 */

public class EducationInfo implements Serializable {

    public Integer SerialNum,LessonId;
    public String LessonName;
    public String TotalScore;
    public int serialNo;

    public EducationInfo(){
        this.SerialNum = null;
        this.LessonId = null ;
        this.LessonName = "";
        this.TotalScore = "";

    }
}
