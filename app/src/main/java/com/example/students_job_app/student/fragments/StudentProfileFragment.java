package com.example.students_job_app.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
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
import com.example.students_job_app.R;
import com.example.students_job_app.model.Course;
import com.example.students_job_app.model.Interest;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.student.adapters.CoursesAdapter;
import com.example.students_job_app.student.adapters.InterestsAdapter;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentProfileFragment extends Fragment {

    TextView mUserNameTV, mNameTV, mPhoneTV, mEmailTV,mGender, mBirthDateTV, mPlaceOfStudyTV, mTypeOfStudyTV, mStartOfStudyTV, mEndOfStudyTV;
    CheckBox mOnGoingCB;
    RecyclerView mInterestsRV, mCoursesRV;
    Button mShowCVBtn, mUpdateBtn;
    public NavController navController;

    ArrayList<Course> courseList;
    ArrayList<Interest> interestList;

    private static String TAG = "student profile";

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public StudentProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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


        Student student = SharedPrefManager.getInstance(context).getStudentData();


        mUpdateBtn.setOnClickListener(v->{
            navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_NAME, student.getName());
            bundle.putString(Constants.KEY_USER_NAME, student.getUserName());
            bundle.putString(Constants.KEY_PHONE, student.getPhone());
            bundle.putString(Constants.KEY_STUDY_END, student.getEndOfStudy());
            bundle.putString(Constants.KEY_STUDY_PLACE, student.getPlaceOfStudy());
            bundle.putString(Constants.KEY_STUDY_TYPE, student.getTypeOfStudy());
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment,bundle);

        });
//        Student student = new Student(
//                1,
//                 "userName",
//                  "User",
//                   "09669834573",
//                    "user@eamil",
//                "1-1-1999",
//                  Constants.MALE,
//                "Computer Science",
//                "University",
//                "1-1-2019",
//                 null,
//                  true,
//                   "url"
//                );
        mUserNameTV.setText(student.getUserName());
        mNameTV.setText(student.getName());
        mPhoneTV.setText(student.getPhone());
        mEmailTV.setText(student.getEmail());
        mBirthDateTV.setText(student.getBirthDate());
        mPlaceOfStudyTV.setText(student.getPlaceOfStudy());
        mTypeOfStudyTV.setText(student.getTypeOfStudy());
        mStartOfStudyTV.setText(student.getStartOfStudy());
        mEndOfStudyTV.setText(student.getEndOfStudy().isEmpty()||student.getEndOfStudy() == null?"----":student.getEndOfStudy());
        mGender.setText(student.getGender()==Constants.MALE?Constants.MALE_TXT:Constants.FEMALE_TXT);
        mOnGoingCB.setChecked(student.isStudyIsGoing());
//        mCoursesRV

        getCourses(String.valueOf(student.getId()));
        getInterests(String.valueOf(student.getId()));
    }

    public void getCourses(String id) {
        courseList = new ArrayList<Course>();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.GET_COURSES;
        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(0);
                                courseList.add(
                                        new Course(
                                                Integer.parseInt(obj.getString("id")),
                                                obj.getString("name"),
                                                obj.getString("institution"),
                                                obj.getString("start_date"),
                                                obj.getString("end_date")
                                        )
                                );

                            }
                            pDialog.dismiss();
                            CoursesAdapter coursesAdapter = new CoursesAdapter(context, courseList);
                            mCoursesRV.setAdapter(coursesAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void getInterests(String id) {
        interestList = new ArrayList<Interest>();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.GET_INTERESTS;
        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(0);
                                interestList.add(
                                        new Interest(
                                                Integer.parseInt(obj.getString("id")),
                                                obj.getString("interest")
                                        )
                                );
                            }
                            pDialog.dismiss();
                            InterestsAdapter interestsAdapter = new InterestsAdapter(context, interestList);
                            mInterestsRV.setAdapter(interestsAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}