package com.example.students_job_app.model;

public class Interest {

    private int id;
    private String interestName;

    public Interest(int id, String tag) {
        this.id = id;
        this.interestName = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return interestName;
    }

    public void setTag(String tag) {
        this.interestName = tag;
    }


}
