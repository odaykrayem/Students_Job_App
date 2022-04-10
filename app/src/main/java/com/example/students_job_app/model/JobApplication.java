package com.example.students_job_app.model;

public class JobApplication {
    private int id;
    private String jobTitle;
    private String company;
    private String jobLocation;
    private int status;
    private String date;

    public JobApplication(int id, String jobTitle, String company, String jobLocation, int status, String date) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.company = company;
        this.jobLocation = jobLocation;
        this.status = status;
        this.date = date;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public int getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public int getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
