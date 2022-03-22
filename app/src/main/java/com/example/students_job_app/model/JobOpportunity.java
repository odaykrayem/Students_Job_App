package com.example.students_job_app.model;

public class JobOpportunity {

    private int id;
    private String name;
    private String company;
    private String advertiser;
    private String location;
    private String position;
    private String required_skills;
     private String details;
     private String date;


    public JobOpportunity(int id, String name, String company, String advertiser, String location, String position, String required_skills, String details, String date) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.advertiser = advertiser;
        this.location = location;
        this.position = position;
        this.required_skills = required_skills;
        this.details = details;
        this.date = date;
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

    public String getAdvertiser() {
        return advertiser;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

}
