package com.example.students_job_app.student.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.students_job_app.R;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.student.OnButtonClickedListener;

import java.util.ArrayList;
import java.util.List;

public class JobsOpportunitiesAdapter extends RecyclerView.Adapter<JobsOpportunitiesAdapter.ViewHolder> {

    Context context;
    private List<JobOpportunity> list;
    public NavController navController;

    OnButtonClickedListener onButtonClicked;

    // RecyclerView recyclerView;
    public JobsOpportunitiesAdapter(Context context, ArrayList<JobOpportunity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_job, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JobOpportunity item = list.get(position);

        holder.jobName.setText(item.getName());
        holder.company.setText(item.getCompany());
        holder.jobLocation.setText(item.getLocation());

        holder.apply.setOnClickListener(v->{
            onButtonClicked.onButtonClicked(String.valueOf(item.getId()));
//            applyForJob(String.valueOf(item.getId()));
        });
        holder.itemView.setOnClickListener(v->{
            navController = Navigation.findNavController(holder.itemView);
            Bundle bundle = new Bundle();
            bundle.putString("job_id", String.valueOf(item.getId()));
            bundle.putString("position", item.getPosition());
            bundle.putString("company", item.getCompany());
            bundle.putString("details", item.getDetails());
            bundle.putString("required_skills", item.getRequired_skills());
            bundle.putString("location", item.getLocation());
            navController.navigate(R.id.action_jobsFragment_to_jobDetailsFragment,bundle);
        });

    }




    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobName, company, jobLocation;
        public Button apply;

        public ViewHolder(View itemView) {
            super(itemView);
            this.jobName = itemView.findViewById(R.id.job_name);
            this.company = itemView.findViewById(R.id.company);
            this.jobLocation = itemView.findViewById(R.id.job_location);
            this.apply = itemView.findViewById(R.id.apply);
        }
    }


}
