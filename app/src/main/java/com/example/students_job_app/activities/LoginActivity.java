package com.example.students_job_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.advertiser.AdvertiserMain;
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
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailET = findViewById(R.id.email);
        mPasswordET = findViewById(R.id.password);

        mLoginBtn = findViewById(R.id.btnLogin);
        mToRegisterBtn = findViewById(R.id.btnLinkToRegisterScreen);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mToRegisterBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentSignupActivity.class));
            finish();
        });


        mLoginBtn.setOnClickListener(v -> {
            if (Validation.validateInput(this, mEmailET, mPasswordET)) {
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
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User founded";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                int userType = userJson.getInt("type");

                                Log.e("uType", userType + "");
                                if (userType == Constants.USER_TYPE_STUDENT) {
                                    SharedPrefManager.getInstance(LoginActivity.this).studentLogin(
                                            new Student(
                                                    Integer.parseInt(userJson.getString("id")),
                                                    userJson.getString("user_name"),
                                                    userJson.getString("nick_name"),
                                                    userJson.getString("phone"),
                                                    userJson.getString("email"),
                                                    userJson.getString("birth_date").substring(0,10),
                                                    Integer.parseInt(userJson.getString("gender")),
                                                    userJson.getString("study_type"),
                                                    userJson.getString("study_place"),
                                                    userJson.getString("study_start_date").substring(0,10),
                                                    userJson.getString("study_end_date").equals("null")?"null":userJson.getString("study_end_date").substring(0,10),
                                                    userJson.getString("study_end_date").equals("null"),
                                                    userJson.getString("cv_url")
                                            )
                                    );
                                } else {
                                    SharedPrefManager.getInstance(LoginActivity.this).advertiserLogin(
                                            new Advertiser(
                                                    Integer.parseInt(userJson.getString("id")),
                                                    userJson.getString("name"),
                                                    userJson.getString("phone"),
                                                    userJson.getString("email"),
                                                    userJson.getString("website"),
                                                    userJson.getString("description"),
                                                    userJson.getString("address"),
                                                    userJson.getString("professional_field"),
                                                    userJson.getString("years_of_incorporation")
                                            )
                                    );
                                }
                                goToHome(userType);
                            }else{
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            mLoginBtn.setEnabled(true);
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mLoginBtn.setEnabled(true);
                            pDialog.dismiss();
                            Log.e("login", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                        Log.e("loginError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(LoginActivity.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("user_type")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("user_type").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void goToHome(int type) {
        if (type == Constants.USER_TYPE_STUDENT) {
            startActivity(new Intent(LoginActivity.this, StudentMain.class));
        } else {
            startActivity(new Intent(LoginActivity.this, AdvertiserMain.class));
        }
        finish();
    }

}