package com.example.students_job_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.advertiser.AdvertiserMain;
import com.example.students_job_app.advertiser.AdvertiserSignupActivity;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.student.StudentMain;
import com.example.students_job_app.student.StudentSignupActivity;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText mEmailET, mPasswordET;
    Button mLoginBtn, mToRegisterBtn;
    RadioGroup mUserTypeRG;
    int type = -1;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailET = findViewById(R.id.email);
        mPasswordET = findViewById(R.id.password);

        mLoginBtn = findViewById(R.id.btnLogin);
        mToRegisterBtn = findViewById(R.id.btnLinkToRegisterScreen);
        mUserTypeRG = findViewById(R.id.type_selector);
        type = Constants.USER_TYPE_STUDENT;
        mUserTypeRG.check(R.id.student);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mToRegisterBtn.setOnClickListener(v->{
            startActivity(new Intent(this, StudentSignupActivity.class));
            finish();
        });

        mUserTypeRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.student:
                    type = Constants.USER_TYPE_STUDENT;
                    break;
                case R.id.advertiser:
                    type = Constants.USER_TYPE_ADVERTISER;
                    break;
            }
        });

        mLoginBtn.setOnClickListener(v->{

            if(Validation.validateInput(this, mEmailET, mPasswordET)){
                login();
            }
        });
    }

    private void login() {
       String url = Urls.LOG_IN;
       mLoginBtn.setEnabled(false);
       pDialog.show();
       String email = mEmailET.getText().toString().trim();
       String password = mPasswordET.getText().toString().trim();
        AndroidNetworking.post(url)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
               .addBodyParameter("user_type", String.valueOf(type))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if(type == Constants.USER_TYPE_STUDENT){
                                SharedPrefManager.getInstance(LoginActivity.this).studentLogin(
                                        new Student(
                                                Integer.parseInt(response.getString("id")),
                                                response.getString("user_name"),
                                                response.getString("name") + response.getString("nick_name"),
                                                response.getString("phone"),
                                                response.getString("email"),
                                                response.getString("birth_date"),
                                                Integer.parseInt(response.getString("gender")),
                                                response.getString("study_type"),
                                                response.getString("study_place"),
                                                response.getString("study_start"),
                                                response.getString("study_end_date"),
                                                response.getBoolean("is_going"),
                                                response.getString("cv")
                                        )
                                );
                            }else{
                                SharedPrefManager.getInstance(LoginActivity.this).advertiserLogin(
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
                                goToHome(type);
                            }
                            mLoginBtn.setEnabled(true);
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("login", e.getMessage() );
                            mLoginBtn.setEnabled(true);
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(LoginActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("loginError", anError.getMessage());
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                    }
                });
    }

    private void goToHome(int type) {
        if(type == Constants.USER_TYPE_STUDENT){
            startActivity(new Intent(LoginActivity.this, StudentMain.class));
        }else{
            startActivity(new Intent(LoginActivity.this, AdvertiserMain.class));
        }
        finish();
    }

}