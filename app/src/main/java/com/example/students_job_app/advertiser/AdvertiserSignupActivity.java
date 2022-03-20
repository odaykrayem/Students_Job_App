package com.example.students_job_app.advertiser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.LoginActivity;
import com.example.students_job_app.R;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.student.StudentSignupActivity;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;

public class AdvertiserSignupActivity extends AppCompatActivity {

    EditText mNameET, mEmailET,mPasswordET, mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation;
    String name, email,password, phone, website, location, profField, yearsOfIncorporation;
    Button mSignUpBtn, mGoToLogin, mGoToRegisterStudent;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser_signup);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mNameET = findViewById(R.id.name);
        mEmailET = findViewById(R.id.email);
        mPasswordET= findViewById(R.id.email);
        mPhoneET= findViewById(R.id.phone);
        mWebsiteET= findViewById(R.id.website);
        mLocationET= findViewById(R.id.location);
        mProfField= findViewById(R.id.prof_field);
        mYearsOfIncorporation= findViewById(R.id.years_of_corp);


        mSignUpBtn= findViewById(R.id.btnSignup);
        mGoToLogin= findViewById(R.id.btnLinkToLoginScreen);
        mGoToRegisterStudent= findViewById(R.id.btnLinkToRegisterStudent);

        mGoToLogin.setOnClickListener(v->{
            startActivity(new Intent(AdvertiserSignupActivity.this, LoginActivity.class));
            finish();
        });

        mGoToRegisterStudent.setOnClickListener(v->{
            startActivity(new Intent(AdvertiserSignupActivity.this, AdvertiserSignupActivity.class));
            finish();
        });

        mSignUpBtn.setOnClickListener(v->{
            if(Validation.validateInput(this, mNameET, mEmailET,mPasswordET, mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation)){
                verifyEmail();
            }
        });
    }

    private void signUp() {
        String url = Urls.REGISTER_ADVERTISER;
        name = mNameET.getText().toString().trim();
        email= mEmailET.getText().toString().trim();
        password= mPasswordET.getText().toString().trim();
        phone= mPhoneET.getText().toString().trim();
        website = mWebsiteET.getText().toString().trim();
        location = mLocationET.getText().toString().trim();
        profField = mProfField.getText().toString().trim();
        yearsOfIncorporation = mYearsOfIncorporation.getText().toString().trim();

        AndroidNetworking.post(url)
                .addBodyParameter("advertiser_name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", password)
                .addBodyParameter("website", website)
                .addBodyParameter("professional_fields", profField)
                .addBodyParameter("location", location)
                .addBodyParameter("years_of_incorporation", yearsOfIncorporation)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            SharedPrefManager.getInstance(AdvertiserSignupActivity.this).advertiserLogin(
                                    new Advertiser(
                                            Integer.parseInt(response.getString("id")),
                                            response.getString("advertiser_name"),
                                            response.getString("phone"),
                                            response.getString("email"),
                                            response.getString("website"),
                                            response.getString("location"),
                                            response.getString("professional_fields"),
                                            response.getString("years_of_incorporation")
                                    )
                            );
                            //converting response to json object
//                            if(response.getInt("status") == 1){
//                                JSONArray items = response.getJSONArray("list");
//                                for(int i = 0; i < items.length(); i++){
//                                    JSONObject item = items.getJSONObject(i);
//
//                                }
//
//
//
//                            } else if(response.getInt("status") == 0){
//                                Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("amn catch", e.getMessage() );
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AdvertiserSignupActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("main", anError.getMessage());
                    }
                });
    }

    private void verifyEmail() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dialog_verify_email, null);
        final AlertDialog verificationDialog = new AlertDialog.Builder(this).create();
        verificationDialog.setView(view);
        verificationDialog.setCanceledOnTouchOutside(true);

        EditText code = view.findViewById(R.id.code);
        Button verify = view.findViewById(R.id.btn_verify_code);
        verify.setOnClickListener(v->{
            if(!code.getText().toString().trim().isEmpty()){
                sendVerificationRequest(code.getText().toString().trim());
                verificationDialog.dismiss();
            }
        });


        verificationDialog.show();
    }

    private void sendVerificationRequest(String code) {

        String url = Urls.EMAIL_VERIFICATION;
        String email = mEmailET.getText().toString().trim();
        mSignUpBtn.setEnabled(false);
        pDialog.show();
        Validation.setEnabled(this, false, mNameET, mEmailET,mPasswordET, mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );
        AndroidNetworking.post(url)
                .addBodyParameter("email", email)
                .addBodyParameter("code", code)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();

                        try {
                            JSONObject obj = response;
                            if(response.getInt("verified") == 1){
                                signUp();
                            }else{
                                Toast.makeText(AdvertiserSignupActivity.this, getResources().getString(R.string.verification_error), Toast.LENGTH_SHORT).show();

                            }
                            Validation.setEnabled(AdvertiserSignupActivity.this, true, mNameET, mEmailET,mPasswordET, mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Validation.setEnabled(AdvertiserSignupActivity.this, true, mNameET, mEmailET,mPasswordET, mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );

                        }
                        mSignUpBtn.setEnabled(true);


                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSignUpBtn.setEnabled(true);
                        Validation.setEnabled(AdvertiserSignupActivity.this, true, mNameET, mEmailET,mPasswordET, mPhoneET, mWebsiteET, mLocationET, mProfField, mYearsOfIncorporation );
                        Toast.makeText(AdvertiserSignupActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}