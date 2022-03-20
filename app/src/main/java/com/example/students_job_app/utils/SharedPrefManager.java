package com.example.students_job_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.model.Course;
import com.example.students_job_app.model.Student;

import java.util.ArrayList;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "generalFile";

    //student registration/ login
    private static final String KEY_ID = "keyid";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_BIRTH_DATE = "keybirthdate";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_STUDY_TYPE = "keystudytype";
    private static final String KEY_STUDY_PLACE = "keystudy_place";
    private static final String KEY_STUDY_START = "keystudystart";
    private static final String KEY_STUDY_END = "keystudyend";
    private static final String KEY_CV = "keycv";
    private static final String KEY_STUDY_IS_GOING = "keyisgoing";

    private static final String KEY_USER_TYPE = "keyusertype";

    private static final String KEY_ADV_LOCATION = "keyadvloaction";
    private static final String KEY_ADV_YEARS_OF_INC= "keyadvyears";
    private static final String KEY_ADV_FIELD = "keyadvfield";
    private static final String KEY_ADV_WEBSITE = "keyadvwebsite";

    private static SharedPrefManager mInstance;
    private static Context context;

    public SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the student data in shared preferences
    public void studentLogin(Student student) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, student.getId());
        editor.putString(KEY_NAME, student.getName());
        editor.putString(KEY_USER_NAME, student.getName());
        editor.putString(KEY_EMAIL, student.getEmail());
        editor.putString(KEY_PHONE, student.getPhone());
        editor.putString(KEY_BIRTH_DATE, student.getBirthDate());
        editor.putInt(KEY_GENDER, student.getGender());
        editor.putString(KEY_STUDY_TYPE, student.getTypeOfStudy());
        editor.putString(KEY_STUDY_PLACE, student.getPlaceOfStudy());
        editor.putString(KEY_STUDY_START, student.getStartOfStudy());
        editor.putString(KEY_STUDY_END, student.getEndOfStudy());
        editor.putBoolean(KEY_STUDY_IS_GOING, student.isStudyIsGoing());
        editor.putString(KEY_CV, student.getCv());
        editor.putInt(KEY_USER_TYPE, Constants.USER_TYPE_STUDENT);
        editor.apply();
    }
    //this method will store the student data in shared preferences
    public void advertiserLogin(Advertiser advertiser) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, advertiser.getId());
        editor.putString(KEY_NAME, advertiser.getCompanyName());
        editor.putString(KEY_PHONE, advertiser.getPhone());
        editor.putString(KEY_EMAIL, advertiser.getEmail());
        editor.putString(KEY_ADV_WEBSITE, advertiser.getWebsite());
        editor.putString(KEY_ADV_LOCATION, advertiser.getLocation());
        editor.putString(KEY_ADV_FIELD, advertiser.getProfessional_field());
        editor.putString(KEY_ADV_YEARS_OF_INC, advertiser.getYears_of_incorporation());
        editor.putInt(KEY_USER_TYPE, Constants.USER_TYPE_ADVERTISER);

        editor.apply();
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1) != -1;
    }

    //this method will give the logged in user id
    public int getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1);
    }

    //this method will give the logged in user
    public Student getStudentData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Student(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_BIRTH_DATE, null),
                sharedPreferences.getInt(KEY_GENDER, -1),
                sharedPreferences.getString(KEY_STUDY_TYPE, null),
                sharedPreferences.getString(KEY_STUDY_PLACE, null),
                sharedPreferences.getString(KEY_STUDY_START, null),
                sharedPreferences.getString(KEY_STUDY_END, null),
                sharedPreferences.getBoolean(KEY_STUDY_IS_GOING, false),
                sharedPreferences.getString(KEY_CV, null)
        );
    }
    public Advertiser getAdvertiserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Advertiser(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_ADV_WEBSITE, null),
                sharedPreferences.getString(KEY_ADV_LOCATION, null),
                sharedPreferences.getString(KEY_ADV_FIELD, null),
                sharedPreferences.getString(KEY_ADV_YEARS_OF_INC, null)
        );
    }

    public int getUserType() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_TYPE, -1);

    }

        //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }
}
