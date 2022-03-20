package com.example.students_job_app.model;

import java.util.ArrayList;

public class Student {
    private int id;
    private String userName;
    private String name;
    private String phone;
    private String email;
    private String birthDate;
    private int gender;
    private String typeOfStudy;
    private String placeOfStudy;
    private String startOfStudy;
    private String endOfStudy;
    private boolean studyIsGoing;
    private String cv;
    private ArrayList<String> interests;
    private ArrayList<Course> courses;

    public Student(int id, String userName, String name, String phone, String email, String birthDate, int gender, String typeOfStudy, String placeOfStudy, String startOfStudy, String endOfStudy, boolean studyIsGoing, String cv) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.typeOfStudy = typeOfStudy;
        this.placeOfStudy = placeOfStudy;
        this.startOfStudy = startOfStudy;
        this.endOfStudy = endOfStudy;
        this.studyIsGoing = studyIsGoing;
        this.cv = cv;
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

    public String getTypeOfStudy() {
        return typeOfStudy;
    }

    public String getPlaceOfStudy() {
        return placeOfStudy;
    }

    public String getStartOfStudy() {
        return startOfStudy;
    }

    public String getEndOfStudy() {
        return endOfStudy;
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
