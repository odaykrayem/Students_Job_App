package com.example.students_job_app.utils;

public class Urls {

    public static final String BASE_URL = "http://std.scit.co/std-jobs/public/api/";
//    public static final String BASE_URL = "http://192.168.43.130/std-student-job/public/api/";
    public static final String BASE_URL_FILE = "http://std.scit.co/std-jobs/public/storage/upload/";
//    public static final String BASE_URL_FILE = "http://192.168.43.130/std-student-job/public/";


    public static final String LOG_IN = BASE_URL + "login";
    public static final String EMAIL_VERIFICATION = BASE_URL + "";

    //=========================Student========================================
    public static final String REGISTER_STUDENT = BASE_URL + "register_student";
    public static final String UPDATE_STUDENT =  BASE_URL + "update_student";
    public static final String STUDENT_HAS_BILL =  BASE_URL + "student_has_bill";
    public static final String STUDENT_PAY_BILL =  BASE_URL + "student_pay_bill";
    public static final String ADVERTISER_PAY_BILL =  BASE_URL + "advertiser_pay_bill";

    public static final String GET_JOBS = BASE_URL + "get_job_opportunities";
    public static final String GET_APPLICATIONS = BASE_URL + "get_job_applications";
    public static final String APPLY_JOB = BASE_URL + "apply_job";
    public static final String GET_COURSES = BASE_URL + "get_courses";
    public static final String GET_INTERESTS = BASE_URL + "get_interests";

    public static final String ADD_COURSE = BASE_URL + "add_course";
    public static final String ADD_INTEREST = BASE_URL + "add_interest";

    //==========================Advertiser=================================//
    public static final String REGISTER_ADVERTISER= BASE_URL + "register_advertiser";
    public static final String UPDATE_ADVERTISER = BASE_URL + "update_advertiser";
    public static final String CHANGE_REQUEST_STATUS = BASE_URL + "change_request_status";;
    public static final String GET_REQUESTS = BASE_URL + "get_job_requests";
    public static final String GET_POSTED_JOBS = BASE_URL + "get_posted_jobs";
    public static final String POST_JOB = BASE_URL + "post_job_opportunity";
    public static final String DELETE_JOB =  BASE_URL + "delete_job_opportunity";
    public static final String GET_STUDENT_INFO =  BASE_URL +"";
    public static final String ADVERTISER_HAS_BILL = BASE_URL + "advertiser_has_bill";
}
