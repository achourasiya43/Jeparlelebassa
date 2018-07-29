package com.mindiii.jeparlelebassa.helper;

/**
 * Created by mindiii on 13/4/17.
 */

public class WebServices {
    //public static final String  BASE_URL = "http://mindiii.com/jeParle/service/";
    //public static final String  BASE_URL = "http://jeparlelebassa2point0.com/jeParle/index.php/service/";
    public static final String  BASE_URL = "http://api.jeparlelebassa2point0.com/jeParle/index.php/service/";


    public static final String Registration_Url = BASE_URL + "userRegistration";
    public static final String Login_Url = BASE_URL + "userLogin";
    public static final String AllCategory = BASE_URL + "user/getAllCategory";
    public static final String AllLesson = BASE_URL + "user/getAllLesson";
    public static final String GetLanguage = BASE_URL + "user/userLanguage";
    public static final String UpdateProfile = BASE_URL + "user/updateProfile";
    public static final String ForgotPassWord = BASE_URL + "forgotPassword";
    public static final String QuestionList = BASE_URL + "user/getQuestionList";
    public static final String AnswerList = BASE_URL + "user/usersAnswer";

}
