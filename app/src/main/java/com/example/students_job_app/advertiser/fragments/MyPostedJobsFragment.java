package com.example.students_job_app.advertiser.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.example.students_job_app.advertiser.adapters.JobRequestsAdapter;
import com.example.students_job_app.advertiser.adapters.MyPostedJobsAdapter;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.model.JobRequest;
import com.example.students_job_app.student.adapters.JobsOpportunitiesAdapter;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPostedJobsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Context context;
    RecyclerView mList;
    ArrayList<JobOpportunity> list;
    MyPostedJobsAdapter mAdapter;

    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    FloatingActionButton floatingActionButton;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public MyPostedJobsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_posted_jobs, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getMyPostedJobs();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        floatingActionButton = view.findViewById(R.id.add_btn);
        navController = Navigation.findNavController(view);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing Please wait...");
//        hasBill();

        floatingActionButton.setOnClickListener(v->{
            hasBill();
        });
    }

    private void getMyPostedJobs(){
        pDialog.show();

        String url = Urls.GET_POSTED_JOBS;
        list = new ArrayList<JobOpportunity>();
        String advertiserId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
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
                                Toast.makeText(context, context.getResources().getString(R.string.get_posted_job_success), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    list.add(
                                            new JobOpportunity(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("title"),
                                                    obj.getString("company_name"),
                                                    obj.getString("advertiser_id"),
                                                    obj.getString("job_location"),
                                                    obj.getString("position"),
                                                    obj.getString("required_skills"),
                                                    obj.getString("details"),
                                                    obj.getString("created_at")
                                            )
                                    );
                                }
                                mAdapter = new MyPostedJobsAdapter(context, list);
                                mList.setAdapter(mAdapter);
                                pDialog.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
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

    public void hasBill(){
        pDialog.show();

        String url = Urls.ADVERTISER_HAS_BILL;

        String advertiserId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.get(url)
                .addQueryParameter("advertiser_id", advertiserId)
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

                                if(!response.isNull("data")){
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
                                        navController.navigate(R.id.action_jobs_To_AddJobFragment);
                                    }
                                }else{
                                    SharedPrefManager.getInstance(context).setHasBill(-1, false, -1);
                                    navController.navigate(R.id.action_jobs_To_AddJobFragment);
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
        getMyPostedJobs();
    }
}