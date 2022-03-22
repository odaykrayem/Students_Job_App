package com.example.students_job_app.advertiser.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.students_job_app.model.JobRequest;
import com.example.students_job_app.student.adapters.JobsOpportunitiesAdapter;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JobRequestsFragment extends Fragment implements JobRequestsAdapter.OnRequestButtonClicked {

    Context ctx;
    RecyclerView mList;
    ArrayList<JobRequest> list;
    ProgressDialog pDialog;
    JobRequestsAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public JobRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();
        mList = view.findViewById(R.id.rv);
        getRequests();
    }

    private void getRequests() {
        String url = Urls.GET_REQUESTS;
        list = new ArrayList<JobRequest>();
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
                                        new JobRequest(
                                                Integer.parseInt(obj.getString("id")),
                                                obj.getString("name"),
                                                obj.getString("studentName"),
                                                obj.getString("date"),
                                                obj.getInt("status")

                                        )
                                );
                            }
                            mAdapter = new JobRequestsAdapter(ctx, list);
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
    public void onAcceptSelected(String id) {
        String url = Urls.ACCEPT_REQUEST;
        pDialog.show();
        int advertiser_id = SharedPrefManager.getInstance(ctx).getUserId();
        AndroidNetworking.post(url)
                .addBodyParameter("advertiser_id", String.valueOf(advertiser_id))
                .addBodyParameter("request_id", id)
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

    @Override
    public void onRejectSelected(String id) {
        String url = Urls.REJECT_REQUEST;
        pDialog.show();
        int advertiser_id = SharedPrefManager.getInstance(ctx).getUserId();
        AndroidNetworking.post(url)
                .addBodyParameter("advertiser_id", String.valueOf(advertiser_id))
                .addBodyParameter("request_id", id)
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