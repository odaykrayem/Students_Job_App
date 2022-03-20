package com.example.students_job_app.student.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.students_job_app.R;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.utils.Constants;

public class StudentProfileFragment extends Fragment {

    TextView mUserNameTV, mNameTV, mPhoneTV, mEmailTV, mBirthDateTV, mPlaceOfStudyTV, mTypeOfStudyTV, mStartOfStudyTV, mEndOfStudyTV;
    CheckBox mOnGoingCB;
    RecyclerView mInterestsRV, mCoursesRV;
    Button mShowCVBtn, mUpdateBtn;


    Context ctx;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_profile, container, false);
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
        mUpdateBtn = view.findViewById(R.id.update);
        mShowCVBtn = view.findViewById(R.id.show_cv);

        Student student = new Student(
                1,
                 "userName",
                  "User",
                   "09669834573",
                    "user@eamil",
                "1-1-1999",
                  Constants.MALE,
                "Computer Science",
                "University",
                "1-1-2019",
                 null,
                  true,
                   "url"
                );
        mUserNameTV.setText(student.getUserName());
        mNameTV.setText(student.getName());
        mPhoneTV.setText(student.getPhone());
        mEmailTV.setText(student.getEmail());
        mBirthDateTV.setText(student.getBirthDate());
        mPlaceOfStudyTV.setText(student.getPlaceOfStudy());
        mTypeOfStudyTV.setText(student.getTypeOfStudy());
        mStartOfStudyTV.setText(student.getStartOfStudy());
        mEndOfStudyTV.setText(student.getEndOfStudy());
        mOnGoingCB.setChecked(student.isStudyIsGoing());
//        mCoursesRV

    }
}