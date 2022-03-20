package com.example.students_job_app.model;

public class JobRequest {
    private  int id;
    private String job_title;
    private String studentName;
    private String date;
    private int status;

    public JobRequest(int id, String studentName, String date, int status) {
        this.id = id;
        this.studentName = studentName;
        this.date = date;
        this.status = status;
    }
}
