package com.example.students_job_app.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.students_job_app.LoginActivity;
import com.example.students_job_app.R;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCourseFragment extends Fragment {

    Context ctx;
    EditText mInstitutionEt, mCourseNameET, mStartET, mEndET;
    Button mAddCourseBtn;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public AddCourseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCourseNameET = view.findViewById(R.id.course_name);
        mInstitutionEt = view.findViewById(R.id.institution);
        mStartET = view.findViewById(R.id.start_of_study);
        mEndET = view.findViewById(R.id.end_of_study);
        mAddCourseBtn = view.findViewById(R.id.add_course);

        pDialog = new ProgressDialog(ctx);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");
        mAddCourseBtn.setOnClickListener(v->{
            if(Validation.validateInput(ctx, mInstitutionEt, mCourseNameET, mStartET, mEndET)){
                addCourse();
            }
        });
    }

    private void addCourse() {

        String url = Urls.ADD_COURSE;
        String courseName = mCourseNameET.getText().toString().trim();
        String institution = mInstitutionEt.getText().toString().trim();
        String start = mStartET.getText().toString().trim();
        String end = mEndET.getText().toString().trim();
        int id = SharedPrefManager.getInstance(ctx).getUserId();

        mAddCourseBtn.setEnabled(false);
        pDialog.show();

        AndroidNetworking.post(url)
                .addBodyParameter("student_id", String.valueOf(id))
                .addBodyParameter("course_name", courseName)
                .addBodyParameter("institution", institution)
                .addBodyParameter("start_date", start)
                .addBodyParameter("end_date", end)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if(response.equals("true")){
                                Toast.makeText(ctx, ctx.getResources().getString(R.string.course_added), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ctx, ctx.getResources().getString(R.string.course_added_error), Toast.LENGTH_SHORT).show();
                            }
                            mAddCourseBtn.setEnabled(true);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("addCourse catch", e.getMessage() );
                            mAddCourseBtn.setEnabled(true);
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("addCourseError", anError.getMessage());
                        mAddCourseBtn.setEnabled(true);
                        pDialog.dismiss();
                    }
                });
    }
}