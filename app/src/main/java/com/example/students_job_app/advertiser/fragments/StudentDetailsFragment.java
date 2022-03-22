package com.example.students_job_app.advertiser.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.advertiser.adapters.JobRequestsAdapter;
import com.example.students_job_app.model.Course;
import com.example.students_job_app.model.Interest;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.student.StudentMain;
import com.example.students_job_app.student.StudentSignupActivity;
import com.example.students_job_app.student.adapters.CoursesAdapter;
import com.example.students_job_app.student.adapters.InterestsAdapter;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentDetailsFragment extends Fragment {

    TextView mUserNameTV, mNameTV, mPhoneTV, mEmailTV,mGender, mBirthDateTV, mPlaceOfStudyTV, mTypeOfStudyTV, mStartOfStudyTV, mEndOfStudyTV;
    CheckBox mOnGoingCB;
    RecyclerView mInterestsRV, mCoursesRV;
    Button mShowCVBtn, mAccept, mReject;
    ArrayList<Course> courseList;
    ArrayList<Interest> interestList;

    String id;
    Student student;

    JobRequestsAdapter.OnRequestButtonClicked onRequestButtonClicked;

    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public StudentDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            id = getArguments().getString(Constants.KEY_STUDENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserNameTV = view.findViewById(R.id.user_name);
        mNameTV = view.findViewById(R.id.name);
        mPhoneTV = view.findViewById(R.id.phone);
        mEmailTV = view.findViewById(R.id.email);
        mBirthDateTV = view.findViewById(R.id.birth);
        mPlaceOfStudyTV = view.findViewById(R.id.place_of_study);
        mTypeOfStudyTV = view.findViewById(R.id.type_of_study);
        mStartOfStudyTV = view.findViewById(R.id.start_of_study);
        mEndOfStudyTV = view.findViewById(R.id.end_of_study);
        mOnGoingCB = view.findViewById(R.id.ongoing);
        mCoursesRV = view.findViewById(R.id.rv_courses);
        mInterestsRV = view.findViewById(R.id.rv_interests);
        mShowCVBtn = view.findViewById(R.id.show_cv);
        mAccept = view.findViewById(R.id.accept);
        mReject = view.findViewById(R.id.reject);

        mAccept.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_accept_reuqest, null);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            TextView yes = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            yes.setOnClickListener(l->{
                onRequestButtonClicked.onAcceptSelected(id);
                dialog.dismiss();
            });

            no.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });
        mReject.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_reject_reuqest, null);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            TextView yes = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            yes.setOnClickListener(l->{
                onRequestButtonClicked.onRejectSelected(id);
                dialog.dismiss();
            });
            no.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });

        getStudentInfo(id);
        getCourses(id);
        getInterests(id);
    }

    private void getStudentInfo(String id) {
        String url = Urls.GET_STUDENT_INFO;
        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            student = new Student(
                                    Integer.parseInt(response.getString("id")),
                                    response.getString("user_name"),
                                    response.getString("name") + response.getString("nick_name"),
                                    response.getString("phone"),
                                    response.getString("email"),
                                    response.getString("birth_date"),
                                    Integer.parseInt(response.getString("gender")),
                                    response.getString("study_type"),
                                    response.getString("study_place"),
                                    response.getString("study_start"),
                                    response.getString("study_end_date"),
                                    response.getBoolean("is_going"),
                                    response.getString("cv")
                            );
                            mUserNameTV.setText(student.getUserName());
                            mNameTV.setText(student.getName());
                            mPhoneTV.setText(student.getPhone());
                            mEmailTV.setText(student.getEmail());
                            mGender.setText(student.getGender()==Constants.MALE?Constants.MALE_TXT:Constants.FEMALE_TXT);
                            mBirthDateTV.setText(student.getBirthDate());
                            mPlaceOfStudyTV.setText(student.getPlaceOfStudy());
                            mTypeOfStudyTV.setText(student.getTypeOfStudy());
                            mStartOfStudyTV.setText(student.getStartOfStudy());
                            mEndOfStudyTV.setText(student.getEndOfStudy().isEmpty()||student.getEndOfStudy() == null?"----":student.getEndOfStudy());
                            mOnGoingCB.setChecked(student.isStudyIsGoing());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("amn catch", e.getMessage() );
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("main", anError.getMessage());
                    }
                });
    }

    public void getCourses(String id) {
        courseList = new ArrayList<Course>();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.GET_COURSES;
        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(0);
                                courseList.add(
                                        new Course(
                                                Integer.parseInt(obj.getString("id")),
                                                obj.getString("name"),
                                                obj.getString("institution"),
                                                obj.getString("start_date"),
                                                obj.getString("end_date")
                                        )
                                );

                            }
                            pDialog.dismiss();
                            CoursesAdapter coursesAdapter = new CoursesAdapter(context, courseList);
                            mCoursesRV.setAdapter(coursesAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void getInterests(String id) {
        interestList = new ArrayList<Interest>();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.GET_INTERESTS;
        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(0);
                                interestList.add(
                                        new Interest(
                                                Integer.parseInt(obj.getString("id")),
                                                obj.getString("interest")
                                        )
                                );
                            }
                            pDialog.dismiss();
                            InterestsAdapter interestsAdapter = new InterestsAdapter(context, interestList);
                            mInterestsRV.setAdapter(interestsAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}