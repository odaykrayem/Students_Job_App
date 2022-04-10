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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.student.OnPaymentRequired;
import com.example.students_job_app.R;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.student.OnApplyButtonClickedListener;
import com.example.students_job_app.student.adapters.JobsOpportunitiesAdapter;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobsOpportunitiesFragment extends Fragment implements OnApplyButtonClickedListener, SwipeRefreshLayout.OnRefreshListener{
    Context context;
    RecyclerView mList;
    JobsOpportunitiesAdapter mAdapter;
    ArrayList<JobOpportunity> list;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    OnPaymentRequired onPaymentRequired;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public JobsOpportunitiesFragment() {}

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
            get_jobs();
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
    private void get_jobs(){
        String url = Urls.GET_JOBS;
        list = new ArrayList<JobOpportunity>();
        pDialog.show();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            String message = jsonObject.getString("message");
                            String successMessage = "Found";
                            if(message.toLowerCase().contains(successMessage.toLowerCase())){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    JSONObject advertiser_data = obj.getJSONObject("advertiser");
                                list.add(
                                        new JobOpportunity(
                                                Integer.parseInt(obj.getString("id")),
                                                obj.getString("title"),
                                                obj.getString("company_name"),
                                                advertiser_data.getString("name"),
                                                obj.getString("job_location"),
                                                obj.getString("position"),
                                                obj.getString("required_skills"),
                                                obj.getString("details"),
                                                obj.getString("created_at").substring(0,10)
                                        )
                                );
                            }
                            mAdapter = new JobsOpportunitiesAdapter(context, list, JobsOpportunitiesFragment.this::onApplyButtonClicked);
                            mList.setAdapter(mAdapter);

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
    public void onApplyButtonClicked(String id) {
//        if(SharedPrefManager.getInstance(context).hasBill()){
//            int amount = SharedPrefManager.getInstance(context).getBillAmount();
//            Toast.makeText(context, context.getResources().getString(R.string.you_have_to_pay)
//                    , Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, context.getResources().getString(R.string.your_Paymnet) + amount, Toast.LENGTH_SHORT).show();
//        }else{
//            applyForJob(id);
//        }
        hasBill(id);
    }


    private void applyForJob(String jobId) {
        pDialog.show();
        String url = Urls.APPLY_JOB;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("student_id", userId)
                .addBodyParameter("job_id", jobId)
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
                                Toast.makeText(context, context.getResources().getString(R.string.apply_job), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.post_job_error), Toast.LENGTH_SHORT).show();
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
    }

    public void hasBill(String jobId){
            pDialog.show();

            String url = Urls.STUDENT_HAS_BILL;

            String studentId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

            AndroidNetworking.get(url)
                    .addQueryParameter("user_id", studentId)
                    .addQueryParameter("student_id", studentId)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                pDialog.dismiss();
                                JSONObject jsonObject = response;
                                String message = jsonObject.getString("message");
                                String successMessage = "Saved";
                                if(message.toLowerCase().contains(successMessage.toLowerCase())){

                                    JSONObject data = response.getJSONObject("data");

                                        if(Integer.parseInt(data.getString("is_paid"))==0){
                                            SharedPrefManager.getInstance(context).setHasBill(
                                                    Integer.parseInt(data.getString("id")),
                                                    true,
                                                    Integer.parseInt(data.getString("amount"))
                                            );
                                            Log.e(
                                                    "bill",
                                                    SharedPrefManager.getInstance(context).getBillID()+ "  "+
                                                            SharedPrefManager.getInstance(context).getBillAmount()+"  "+
                                                            SharedPrefManager.getInstance(context).hasBill()
                                            );
                                            Toast.makeText(context, context.getResources().getString(R.string.you_have_bill), Toast.LENGTH_SHORT).show();
                                            Toast.makeText(context, context.getResources().getString(R.string.bill_amount) +
                                                    SharedPrefManager.getInstance(context).getBillAmount()
                                                    , Toast.LENGTH_SHORT).show();
                                        }else{
                                            SharedPrefManager.getInstance(context).setHasBill(-1, false, -1);
                                            applyForJob(jobId);

                                        }
//                                    }

                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                }
                                pDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                pDialog.dismiss();
                                Log.e("jobs catch", e.getMessage());
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            pDialog.dismiss();
                            Log.e("jobs anerror",error.getErrorBody());
                        }
                    });
    }
    @Override
    public void onRefresh() {
        get_jobs();
    }
}
