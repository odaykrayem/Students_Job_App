package com.example.students_job_app.model;

public class JobRequest {
    private  int id;
    private String job_title;
    private String studentName;
    private String date;
    private int status;

    public JobRequest(int id, String job_title, String studentName, String date, int status) {
        this.id = id;
        this.job_title = job_title;
        this.studentName = studentName;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getJob_title() {
        return job_title;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }
}
