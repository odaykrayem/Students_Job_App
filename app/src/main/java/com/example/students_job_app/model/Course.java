package com.example.students_job_app.model;

public class Course {

    private int id;
    private String courseName;
    private String institution;
    private String startDate;
    private String EndDate;

    public Course(int id, String courseName, String institution, String startDate, String endDate) {
        this.id = id;
        this.courseName = courseName;
        this.institution = institution;
        this.startDate = startDate;
        this.EndDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
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

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", institution='" + institution + '\'' +
                ", startDate='" + startDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                '}';
    }
}
