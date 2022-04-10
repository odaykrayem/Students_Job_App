package com.example.students_job_app.model;

public class JobOpportunity {

    private int id;
    private String title;
    private String company;
    private String advertiserName;
    private String jobLocation;
    private String position;
    private String required_skills;
    private String details;
    private String created_at;


    public JobOpportunity(int id, String title, String company, String advertiserName, String jobLocation, String position, String required_skills, String details, String created_at) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.advertiserName = advertiserName;
        this.jobLocation = jobLocation;
        this.position = position;
        this.required_skills = required_skills;
        this.details = details;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getRequired_skills() {
        return required_skills;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDetails() {
        return details;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getJobLocation() {
        return jobLocation;
    }

}
