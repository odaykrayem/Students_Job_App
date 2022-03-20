package com.example.students_job_app.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.students_job_app.R;
import com.example.students_job_app.model.JobApplication;
import com.example.students_job_app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MyApplicationsAdapter  extends RecyclerView.Adapter<MyApplicationsAdapter.ViewHolder> {

    Context context;
    private List<JobApplication> list;
    public NavController navController;


    // RecyclerView recyclerView;
    public MyApplicationsAdapter(Context context, ArrayList<JobApplication> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyApplicationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_job, parent, false);
        MyApplicationsAdapter.ViewHolder viewHolder = new MyApplicationsAdapter.ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyApplicationsAdapter.ViewHolder holder, int position) {

        JobApplication item = list.get(position);

        holder.jobName.setText(item.getName());
        holder.company.setText(item.getCompany());
        holder.jobLocation.setText(item.getLocation());

        if(item.getStatus() == Constants.JOB_REQUEST_PROCESSING){
            holder.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_status_processing));

        }else if(item.getStatus() == Constants.JOB_REQUEST_ACCEPTED){
            holder.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_status_accepted));

        }else if(item.getStatus() == Constants.JOB_REQUEST_REJECTED){
            holder.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_status_rejected));
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobName, company, jobLocation, status;

        public ViewHolder(View itemView) {
            super(itemView);
            this.jobName = itemView.findViewById(R.id.job_name);
            this.company = itemView.findViewById(R.id.company);
            this.jobLocation = itemView.findViewById(R.id.job_location);
            this.status = itemView.findViewById(R.id.status);
        }
    }


}