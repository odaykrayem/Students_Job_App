package com.example.students_job_app.student.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.students_job_app.R;

public class StudentUpdateProfileFragment extends Fragment {

    Button addCourseBtn, addInterestBtn;


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

        bindViews(view);

        addCourseBtn.setOnClickListener(v -> {
            //todo go to add courses fragment
        });

        addInterestBtn.setOnClickListener(v -> {
            // TODO: go to add interests fragment
        });
    }

    private void bindViews(View view) {
        addCourseBtn = view.findViewById(R.id.add_courses);
        addInterestBtn = view.findViewById(R.id.add_interests);
    }
}