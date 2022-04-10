package com.example.students_job_app.student.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.students_job_app.R;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.student.OnApplyButtonClickedListener;
import java.util.ArrayList;
import java.util.List;

public class JobsOpportunitiesAdapter extends RecyclerView.Adapter<JobsOpportunitiesAdapter.ViewHolder> {

    Context context;
    private List<JobOpportunity> list;
    public NavController navController;

    OnApplyButtonClickedListener onButtonClicked;

    public JobsOpportunitiesAdapter(Context context, ArrayList<JobOpportunity> list, OnApplyButtonClickedListener onButtonClicked) {
        this.context = context;
        this.list = list;
        this.onButtonClicked = onButtonClicked;
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

        holder.jobName.setText(item.getTitle());
        holder.company.setText(item.getCompany());
        holder.jobLocation.setText(item.getJobLocation());

        holder.apply.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_confirm_application, null);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            TextView yes = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            yes.setOnClickListener(l->{
                //Interface is in JobsOpportunities Fragment
                onButtonClicked.onApplyButtonClicked(String.valueOf(item.getId()));
                dialog.dismiss();
            });

            no.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });
        holder.itemView.setOnClickListener(v->{
            navController = Navigation.findNavController(holder.itemView);
            Bundle bundle = new Bundle();
            bundle.putString("job_id", String.valueOf(item.getId()));
            bundle.putString("position", item.getPosition());
            bundle.putString("company", item.getCompany());
            bundle.putString("advertiser_name", item.getAdvertiserName());
            bundle.putString("details", item.getDetails());
            bundle.putString("required_skills", item.getRequired_skills());
            bundle.putString("location", item.getJobLocation());
            bundle.putString("date", item.getCreated_at());
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
            this.jobName = itemView.findViewById(R.id.job_title);
            this.company = itemView.findViewById(R.id.company_name);
            this.jobLocation = itemView.findViewById(R.id.job_location);
            this.apply = itemView.findViewById(R.id.apply);
        }
    }
}
