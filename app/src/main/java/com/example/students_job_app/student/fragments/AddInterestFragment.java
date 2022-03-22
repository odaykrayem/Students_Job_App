package com.example.students_job_app.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    Context ctx;
    EditText mInterestET;
    Button mAddInterestBtn;
    ProgressDialog pDialog;

    public AddInterestFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
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
        pDialog = new ProgressDialog(ctx);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mAddInterestBtn.setOnClickListener(v->{
            if(Validation.validateInput(ctx, mInterestET)){
                addInterest();
            }
        });
    }

    private void addInterest() {
        String url = Urls.ADD_INTEREST;
        String interest = mInterestET.getText().toString().trim();
        int id = SharedPrefManager.getInstance(ctx).getUserId();

        mAddInterestBtn.setEnabled(false);
        pDialog.show();

        AndroidNetworking.post(url)
                .addBodyParameter("student_id", String.valueOf(id))
                .addBodyParameter("interest", interest)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if(response.equals("true")){
                                Toast.makeText(ctx, ctx.getResources().getString(R.string.course_added), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ctx, ctx.getResources().getString(R.string.course_added_error), Toast.LENGTH_SHORT).show();
                            }
                            mAddInterestBtn.setEnabled(true);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("addInterest catch", e.getMessage() );
                            mAddInterestBtn.setEnabled(true);
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("addInterestError", anError.getMessage());
                        pDialog.dismiss();
                        mAddInterestBtn.setEnabled(true);

                    }
                });
    }


}