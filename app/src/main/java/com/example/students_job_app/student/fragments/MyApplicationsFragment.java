package com.example.students_job_app.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.students_job_app.R;
import com.example.students_job_app.model.JobApplication;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.model.JobRequest;
import com.example.students_job_app.student.adapters.JobsOpportunitiesAdapter;
import com.example.students_job_app.student.adapters.MyApplicationsAdapter;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;

import java.util.ArrayList;

public class MyApplicationsFragment extends Fragment {

    Context ctx;
    RecyclerView mList;
    MyApplicationsAdapter mAdapter;
    ArrayList<JobApplication> list;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public MyApplicationsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_applications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);

        list = new ArrayList<JobApplication>(){{
            add(new JobApplication(1, "android developer", "technology comapny","jaddah",Constants.JOB_REQUEST_ACCEPTED, "3-10-2022"));
            add(new JobApplication(1, "android developer", "technology comapny","jaddah",Constants.JOB_REQUEST_REJECTED, "3-10-2022"));
            add(new JobApplication(1, "android developer", "technology comapny","jaddah",Constants.JOB_REQUEST_PROCESSING, "3-10-2022"));
            add(new JobApplication(1, "android developer", "technology comapny","jaddah",Constants.JOB_REQUEST_ACCEPTED, "3-10-2022"));
               }};
        mAdapter = new MyApplicationsAdapter(ctx, list);

        mList.setAdapter(mAdapter);
    }

    private void get_applications(){
        //Todo api
        String url = Urls.GET_APPLICATIONS;
        String id = String.valueOf(SharedPrefManager.getInstance(ctx).getUserId());

    }
}