package com.example.students_job_app.model;

import com.example.students_job_app.utils.Urls;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    private int id;
    private String userName;
    private String name;
    private String phone;
    private String email;
    private String birthDate;
    private int gender;
    private String studyType;
    private String studyPlace;
    private String studyStartDate;
    private String studyEndDate;
    private boolean studyIsGoing;
    private String cv;

    private ArrayList<String> interests;
    private ArrayList<Course> courses;

    public Student(int id, String userName, String name, String phone, String email, String birthDate, int gender, String studyType, String studyPlace, String studyStartDate, String studyEndDate, boolean studyIsGoing, String cv) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.studyType = studyType;
        this.studyPlace = studyPlace;
        this.studyStartDate = studyStartDate;
        this.studyEndDate = studyEndDate;
        this.studyIsGoing = studyIsGoing;
        this.cv = Urls.BASE_URL_FILE +  cv;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getGender() {
        return gender;
    }

    public String getStudyType() {
        return studyType;
    }

    public String getStudyPlace() {
        return studyPlace;
    }

    public String getStudyStartDate() {
        return studyStartDate;
    }

    public String getStudyEndDate() {
        return studyEndDate;
    }

    public boolean isStudyIsGoing() {
        return studyIsGoing;
    }

    public String getCv() {
        return cv;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }
}
