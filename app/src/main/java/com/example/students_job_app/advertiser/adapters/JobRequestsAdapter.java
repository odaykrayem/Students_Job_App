package com.example.students_job_app.advertiser.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.students_job_app.R;
import com.example.students_job_app.model.JobRequest;
import com.example.students_job_app.utils.Constants;

import java.util.ArrayList;

public class JobRequestsAdapter extends RecyclerView.Adapter<JobRequestsAdapter.ViewHolder> {

    Context context;
    private ArrayList<JobRequest> list;
    OnRequestButtonClicked onRequestButtonClicked;
    public NavController navController;

    public JobRequestsAdapter(Context context, ArrayList<JobRequest> list , OnRequestButtonClicked onRequestButtonClicked) {
        this.context = context;
        this.list = list;
        this.onRequestButtonClicked =onRequestButtonClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_job_request, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JobRequest item = list.get(position);

        holder.studentName.setText(item.getStudentName());
        holder.jobTitle.setText(item.getJob_title());
        holder.date.setText(item.getDate());
        Log.e("st", item.getStatus()+"");
        if(item.getStatus() == Constants.JOB_REQUEST_ACCEPTED){
            holder.status.setText(Constants.JOB_REQUEST_ACCEPTED_TXT);
            holder.status.setBackground(context.getDrawable(R.drawable.bg_status_accepted));
            holder.layoutBtns.setVisibility(View.GONE);
            holder.layoutStatus.setVisibility(View.VISIBLE);
        }else if(item.getStatus() == Constants.JOB_REQUEST_REJECTED){
            holder.status.setText(Constants.JOB_REQUEST_REJECTED_TXT);
            holder.status.setBackground(context.getDrawable(R.drawable.bg_status_rejected));
            holder.layoutBtns.setVisibility(View.GONE);
            holder.layoutStatus.setVisibility(View.VISIBLE);
        }else{
            holder.layoutBtns.setVisibility(View.VISIBLE);
            holder.layoutStatus.setVisibility(View.GONE);
        }
        holder.accept.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_accept_reuqest, null);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            TextView yes = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            yes.setOnClickListener(l->{
                onRequestButtonClicked.onAcceptSelected(String.valueOf(item.getId()));
                dialog.dismiss();
            });

            no.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });

        holder.reject.setOnClickListener(v->{
                LayoutInflater factory = LayoutInflater.from(context);
                final View view1 = factory.inflate(R.layout.dialog_reject_reuqest, null);
                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setView(view1);
                dialog.setCanceledOnTouchOutside(true);

                TextView yes = view1.findViewById(R.id.yes_btn);
                TextView no = view1.findViewById(R.id.no_btn);
                yes.setOnClickListener(l->{
                    onRequestButtonClicked.onRejectSelected(String.valueOf(item.getId()));
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
            bundle.putSerializable(Constants.KEY_STUDENT, item.getStudent());
            bundle.putSerializable(Constants.KEY_JOB_ID, item.getId());
            navController.navigate(R.id.action_request_to_studentDetailsFragment,bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView studentName, jobTitle, date, status;
        Button accept, reject;
        LinearLayout layoutBtns, layoutStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            this.studentName = itemView.findViewById(R.id.student_name);
            this.jobTitle = itemView.findViewById(R.id.job_title);
            this.date = itemView.findViewById(R.id.date);
            this.status = itemView.findViewById(R.id.status);
            this.accept = itemView.findViewById(R.id.accept);
            this.reject = itemView.findViewById(R.id.reject);
            this.layoutBtns = itemView.findViewById(R.id.layout_btns);
            this.layoutStatus = itemView.findViewById(R.id.layout_status);
        }
    }

    public interface OnRequestButtonClicked {
         void onAcceptSelected(String requestId);
         void onRejectSelected(String requestId);
    }
}
