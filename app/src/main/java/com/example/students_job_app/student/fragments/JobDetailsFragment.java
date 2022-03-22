package com.example.students_job_app.student.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.students_job_app.R;
import com.example.students_job_app.student.OnButtonClickedListener;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;

public class JobDetailsFragment extends Fragment {


    Context ctx;
    TextView mPosition, mCompany, mAdvertiser, mDetails, mJobLocation, mRequiredSkills, mDate;
    String id, position, company, advertiser, details, location, requiredSkills, date;
    Button mApplyBtn;
    public NavController navController;

    OnButtonClickedListener onButtonClicked;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public JobDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            id = getArguments().getString("job_id");
            position = getArguments().getString("position");
            company = getArguments().getString("company");
            advertiser = getArguments().getString("advertiser");
            details = getArguments().getString("details");
            location = getArguments().getString("location");
            requiredSkills = getArguments().getString("required_skills");
            date = getArguments().getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPosition = view.findViewById(R.id.position);
        mCompany = view.findViewById(R.id.company);
        mAdvertiser = view.findViewById(R.id.advertiser);
        mDetails = view.findViewById(R.id.details);
        mRequiredSkills = view.findViewById(R.id.required_skills);
        mJobLocation = view.findViewById(R.id.job_location);
        mDate = view.findViewById(R.id.date);
        mApplyBtn = view.findViewById(R.id.apply);
        mPosition.setText(position);
        mCompany.setText(company);
        mAdvertiser.setText(advertiser);
        mDetails.setText(details);
        mRequiredSkills.setText(requiredSkills);
        mJobLocation.setText(location);
        mDate.setText(date);

        mApplyBtn.setOnClickListener(v->{

            LayoutInflater factory = LayoutInflater.from(ctx);
            final View view1 = factory.inflate(R.layout.dialog_confirm_application, null);
            final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            TextView yes = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            yes.setOnClickListener(l->{
                //Interface is in JobsOpportunities Fragment
                onButtonClicked.onButtonClicked(String.valueOf(id));
                dialog.dismiss();
                navController.popBackStack();
            });

            no.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });
    }

}