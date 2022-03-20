package com.example.students_job_app.model;

public class JobApplication {
    private int id;
    private String name;
    private String company;
    private String location;
    private int status;
    private String date;

    public JobApplication(int id, String name, String company, String location, int status, String date) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.location = location;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return id;
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

    public int getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
