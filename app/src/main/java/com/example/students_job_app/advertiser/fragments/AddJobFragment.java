package com.example.students_job_app.advertiser.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.activities.LoginActivity;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class AddJobFragment extends Fragment {

    EditText mTitleET, mCompanyNameET, mJobLocationET, mPositionET, mRequiredSkillsET, mDetailsET;
    String title, companyName, jobLocation, position, requiredSkills, details;
    Button mPostJobOpportunityBtn;

    Context context;
    ProgressDialog pDialog;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AddJobFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_job, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleET = view.findViewById(R.id.title);
        mCompanyNameET = view.findViewById(R.id.company_name);
        mJobLocationET = view.findViewById(R.id.job_location);
        mPositionET = view.findViewById(R.id.position);
        mRequiredSkillsET = view.findViewById(R.id.required_skills);
        mDetailsET = view.findViewById(R.id.details);
        mPostJobOpportunityBtn = view.findViewById(R.id.post_job);

        navController = Navigation.findNavController(view);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mPostJobOpportunityBtn.setOnClickListener(v->{
            if(Validation.validateInput(context, mTitleET, mCompanyNameET, mJobLocationET, mPositionET, mRequiredSkillsET, mDetailsET)){
                postJob();
            }
        });
    }

    private void postJob() {
        pDialog.show();
        mPostJobOpportunityBtn.setEnabled(false);

        String url = Urls.POST_JOB;

        title = mTitleET.getText().toString().trim();
        companyName= mTitleET.getText().toString().trim();
        jobLocation= mJobLocationET.getText().toString().trim();
        position= mPositionET.getText().toString().trim();
        requiredSkills= mRequiredSkillsET.getText().toString().trim();
        details= mDetailsET.getText().toString().trim();

        String advertiserId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("advertiser_id", advertiserId)
                .addBodyParameter("title", title)
                .addBodyParameter("company_name", companyName)
                .addBodyParameter("job_location", jobLocation)
                .addBodyParameter("position", position)
                .addBodyParameter("required_skills", requiredSkills)
                .addBodyParameter("details", details)
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
                                Toast.makeText(context, context.getResources().getString(R.string.post_job_success), Toast.LENGTH_SHORT).show();
                                navController.popBackStack();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.post_job_error), Toast.LENGTH_SHORT).show();
                            }
                            mPostJobOpportunityBtn.setEnabled(true);
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mPostJobOpportunityBtn.setEnabled(true);
                            pDialog.dismiss();
                            Log.e("addjob catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mPostJobOpportunityBtn.setEnabled(true);
                        Log.e("addjobError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("advertiser_id")) {
                                Toast.makeText(context, data.getJSONArray("advertiser_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("title")) {
                                Toast.makeText(context, data.getJSONArray("title").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("company_name")) {
                                Toast.makeText(context, data.getJSONArray("company_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("job_location")) {
                                Toast.makeText(context, data.getJSONArray("job_location").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("position")) {
                                Toast.makeText(context, data.getJSONArray("position").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("required_skills")) {
                                Toast.makeText(context, data.getJSONArray("required_skills").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("details")) {
                                Toast.makeText(context, data.getJSONArray("details").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}