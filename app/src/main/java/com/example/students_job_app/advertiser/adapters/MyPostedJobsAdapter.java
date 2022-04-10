package com.example.students_job_app.advertiser.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MyPostedJobsAdapter extends RecyclerView.Adapter<MyPostedJobsAdapter.ViewHolder> {

    Context context;
    private ArrayList<JobOpportunity> list;
    public NavController navController;
    ProgressDialog pDialog;
    public MyPostedJobsAdapter(Context context, ArrayList<JobOpportunity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPostedJobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_posted_job, parent, false);
        MyPostedJobsAdapter.ViewHolder viewHolder = new MyPostedJobsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostedJobsAdapter.ViewHolder holder, int position) {

        JobOpportunity item = list.get(position);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        holder.jobName.setText(item.getTitle());
        holder.company.setText(item.getCompany());
        holder.location.setText(item.getJobLocation());

        holder.delete.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_delete_job, null);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            TextView yes = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            yes.setOnClickListener(l->{
                deleteJob(String.valueOf(item.getId()), position);
                dialog.dismiss();
            });

            no.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });

    }

    private void deleteJob(String jobOpportunityId, int position) {

        pDialog.show();

        String url = Urls.DELETE_JOB;
        String advertiser_id = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.post(url)
                .addBodyParameter("advertiser_id", advertiser_id)
                .addBodyParameter("job_opportunity_id", jobOpportunityId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User Saved";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.job_delete), Toast.LENGTH_SHORT).show();
                                list.remove(position);
                                notifyItemRemoved(position);
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.error_delete), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("delete catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("deleteerror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("advertiser_id")) {
                                Toast.makeText(context, data.getJSONArray("advertiser_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("job_opportunity_id")) {
                                Toast.makeText(context, data.getJSONArray("job_opportunity_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobName, company, location;
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.jobName = itemView.findViewById(R.id.job_title);
            this.company = itemView.findViewById(R.id.company_name);
            this.location = itemView.findViewById(R.id.job_location);
            this.delete = itemView.findViewById(R.id.delete);
        }
    }

}

