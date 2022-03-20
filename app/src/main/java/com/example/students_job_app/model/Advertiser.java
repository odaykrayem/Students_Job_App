package com.example.students_job_app.model;

public class Advertiser {

    private int id;
    private String companyName;
    private String phone;
    private String email;
    private String website;
    private String location;
    private String professional_field;
    private String years_of_incorporation;

    public Advertiser(int id, String companyName, String phone, String email, String website, String location, String professional_field, String years_of_incorporation) {
        this.id = id;
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.location = location;
        this.professional_field = professional_field;
        this.years_of_incorporation = years_of_incorporation;
    }

    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getLocation() {
        return location;
    }

    public String getProfessional_field() {
        return professional_field;
    }

    public String getYears_of_incorporation() {
        return years_of_incorporation;
    }
}
