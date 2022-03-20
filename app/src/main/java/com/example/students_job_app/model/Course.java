package com.example.students_job_app.model;

public class Course {

    private int id;
    private String name;
    private String institution;
    private String startDate;
    private String EndDate;

    public Course(String name, String institution, String startDate, String endDate) {
        this.name = name;
        this.institution = institution;
        this.startDate = startDate;
        EndDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstitution() {
        return institution;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
