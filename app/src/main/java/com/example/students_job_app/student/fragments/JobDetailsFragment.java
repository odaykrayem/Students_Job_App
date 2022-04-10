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
import com.example.students_job_app.student.OnApplyButtonClickedListener;

public class JobDetailsFragment extends Fragment {

    Context context;
    TextView mPosition, mCompany, mAdvertiser, mDetails, mJobLocation, mRequiredSkills, mDate;
    String id, position, company, advertiserName, details, location, requiredSkills, date;
    Button mApplyBtn;
    public NavController navController;

    OnApplyButtonClickedListener onButtonClicked;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public JobDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            id = getArguments().getString("job_id");
            position = getArguments().getString("position");
            company = getArguments().getString("company");
            advertiserName = getArguments().getString("advertiser_name");
            details = getArguments().getString("details");
            location = getArguments().getString("location");
            requiredSkills = getArguments().getString("required_skills");
            date = getArguments().getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPosition = view.findViewById(R.id.position);
        mCompany = view.findViewById(R.id.company_name);
        mAdvertiser = view.findViewById(R.id.advertiser);
        mDetails = view.findViewById(R.id.details);
        mRequiredSkills = view.findViewById(R.id.required_skills);
        mJobLocation = view.findViewById(R.id.job_location);
        mDate = view.findViewById(R.id.date);
        mApplyBtn = view.findViewById(R.id.apply);
        mPosition.setText(position);
        mCompany.setText(company);
        mAdvertiser.setText(advertiserName);
        mDetails.setText(details);
        mRequiredSkills.setText(requiredSkills);
        mJobLocation.setText(location);
        mDate.setText(date);

        mApplyBtn.setOnClickListener(v->{

            mApplyBtn.setEnabled(false);
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_confirm_application, null);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            TextView yes = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            yes.setOnClickListener(l->{
                //Interface is in JobsOpportunities Fragment
                onButtonClicked.onApplyButtonClicked(id);
                dialog.dismiss();
                mApplyBtn.setEnabled(true);
                navController.popBackStack();
            });

            no.setOnClickListener(l->{
                dialog.dismiss();
                mApplyBtn.setEnabled(true);
            });
            dialog.show();
        });
    }

}