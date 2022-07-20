package com.example.students_job_app.advertiser.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.example.students_job_app.student.adapters.CoursesAdapter;
import com.example.students_job_app.student.adapters.InterestsAdapter;
import com.example.students_job_app.utils.Constants;
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

    Student student;
    int jobId;

    JobRequestsAdapter.OnRequestButtonClicked onRequestButtonClicked;

    Context context;
    ProgressDialog pDialog;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public StudentDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            student = (Student) getArguments().getSerializable(Constants.KEY_STUDENT);
            jobId = getArguments().getInt(Constants.KEY_JOB_ID);
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
        mGender = view.findViewById(R.id.gender);
        mPlaceOfStudyTV = view.findViewById(R.id.place_of_study);
        mTypeOfStudyTV = view.findViewById(R.id.type_of_study);
        mStartOfStudyTV = view.findViewById(R.id.start_of_study);
        mEndOfStudyTV = view.findViewById(R.id.end_of_study);
        mOnGoingCB = view.findViewById(R.id.ongoing);
        mCoursesRV = view.findViewById(R.id.rv_courses);
        mInterestsRV = view.findViewById(R.id.rv_interests);
        mShowCVBtn = view.findViewById(R.id.show_cv);

        navController = Navigation.findNavController(view);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mShowCVBtn.setOnClickListener(v->{
            Log.e("cv", student.getCv());
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_CV_URL, Urls.BASE_URL_FILE+student.getCv());
            bundle.putString(Constants.KEY_FILE_NAME, student.getName());
            navController.navigate(R.id.action_Student_to_ViewCVFragment, bundle);
        });
        mUserNameTV.setText(student.getUserName());
        mNameTV.setText(student.getName());
        mPhoneTV.setText(student.getPhone());
        mEmailTV.setText(student.getEmail());
        mBirthDateTV.setText(student.getBirthDate());
        mPlaceOfStudyTV.setText(student.getStudyPlace());
        mTypeOfStudyTV.setText(student.getStudyType());
        mStartOfStudyTV.setText(student.getStudyStartDate());
        mEndOfStudyTV.setText(student.getStudyEndDate().isEmpty() || student.getStudyEndDate() == null ? "----" : student.getStudyEndDate());
        mGender.setText(student.getGender() == Constants.MALE ? Constants.MALE_TXT : Constants.FEMALE_TXT);
        if(student.isStudyIsGoing()){
            mOnGoingCB.setVisibility(View.VISIBLE);
        }else{
            mOnGoingCB.setVisibility(View.GONE);
        }
        mOnGoingCB.setChecked(student.isStudyIsGoing());

        getCourses(String.valueOf(student.getId()));
        getInterests(String.valueOf(student.getId()));
    }


    public void getCourses(String id) {
        courseList = new ArrayList<Course>();

        pDialog.show();

        String url = Urls.GET_COURSES;
        AndroidNetworking.get(url)
                .addQueryParameter("user_id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String message = jsonObject.getString("message");
                            String successMessage = "Saved";
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    courseList.add(
                                            new Course(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("course_name"),
                                                    obj.getString("institution"),
                                                    obj.getString("start_date").substring(0,10),
                                                    obj.getString("end_date").substring(0,10)
                                            )
                                    );
                                }
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                            CoursesAdapter coursesAdapter = new CoursesAdapter(context, courseList);
                            mCoursesRV.setAdapter(coursesAdapter);
                        } catch (Exception e) {
                            pDialog.dismiss();
                            Log.e("courses catch", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("courses AnError", anError.getErrorBody());
                    }
                });
    }
    public void getInterests(String id) {
        interestList = new ArrayList<Interest>();
        pDialog.show();

        String url = Urls.GET_INTERESTS;
        AndroidNetworking.get(url)
                .addQueryParameter("user_id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String message = jsonObject.getString("message");
                            String successMessage = "Saved";
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    interestList.add(
                                            new Interest(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("interest_name")
                                            )
                                    );
                                }
                                InterestsAdapter interestsAdapter = new InterestsAdapter(context, interestList);
                                mInterestsRV.setAdapter(interestsAdapter);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (Exception e) {
                            pDialog.dismiss();
                            Log.e("interests catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("interests AnError", anError.getErrorBody());
                    }
                });
    }
}