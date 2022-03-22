package com.example.students_job_app.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.model.Interest;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.student.OnButtonClickedListener;
import com.example.students_job_app.student.adapters.InterestsAdapter;
import com.example.students_job_app.student.adapters.JobsOpportunitiesAdapter;
import com.example.students_job_app.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobsOpportunitiesFragment extends Fragment implements OnButtonClickedListener {
    Context ctx;
    RecyclerView mList;
    JobsOpportunitiesAdapter mAdapter;
    ArrayList<JobOpportunity> list;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }
    public JobsOpportunitiesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_applications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);
        pDialog = new ProgressDialog(ctx);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

//        list = new ArrayList<JobOpportunity>(){{
//            add(new JobOpportunity(1, "android developer", "comapny", "jaddah", "junior android developer", "2 years of experience in java", "we are lookin for motivative developer"));
//            add(new JobOpportunity(1, "android developer", "comapny", "jaddah", "junior android developer", "2 years of experience in java", "we are lookin for motivative developer"));
//            add(new JobOpportunity(1, "android developer", "comapny", "jaddah", "junior android developer", "2 years of experience in java", "we are lookin for motivative developer"));
//            add(new JobOpportunity(1, "android developer", "comapny", "jaddah", "junior android developer", "2 years of experience in java", "we are lookin for motivative developer"));
//             }};
       get_jobs();
    }
    private void get_jobs(){
        String url = Urls.GET_JOBS;
        list = new ArrayList<JobOpportunity>();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(0);
                                list.add(
                                        new JobOpportunity(
                                                Integer.parseInt(obj.getString("id")),
                                                obj.getString("name"),
                                                obj.getString("company"),
                                                obj.getString("advertiser"),
                                                obj.getString("location"),
                                                obj.getString("position"),
                                                obj.getString("required_skills"),
                                                obj.getString("details"),
                                                obj.getString("date")
                                        )
                                );
                            }
                            mAdapter = new JobsOpportunitiesAdapter(ctx, list);
                            mList.setAdapter(mAdapter);
                            pDialog.dismiss();
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

    @Override
    public void onButtonClicked(String id) {
        applyForJob(id);
    }


    private void applyForJob(String id) {
        pDialog.show();
        String url = Urls.APPLY_JOB;
        AndroidNetworking.post(url)
                .addBodyParameter("student_id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int result = response.getInt("result");
                            if(result == 1){
                                Toast.makeText(ctx, ctx.getResources().getString(R.string.course_added), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ctx, ctx.getResources().getString(R.string.course_added_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("amn catch", e.getMessage() );
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("main", anError.getMessage());
                        pDialog.dismiss();
                    }
                });
    }
}
