package com.example.students_job_app.student.fragments;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.model.Course;
import com.example.students_job_app.model.Interest;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.student.adapters.CoursesAdapter;
import com.example.students_job_app.student.adapters.InterestsAdapter;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentProfileFragment extends Fragment {

    TextView mUserNameTV, mNameTV, mPhoneTV, mEmailTV, mGender, mBirthDateTV, mPlaceOfStudyTV, mTypeOfStudyTV, mStartOfStudyTV, mEndOfStudyTV;
    CheckBox mOnGoingCB;
    RecyclerView mInterestsRV, mCoursesRV;
    Button mShowCVBtn, mUpdateBtn,mPayBtn;
    public NavController navController;

    ArrayList<Course> courseList;
    ArrayList<Interest> interestList;

    private static String TAG = "student profile";

    Context context;
    ProgressDialog pDialog;
    AlertDialog dialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public StudentProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserNameTV = view.findViewById(R.id.user_name);
        mNameTV = view.findViewById(R.id.name);
        mPhoneTV = view.findViewById(R.id.phone);
        mEmailTV = view.findViewById(R.id.email);
        mGender = view.findViewById(R.id.gender);
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
        mPayBtn = view.findViewById(R.id.pay_bill);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        Student student = SharedPrefManager.getInstance(context).getStudentData();

        navController = Navigation.findNavController(view);

        mUpdateBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_NAME, student.getName());
            bundle.putString(Constants.KEY_USER_NAME, student.getUserName());
            bundle.putString(Constants.KEY_PHONE, student.getPhone());
            bundle.putString(Constants.KEY_STUDY_END, student.getStudyEndDate());
            bundle.putString(Constants.KEY_STUDY_START, student.getStudyStartDate());
            bundle.putString(Constants.KEY_STUDY_PLACE, student.getStudyPlace());
            bundle.putString(Constants.KEY_STUDY_TYPE, student.getStudyType());
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment, bundle);

        });
        mUserNameTV.setText(student.getUserName());
        mNameTV.setText(student.getName());
        mPhoneTV.setText(student.getPhone());
        mEmailTV.setText(student.getEmail());
        mBirthDateTV.setText(student.getBirthDate());
        mPlaceOfStudyTV.setText(student.getStudyPlace());
        mTypeOfStudyTV.setText(student.getStudyType());
        mStartOfStudyTV.setText(student.getStudyStartDate());
        mEndOfStudyTV.setText(student.getStudyEndDate().isEmpty() || student.getStudyEndDate().equals("null") ? "----" : student.getStudyEndDate());
        mGender.setText(student.getGender() == Constants.MALE ? Constants.MALE_TXT : Constants.FEMALE_TXT);
        if(student.isStudyIsGoing()){
            mOnGoingCB.setVisibility(View.VISIBLE);
        }else{
            mOnGoingCB.setVisibility(View.GONE);
        }
        mOnGoingCB.setChecked(student.isStudyIsGoing());

        mShowCVBtn.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_CV_URL, Urls.BASE_URL_FILE+student.getCv());
            bundle.putString(Constants.KEY_FILE_NAME, student.getName());
            navController.navigate(R.id.action_Student_to_ViewCVFragment, bundle);
        });

        if(SharedPrefManager.getInstance(context).hasBill()){
            mPayBtn.setEnabled(true);
        }else {
            mPayBtn.setEnabled(false);
        }
        mPayBtn.setEnabled(false);
        if(SharedPrefManager.getInstance(context).hasBill()){
            mPayBtn.setEnabled(true);
        }
        mPayBtn.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_credit_card, null);
             dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            EditText number = view1.findViewById(R.id.number);
            EditText month = view1.findViewById(R.id.month);
            EditText day = view1.findViewById(R.id.day);
            EditText cvv = view1.findViewById(R.id.cvv);
            Button creditBtn = view1.findViewById(R.id.credit_btn);
            Button cancel = view1.findViewById(R.id.cancel);
            creditBtn.setOnClickListener(l->{
                if(Validation.validateInput(context, number, month, day, cvv)){
                    payBill();
                }
            });

            cancel.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });
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
                .doNotCacheResponse()
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
    }//TODO sj=how cv

    public void getInterests(String id) {
        interestList = new ArrayList<Interest>();
        pDialog.show();
Log.e("id,", id);
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
    private void payBill() {
        pDialog.show();
        String url = Urls.STUDENT_PAY_BILL;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        String billId = String.valueOf(SharedPrefManager.getInstance(context).getBillID());
        Log.e("billid", billId);
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("bill_id", billId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "Saved";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.bill_paid), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.error_pay_bill), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("apply", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("applyError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("student_id")) {
                                Toast.makeText(context, data.getJSONArray("student_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("job_id")) {
                                Toast.makeText(context, data.getJSONArray("job_id").toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }}