package com.example.students_job_app.advertiser.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class AdvertiserProfileFragment extends Fragment {

    TextView mNameTV, mEmailT, mPhoneTV, mWebsiteTV, mLocationTV, mProfFieldTV, mYearsOfIncorporationTV;
    Button mUpdateProfileBtn, mPayBtn;

    public NavController navController;
    ProgressDialog pDialog;

    Context context;
    AlertDialog dialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AdvertiserProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertiser_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameTV = view.findViewById(R.id.company_name);
        mLocationTV = view.findViewById(R.id.location);
        mPhoneTV = view.findViewById(R.id.phone);
        mEmailT = view.findViewById(R.id.email);
        mWebsiteTV = view.findViewById(R.id.website);
        mYearsOfIncorporationTV = view.findViewById(R.id.years_of_corp);
        mProfFieldTV = view.findViewById(R.id.prof_field);
        mUpdateProfileBtn = view.findViewById(R.id.update);
        mPayBtn = view.findViewById(R.id.pay_bill);

        Advertiser advertiser = SharedPrefManager.getInstance(context).getAdvertiserData();
        mNameTV.setText(advertiser.getAdvertiserName());
        mLocationTV.setText(advertiser.getAddress());
        mPhoneTV.setText(advertiser.getPhone());
        mEmailT.setText(advertiser.getEmail());
        mWebsiteTV.setText(advertiser.getWebsite());
        mYearsOfIncorporationTV.setText(advertiser.getYears_of_incorporation());
        mProfFieldTV.setText(advertiser.getProfessional_field());

        mUpdateProfileBtn.setOnClickListener(v->{
            navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment);
        });

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing Please wait...");

        if(SharedPrefManager.getInstance(context).hasBill()){
            mPayBtn.setEnabled(true);
        }else {
            mPayBtn.setEnabled(false);
        }
        mPayBtn.setEnabled(false);
        if(SharedPrefManager.getInstance(context).hasBill()){
            mPayBtn.setEnabled(true);
        }
        mPayBtn.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_credit_card, null);
             dialog = new AlertDialog.Builder(context).create();
            dialog.setView(view1);
            dialog.setCanceledOnTouchOutside(true);

            EditText number = view1.findViewById(R.id.number);
            EditText month = view1.findViewById(R.id.month);
            EditText day = view1.findViewById(R.id.day);
            EditText cvv = view1.findViewById(R.id.cvv);
            Button creditBtn = view1.findViewById(R.id.credit_btn);
            Button cancel = view1.findViewById(R.id.cancel);
            creditBtn.setOnClickListener(l->{
                if(Validation.validateInput(context, number, month, day, cvv)){
                    payBill();
                }
            });

            cancel.setOnClickListener(l->{
                dialog.dismiss();
            });
            dialog.show();
        });
    }


    private void payBill() {
        pDialog.show();
        String url = Urls.ADVERTISER_PAY_BILL;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        String billId = String.valueOf(SharedPrefManager.getInstance(context).getBillID());
        Log.e("billid", billId);
        AndroidNetworking.post(url)
                .addBodyParameter("advertiser_id", userId)
                .addBodyParameter("bill_id", billId)
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
                                Toast.makeText(context, context.getResources().getString(R.string.bill_paid), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.error_pay_bill), Toast.LENGTH_SHORT).show();
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
}