package com.example.students_job_app.model;

public class Advertiser {

    private int id;
    private String advertiserName;
    private String phone;
    private String email;
    private String website;
    private String address;
    private String description;
    private String professional_field;
    private String years_of_incorporation;

    public Advertiser(int id, String name, String phone, String email, String website, String description, String address, String professional_field, String years_of_incorporation) {
        this.id = id;
        this.advertiserName = name;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.description = description;
        this.address = address;
        this.professional_field = professional_field;
        this.years_of_incorporation = years_of_incorporation;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getAdvertiserName() {
        return advertiserName;
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

    public String getAddress() {
        return address;
    }

    public String getProfessional_field() {
        return professional_field;
    }

    public String getYears_of_incorporation() {
        return years_of_incorporation;
    }
}
