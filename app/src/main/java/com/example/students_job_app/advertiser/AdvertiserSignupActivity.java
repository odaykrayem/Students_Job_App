package com.example.students_job_app.advertiser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.students_job_app.activities.LoginActivity;
import com.example.students_job_app.R;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.student.StudentMain;
import com.example.students_job_app.student.StudentSignupActivity;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class AdvertiserSignupActivity extends AppCompatActivity {

    EditText mNameET, mEmailET, mPasswordET, mPhoneET, mWebsiteET, mAddressET, mProfField, mYearsOfIncorporation, mDescriptionET;
    String name, email, password, phone, website, address, profField, yearsOfIncorporation, description;
    Button mSignUpBtn, mGoToLogin, mGoToRegisterStudent;

    ProgressDialog pDialog;
    String verificationCode;
    AlertDialog verificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser_signup);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mNameET = findViewById(R.id.name);
        mEmailET = findViewById(R.id.email);
        mPasswordET = findViewById(R.id.password);
        mPhoneET = findViewById(R.id.phone);
        mWebsiteET = findViewById(R.id.website);
        mAddressET = findViewById(R.id.location);
        mProfField = findViewById(R.id.prof_field);
        mDescriptionET = findViewById(R.id.description);
        mYearsOfIncorporation = findViewById(R.id.years_of_corp);

        mSignUpBtn = findViewById(R.id.btnSignup);
        mGoToLogin = findViewById(R.id.btnLinkToLoginScreen);
        mGoToRegisterStudent = findViewById(R.id.btnLinkToRegisterStudent);

        mGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(AdvertiserSignupActivity.this, LoginActivity.class));
            finish();
        });

        mGoToRegisterStudent.setOnClickListener(v -> {
            startActivity(new Intent(AdvertiserSignupActivity.this, AdvertiserSignupActivity.class));
            finish();
        });

        mSignUpBtn.setOnClickListener(v -> {
            if (Validation.validateInput(this, mNameET, mEmailET, mPasswordET, mPhoneET, mWebsiteET, mAddressET, mProfField, mYearsOfIncorporation)) {
                signUp();
            }
        });
    }

    private void signUp() {
        String url = Urls.REGISTER_ADVERTISER;
        pDialog.show();
        mSignUpBtn.setEnabled(false);

        name = mNameET.getText().toString().trim();
        email = mEmailET.getText().toString().trim();
        password = mPasswordET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        website = mWebsiteET.getText().toString().trim();
        address = mAddressET.getText().toString().trim();
        profField = mProfField.getText().toString().trim();
        description = mDescriptionET.getText().toString().trim();
        yearsOfIncorporation = mYearsOfIncorporation.getText().toString().trim();

        AndroidNetworking.post(url)
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", password)
                .addBodyParameter("description", description)
                .addBodyParameter("website", website)
                .addBodyParameter("professional_fields", profField)
                .addBodyParameter("address", address)
                .addBodyParameter("years_of_incorporation", yearsOfIncorporation)
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
                            JSONObject object = obj.getJSONObject("data");

                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                SharedPrefManager.getInstance(AdvertiserSignupActivity.this).advertiserLogin(
                                        new Advertiser(
                                                Integer.parseInt(object.getString("id")),
                                                object.getString("name"),
                                                object.getString("phone"),
                                                object.getString("email"),
                                                object.getString("website"),
                                                object.getString("description"),
                                                object.getString("address"),
                                                object.getString("professional_field"),
                                                object.getString("years_of_incorporation")
                                        )
                                );
                                verificationCode = object.getString("status");
                                Log.e("code", verificationCode);
                                SharedPrefManager.getInstance(getApplicationContext()).setVerified(false);
                                SharedPrefManager.getInstance(getApplicationContext()).setVerificationCode(verificationCode);
                                verifyEmail(verificationCode);

                            }

                            pDialog.dismiss();
                            mSignUpBtn.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("register adv catch", e.getMessage());
                            pDialog.dismiss();
                            mSignUpBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSignUpBtn.setEnabled(true);
                        Log.e("registeradverror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(AdvertiserSignupActivity.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("advertiser_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("phone")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("phone").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("website")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("website").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("professional_fields")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("professional_fields").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("address")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("address").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("professional_field")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("professional_field").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("years_of_incorporation")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("years_of_incorporation").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("description")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("description").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void verifyEmail(String verificationCode) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dialog_verify_email, null);
        verificationDialog = new AlertDialog.Builder(this).create();
        verificationDialog.setView(view);
        verificationDialog.setCanceledOnTouchOutside(true);
        verificationDialog.setCancelable(false);


        EditText code = view.findViewById(R.id.code);
        Button verify = view.findViewById(R.id.btn_verify_code);
        verify.setOnClickListener(v -> {
            if (!code.getText().toString().trim().isEmpty()) {
                String codeFromUser = code.getText().toString().trim();
                if(codeFromUser.equals(verificationCode)){
                    Toast.makeText(this, getResources().getString(R.string.correct_code), Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).setVerified(true);
                    startActivity(new Intent(AdvertiserSignupActivity.this, AdvertiserMain.class));
                    finish();
                }else{
                    Toast.makeText(this, getResources().getString(R.string.error_code), Toast.LENGTH_SHORT).show();
                }
            }
        });
        verificationDialog.show();
    }


    private void sendVerificationRequest(String code) {

        String url = Urls.EMAIL_VERIFICATION;
        String email = mEmailET.getText().toString().trim();
        mSignUpBtn.setEnabled(false);
        pDialog.show();
        Validation.setEnabled(this, false, mNameET, mEmailET, mPasswordET, mPhoneET, mWebsiteET, mAddressET, mProfField, mYearsOfIncorporation);
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
                            if (response.getInt("verified") == 1) {
                                signUp();
                            } else {
                                Toast.makeText(AdvertiserSignupActivity.this, getResources().getString(R.string.verification_error), Toast.LENGTH_SHORT).show();

                            }
                            Validation.setEnabled(AdvertiserSignupActivity.this, true, mNameET, mEmailET, mPasswordET, mPhoneET, mWebsiteET, mAddressET, mProfField, mYearsOfIncorporation);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Validation.setEnabled(AdvertiserSignupActivity.this, true, mNameET, mEmailET, mPasswordET, mPhoneET, mWebsiteET, mAddressET, mProfField, mYearsOfIncorporation);

                        }
                        mSignUpBtn.setEnabled(true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSignUpBtn.setEnabled(true);
                        Validation.setEnabled(AdvertiserSignupActivity.this, true, mNameET, mEmailET, mPasswordET, mPhoneET, mWebsiteET, mAddressET, mProfField, mYearsOfIncorporation);
                        Toast.makeText(AdvertiserSignupActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}