package com.example.students_job_app.advertiser.fragments;

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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.advertiser.adapters.JobRequestsAdapter;
import com.example.students_job_app.model.JobApplication;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.model.JobRequest;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.student.adapters.JobsOpportunitiesAdapter;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JobRequestsFragment extends Fragment implements JobRequestsAdapter.OnRequestButtonClicked,  SwipeRefreshLayout.OnRefreshListener {

    Context context;
    RecyclerView mList;
    ArrayList<JobRequest> list;
    ProgressDialog pDialog;
    JobRequestsAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public JobRequestsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_requests, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getRequests();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        mList = view.findViewById(R.id.rv);
    }

    private void getRequests() {
        String url = Urls.GET_REQUESTS;
        list = new ArrayList<JobRequest>();
        pDialog.show();
        String advertiserId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        Log.e("id", advertiserId+"");
        AndroidNetworking.get(url)
                .addQueryParameter("advertiser_id", advertiserId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            String message = jsonObject.getString("message");
                            String successMessage = "User Saved";
                            if(message.toLowerCase().contains(successMessage.toLowerCase())){
                                Toast.makeText(context, context.getResources().getString(R.string.get_requests_success), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    JSONObject student_data = obj.getJSONObject("student");
                                    JSONObject job_data = obj.getJSONObject("job");
                                    Student student = new Student(
                                            Integer.parseInt(student_data.getString("id")),
                                            student_data.getString("user_name"),
                                            student_data.getString("nick_name"),
                                            student_data.getString("phone"),
                                            student_data.getString("email"),
                                            student_data.getString("birth_date").substring(0,10),
                                            Integer.parseInt(student_data.getString("gender")),
                                            student_data.getString("study_type"),
                                            student_data.getString("study_place"),
                                            student_data.getString("study_start_date").substring(0,10),
                                            student_data.getString("study_end_date").substring(0,10),
                                            student_data.getString("study_end_date").equals("null"),
                                            student_data.getString("cv_url")                                    );
                                    //TODO
                                    list.add(
                                            new JobRequest(
                                                    Integer.parseInt(obj.getString("id")),
                                                    job_data.getString("title"),
                                                    student_data.getString("user_name"),
                                                    obj.getString("created_at"),
                                                    Integer.parseInt(obj.getString("status")),
                                                    student
                                            )
                                    );
                                    if(obj.get("student").equals("null")){

                                    }
                                }

                                mAdapter = new JobRequestsAdapter(context, list, JobRequestsFragment.this);
                                mList.setAdapter(mAdapter);
                                pDialog.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }else{
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            Log.e("jobs catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.e("jobs anerror",error.getErrorBody());
                    }
                });
    }

    @Override
    public void onAcceptSelected(String requestId) {
        change_status(requestId, Constants.JOB_REQUEST_ACCEPTED);
    }

    @Override
    public void onRejectSelected(String requestId) {
        change_status(requestId, Constants.JOB_REQUEST_REJECTED);

    }

    private void change_status(String request_id, int status){

        pDialog.show();

        String url = Urls.CHANGE_REQUEST_STATUS;
        String advertiser_id = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
//                .addBodyParameter("advertiser_id", advertiser_id)
                .addBodyParameter("job_request_id", request_id)
                .addBodyParameter("status", String.valueOf(status))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "Saved";///TODO
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                if(status == Constants.JOB_REQUEST_ACCEPTED){
                                    Toast.makeText(context, context.getResources().getString(R.string.job_accept), Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.job_rejected), Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.post_job_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("status catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("statusError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("advertiser_id")) {
                                Toast.makeText(context, data.getJSONArray("advertiser_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("job_request_id")) {
                                Toast.makeText(context, data.getJSONArray("job_request_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("status")) {
                                Toast.makeText(context, data.getJSONArray("status").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    @Override
    public void onRefresh() {
        getRequests();
    }
}