package com.example.students_job_app.advertiser.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONObject;

public class AdvertiserUpdateProfileFragment extends Fragment {

    Context context;
    EditText  mNameET , mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation;
    String name, phone, website, location, profField, yearsOfIncorporation;
    Button updateBtn;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public AdvertiserUpdateProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            name = getArguments().getString(Constants.KEY_COMPANY_NAME);
            phone = getArguments().getString(Constants.KEY_PHONE);
            website = getArguments().getString(Constants.KEY_WEB_SITE);
            location = getArguments().getString(Constants.KEY_LOCATION);
            yearsOfIncorporation = getArguments().getString(Constants.KEY_YEARS_OF_INC);
            profField = getArguments().getString(Constants.KEY_PROF_FIELD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertiser_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameET = view.findViewById(R.id.name);
        mPhoneET= view.findViewById(R.id.phone);
        mWebsiteET= view.findViewById(R.id.website);
        mLocationET= view.findViewById(R.id.location);
        mProfField= view.findViewById(R.id.prof_field);
        mYearsOfIncorporation= view.findViewById(R.id.years_of_corp);
        updateBtn = view.findViewById(R.id.update);
        mNameET.setText(name);
        mPhoneET.setText(phone);
        mWebsiteET.setText(website);
        mLocationET.setText(location);
        mProfField.setText(profField);
        mYearsOfIncorporation.setText(yearsOfIncorporation);

        updateBtn.setOnClickListener(v->{
            updateProfile();
        });
    }

    private void updateProfile() {
        String url = Urls.UPDATE_ADVERTISER;
        String id = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        Validation.setEnabled(context, false,  mNameET , mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );
        updateBtn.setEnabled(false);

        name = mNameET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        website = mWebsiteET.getText().toString().trim();
        location = mLocationET.getText().toString().trim();
        profField = mProfField.getText().toString().trim();
        yearsOfIncorporation = mYearsOfIncorporation.getText().toString().trim();

        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .addBodyParameter("advertiser_name", name)
                .addBodyParameter("phone", phone)
                .addBodyParameter("website", website)
                .addBodyParameter("location", location)
                .addBodyParameter("professional_fields", profField)
                .addBodyParameter("years_of_incorporation", yearsOfIncorporation)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if(response.equals("true"))
                                SharedPrefManager.getInstance(context).advertiserUpdate(
                                        name,
                                        phone,
                                        website,
                                        location,
                                        profField,
                                        yearsOfIncorporation
                                );
                            Validation.setEnabled(context, true,  mNameET , mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );
                            updateBtn.setEnabled(true);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("amn catch", e.getMessage() );
                            Validation.setEnabled(context, true,  mNameET , mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );
                            updateBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("main", anError.getMessage());
                        Validation.setEnabled(context, true,  mNameET , mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );
                        updateBtn.setEnabled(true);
                    }
                });
    }
}