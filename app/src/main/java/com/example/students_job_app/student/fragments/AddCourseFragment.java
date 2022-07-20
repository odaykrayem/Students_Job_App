package com.example.students_job_app.student.fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AddCourseFragment extends Fragment {

    Context context;
    EditText mInstitutionEt, mCourseNameET, mStartET, mEndET;
    Button mAddCourseBtn;
    ProgressDialog pDialog;
    final Calendar myCalendar = Calendar.getInstance();

    NavController navController;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        navController = Navigation.findNavController(view);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");
        mAddCourseBtn.setOnClickListener(v->{
            if(Validation.validateInput(context, mInstitutionEt, mCourseNameET, mStartET, mEndET)){
                addCourse();
            }
        });

        mStartET.setOnClickListener(v -> {
            final DatePickerDialog picker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = "";
                    if(String.valueOf(monthOfYear).length()<2){
                        month = "0" + monthOfYear;
                    }else{
                        month = "" + monthOfYear;
                    }
                    String day = "";
                    if(String.valueOf(dayOfMonth).length()<2){
                        day = "0"+ dayOfMonth;
                    }else{
                        day = ""+dayOfMonth;
                    }
                    mStartET.setText(""+year+"-"+month+"-"+day);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        mEndET.setOnClickListener(v -> {
            final DatePickerDialog picker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = "";
                    if(String.valueOf(monthOfYear).length()<2){
                        month = "0" + monthOfYear;
                    }else{
                        month = "" + monthOfYear;
                    }
                    String day = "";
                    if(String.valueOf(dayOfMonth).length()<2){
                        day = "0"+ dayOfMonth;
                    }else{
                        day = ""+dayOfMonth;
                    }
                    mEndET.setText(""+year+"-"+month+"-"+day);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });
    }

    private void addCourse() {
        String url = Urls.ADD_COURSE;
        String courseName = mCourseNameET.getText().toString().trim();
        String institution = mInstitutionEt.getText().toString().trim();
        String start = mStartET.getText().toString().trim();
        String end = mEndET.getText().toString().trim();
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        mAddCourseBtn.setEnabled(false);
        pDialog.show();

        AndroidNetworking.post(url)
                .addBodyParameter("student_id", userId)
                .addBodyParameter("course_name", courseName)
                .addBodyParameter("institution", institution)
                .addBodyParameter("start_date", start)
                .addBodyParameter("end_date", end)
                .doNotCacheResponse()
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
                                Toast.makeText(context, context.getResources().getString(R.string.add_courses_success), Toast.LENGTH_SHORT).show();
                                navController.popBackStack();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.post_job_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("addCourse", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("addCourseError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("student_id")) {
                                Toast.makeText(context, data.getJSONArray("student_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("course_name")) {
                                Toast.makeText(context, data.getJSONArray("course_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("institution")) {
                                Toast.makeText(context, data.getJSONArray("institution").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("start_date")) {
                                Toast.makeText(context, data.getJSONArray("start_date").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("end_date")) {
                                Toast.makeText(context, data.getJSONArray("end_date").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}