package com.example.students_job_app.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

public class StudentUpdateProfileFragment extends Fragment {
    EditText mNameET, mUserNameET, mPhoneET, mPlaceOfStudyET, mTypeOfStudyET, mStudyEndDateET;
    String  name, userName,phone, placeOfStudy,typeOfStudy, studyEndDate ;
    CheckBox mOnGoingCB;
    Button addCourseBtn, addInterestBtn, updateBtn;
    NavController navController;

    ProgressDialog pDialog;

    Boolean onGoing;
    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public StudentUpdateProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            name = getArguments().getString(Constants.KEY_NAME);
            userName = getArguments().getString(Constants.KEY_USER_NAME);
            phone = getArguments().getString(Constants.KEY_PHONE);
            studyEndDate = getArguments().getString(Constants.KEY_STUDY_END);
            typeOfStudy = getArguments().getString(Constants.KEY_STUDY_TYPE);
            placeOfStudy = getArguments().getString(Constants.KEY_STUDY_PLACE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");
        addCourseBtn = view.findViewById(R.id.add_courses);
        addInterestBtn = view.findViewById(R.id.add_interests);
        updateBtn = view.findViewById(R.id.update);
        mNameET = view.findViewById(R.id.name);
        mUserNameET = view.findViewById(R.id.user_name);
        mPhoneET = view.findViewById(R.id.phone);
        mPlaceOfStudyET = view.findViewById(R.id.place_of_study);
        mTypeOfStudyET = view.findViewById(R.id.type_of_study);
        mOnGoingCB = view.findViewById(R.id.ongoing);
        mOnGoingCB.setEnabled(false);
        mStudyEndDateET = view.findViewById(R.id.end_of_study);
        mNameET.setText(name);
        mUserNameET.setText(userName);
        mPhoneET.setText(phone);
        mPlaceOfStudyET.setText(placeOfStudy);
        mTypeOfStudyET.setText(typeOfStudy);

        if(studyEndDate == null || studyEndDate.isEmpty()){
            mOnGoingCB.setChecked(true);
            mStudyEndDateET.setText("-------");
        }else{
            mOnGoingCB.setChecked(false);
            mStudyEndDateET.setText(studyEndDate);

        }
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

    //TODO SHOW CV
    private void update() {
        String url = Urls.UPDATE_STUDENT;
        String id = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        Validation.setEnabled(context,false,  mNameET, mUserNameET, mPhoneET, mPlaceOfStudyET, mTypeOfStudyET, mStudyEndDateET);
        updateBtn.setEnabled(false);
        name = mNameET.getText().toString().trim();
        userName = mUserNameET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        placeOfStudy = mPlaceOfStudyET.getText().toString().trim();
        typeOfStudy = mTypeOfStudyET.getText().toString().trim();
        studyEndDate = mStudyEndDateET.getText().toString().trim();

        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .addBodyParameter("user_name", userName)
                .addBodyParameter("name", name)
                .addBodyParameter("phone", phone)
                .addBodyParameter("study_type", typeOfStudy)
                .addBodyParameter("study_place", placeOfStudy)
                .addBodyParameter("study_end_date", studyEndDate)
                .addBodyParameter("user_type", "0")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if(response.equals("true"))
                            SharedPrefManager.getInstance(context).studentUpdate(
                                    name,
                                    userName,
                                    phone,
                                    placeOfStudy,
                                    typeOfStudy,
                                    studyEndDate,
                                    (studyEndDate==null || studyEndDate.isEmpty())?true:false
                            );
                            Validation.setEnabled(context,true,  mNameET, mUserNameET, mPhoneET, mPlaceOfStudyET, mTypeOfStudyET, mStudyEndDateET);
                            updateBtn.setEnabled(true);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("amn catch", e.getMessage() );
                            Validation.setEnabled(context,true,  mNameET, mUserNameET, mPhoneET, mPlaceOfStudyET, mTypeOfStudyET, mStudyEndDateET);
                            updateBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("main", anError.getMessage());
                        Validation.setEnabled(context,true,  mNameET, mUserNameET, mPhoneET, mPlaceOfStudyET, mTypeOfStudyET, mStudyEndDateET);
                        updateBtn.setEnabled(true);

                    }
                });
    }

}