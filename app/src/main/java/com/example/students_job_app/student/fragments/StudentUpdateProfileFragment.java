package com.example.students_job_app.student.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.students_job_app.R;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;

public class StudentUpdateProfileFragment extends Fragment {
    EditText mPhoneET, mPlaceOfStudyET, mTypeOfStudyET, mStudyEndDateET;
    String  phone, placeOfStudy,typeOfStudy, studyEndDate , cvFile;
    CheckBox mOnGoingCB;
    Button addCourseBtn, addInterestBtn, updateBtn;
    NavController navController;

    Boolean onGoing;
    Context ctx;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public StudentUpdateProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addCourseBtn = view.findViewById(R.id.add_courses);
        addInterestBtn = view.findViewById(R.id.add_interests);
        updateBtn = view.findViewById(R.id.update);
        mPhoneET = view.findViewById(R.id.phone);
        mPlaceOfStudyET = view.findViewById(R.id.place_of_study);
        mTypeOfStudyET = view.findViewById(R.id.type_of_study);
        mOnGoingCB = view.findViewById(R.id.ongoing);
        mStudyEndDateET = view.findViewById(R.id.end_of_study);

        addCourseBtn.setOnClickListener(v -> {
            navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_updateProfileFragment_to_addCourseFragment);
        });

        addInterestBtn.setOnClickListener(v -> {
            navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_updateProfileFragment_to_chooseInterestsFragment);
        });
        updateBtn.setOnClickListener(v->{
            update();
        });
    }

    private void update() {
        String url = Urls.UPDATE_STUDENT;
        String id = String.valueOf(SharedPrefManager.getInstance(ctx).getUserId());
        phone = mPhoneET.getText().toString().trim();
        placeOfStudy = mPlaceOfStudyET.getText().toString().trim();
        typeOfStudy = mTypeOfStudyET.getText().toString().trim();
        studyEndDate = mStudyEndDateET.getText().toString().trim();
//        onGoing

    }

}