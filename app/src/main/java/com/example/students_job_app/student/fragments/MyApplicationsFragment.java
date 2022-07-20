package com.example.students_job_app.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.model.JobApplication;
import com.example.students_job_app.student.adapters.JobsOpportunitiesAdapter;
import com.example.students_job_app.student.adapters.MyApplicationsAdapter;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyApplicationsFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    Context context;
    RecyclerView mList;
    MyApplicationsAdapter mAdapter;
    ArrayList<JobApplication> list;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public MyApplicationsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_applications, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            get_applications();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

    }

    private void get_applications(){
        String url = Urls.GET_APPLICATIONS;
        list = new ArrayList<>();
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        pDialog.show();
        Log.e("id", userId);
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("student_id", userId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String message = jsonObject.getString("message");
                            String successMessage = "Found";
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.data_loaded), Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    if(!obj.isNull("job"))
                                    {
                                        JSONObject job_data = obj.getJSONObject("job");
                                        list.add(
                                                new JobApplication(
                                                        Integer.parseInt(obj.getString("id")),
                                                        job_data.getString("title"),
                                                        job_data.getString("company_name"),
                                                        job_data.getString("job_location"),
                                                        Integer.parseInt(obj.getString("status")),
                                                        obj.getString("created_at").substring(0, 10)
                                                )
                                        );
                                    }

                                    Log.e("stats", obj.getString("status")+"");
                                }
                                mAdapter = new MyApplicationsAdapter(context, list);
                                mList.setAdapter(mAdapter);
                            }else {
                                Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                            pDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                            Log.e("jobsapplCatch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.e("jobsapplAnError", error.getErrorBody());
                    }
                });
    }

    @Override
    public void onRefresh() {
        get_applications();
    }
}