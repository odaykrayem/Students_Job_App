package com.example.students_job_app;

import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.model.Course;
import com.example.students_job_app.model.Interest;
import com.example.students_job_app.model.JobApplication;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.model.JobRequest;
import com.example.students_job_app.model.Student;

import java.util.ArrayList;

public interface apiRequests {

    /**
     * User Types:
     *   int USER_TYPE_STUDENT = 0;
     *   int USER_TYPE_ADVERTISER = 1;
     */

    /**
     *    Gender types:
     *      int MALE = 0;
     *      int FEMALE = 1;
     */
    //log in for all users
    Student log_in(String email, String password, int type);
    //Register
    Student register_student(String name, String user_name, String email, String phone, String birth_date, int gender, String study_type, String study_place, String start_study, String end_study,boolean study_is_going, String cv);
    Student student_update_profile(String name, String user_name, String phone, String study_type, String study_place, String start_study, String end_study,boolean study_is_going, String cv);

    /**
     *  Job Opportunity:
     *   int id;
     *   String name;
     *   String advertiser;
     *   String location;
     *   String details;
     *   String required_skills;
     *   String position;
     */
    ArrayList<JobOpportunity> get_jobs();
    /**
     * Job Application:
     *  int id;
     *  String name;
     *  String advertiser;
     *  String location;
     *  int status;
     *  String date;
     */
    /**
     * status:
     *      int JOB_REQUEST_PROCESSING = 0;
     *      int JOB_REQUEST_REJECTED = -1;
     *      int JOB_REQUEST_ACCEPTED = 1;
     */
    ArrayList<JobApplication> get_applications(int student_id);

    boolean apply_job(int job_id, int student_id);
    boolean add_course(int student_id, String institution, String course_name, String start_date, String end_date);
    boolean add_interest(int student_id, String interest);
    //student should pay amount of money after get accepted in a a job
    boolean has_bill(int user_id);
    int get_bill(int user_id);
    boolean pay_bill(int user_id);
    ArrayList<Course> get_courses(int user_id);
    ArrayList<Interest> get_interests(int user_id);

    //===========Company ===========
    /**
     * Advertiser:
     *      int id;
     *      String companyName;
     *      String phone;
     *      String email;
     *    //optional :
     *      String website;
     *      String location;
     *      String professional_field;
     *      int years_of_incorporation;
     */
    Advertiser register_advertiser(String advertiser_name, String phone, String email, String password, String website, String location, int years_of_incorporation, String professional_fields);
    Advertiser update_advertiser(String advertiser_name, String phone, String website, String location, int years_of_incorporation, String professional_fields);
    ArrayList<JobOpportunity> get_posted_job(int advertiser_id);
    boolean delete_job(int advertiser_id, int job_id);
    //advertiser should pay a commission after a month of using the app
    boolean has_bill_advertiser(int advertiser_id, int amount);
    int get_bill_advertiser(int advertiser_id);
    boolean pay_bill_advertiser(int user_id);

    /**
     * Job Request :
     *   int id;
     *   String job_title;
     *   String studentName;
     *   String date;
     *   int status;
     */
    ArrayList<JobRequest> get_job_requests(int advertiser_id);
    /**
     * status:
     *      int JOB_REQUEST_PROCESSING = 0;
     *      int JOB_REQUEST_REJECTED = -1;
     *      int JOB_REQUEST_ACCEPTED = 1;
     */
    boolean change_request_status(int advertiser_id, int job_id, int status);

}
