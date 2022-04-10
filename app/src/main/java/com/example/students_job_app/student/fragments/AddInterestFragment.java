package com.example.students_job_app.student.fragments;

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
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;


public class AddInterestFragment extends Fragment {

    Context context;
    EditText mInterestET;
    Button mAddInterestBtn;
    ProgressDialog pDialog;
    NavController navController;

    public AddInterestFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_interest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInterestET = view.findViewById(R.id.interest);
        mAddInterestBtn = view.findViewById(R.id.add_interest);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");
        navController = Navigation.findNavController(view);

        mAddInterestBtn.setOnClickListener(v->{
            if(Validation.validateInput(context, mInterestET)){
                addInterest();
            }
        });
    }

    private void addInterest() {
        String url = Urls.ADD_INTEREST;
        String interest = mInterestET.getText().toString().trim();

        mAddInterestBtn.setEnabled(false);
        pDialog.show();
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("student_id", userId)
                .addBodyParameter("interest", interest)
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
                                Toast.makeText(context, context.getResources().getString(R.string.add_interest_success), Toast.LENGTH_SHORT).show();
                                navController.popBackStack();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.post_job_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("addInterest", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("addInterestError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("student_id")) {
                                Toast.makeText(context, data.getJSONArray("student_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("course_name")) {
                                Toast.makeText(context, data.getJSONArray("course_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("institution")) {
                                Toast.makeText(context, data.getJSONArray("institution").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("start_date")) {
                                Toast.makeText(context, data.getJSONArray("start_date").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("end_date")) {
                                Toast.makeText(context, data.getJSONArray("end_date").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}