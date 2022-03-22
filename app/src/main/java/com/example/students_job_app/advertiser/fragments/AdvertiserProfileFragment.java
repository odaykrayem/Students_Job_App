package com.example.students_job_app.advertiser.fragments;

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
import android.widget.EditText;

import com.example.students_job_app.R;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;

public class AdvertiserProfileFragment extends Fragment {

    EditText mNameTV, mEmailT, mPhoneTV, mWebsiteTV, mLocationTV, mProfFieldTV, mYearsOfIncorporationTV;
    Button mUpdateProfile;

    public NavController navController;

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AdvertiserProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertiser_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameTV = view.findViewById(R.id.company_name);
        mLocationTV = view.findViewById(R.id.location);
        mPhoneTV = view.findViewById(R.id.phone);
        mEmailT = view.findViewById(R.id.email);
        mWebsiteTV = view.findViewById(R.id.website);
        mYearsOfIncorporationTV = view.findViewById(R.id.years_of_corp);
        mProfFieldTV = view.findViewById(R.id.prof_field);
        mUpdateProfile = view.findViewById(R.id.update);

        Advertiser advertiser = SharedPrefManager.getInstance(context).getAdvertiserData();
        mNameTV.setText(advertiser.getCompanyName());
        mLocationTV.setText(advertiser.getLocation());
        mPhoneTV.setText(advertiser.getPhone());
        mEmailT.setText(advertiser.getEmail());
        mWebsiteTV.setText(advertiser.getWebsite());
        mYearsOfIncorporationTV.setText(advertiser.getYears_of_incorporation());
        mProfFieldTV.setText(advertiser.getProfessional_field());

        mUpdateProfile.setOnClickListener(v->{
            navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_COMPANY_NAME, advertiser.getCompanyName());
            bundle.putString(Constants.KEY_PHONE, advertiser.getPhone());
            bundle.putString(Constants.KEY_WEB_SITE, advertiser.getWebsite());
            bundle.putString(Constants.KEY_LOCATION, advertiser.getLocation());
            bundle.putString(Constants.KEY_PROF_FIELD, advertiser.getProfessional_field());
            bundle.putString(Constants.KEY_YEARS_OF_INC, advertiser.getYears_of_incorporation());
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment,bundle);
        });

    }
}