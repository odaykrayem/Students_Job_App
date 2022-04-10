package com.example.students_job_app.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.activities.LoginActivity;
import com.example.students_job_app.R;
import com.example.students_job_app.advertiser.AdvertiserSignupActivity;
import com.example.students_job_app.model.Student;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.FilePath;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import com.example.students_job_app.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

public class StudentSignupActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    EditText mUserNameET, mNameET, mPhoneET, mEmailET,mPasswordET, mPlaceOfStudyET, mTypeOfStudyET;
    EditText mBirthDateET, mStudyStartDateET, mStudyEndDateET;
    String userName, name , phone, email,password,placeOfStudy, typeOfStudy,birthDate, studyStartDate, studyEndDate ;
    int gender = -1;

    ProgressDialog pDialog;
    boolean onGoing , cvSelected = false;

    CheckBox mOnGoingCB;
    Button mUploadCVBtn, mSignUpBtn, mToLoginBtn, mToRegisterAdvertiserBtn;
    RadioGroup genderRG;

    Uri pdfUri;
    final Calendar myCalendar = Calendar.getInstance();
    String filePath;
    AlertDialog verificationDialog;
    String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mUserNameET = findViewById(R.id.user_name);
        mNameET = findViewById(R.id.name);
        mPhoneET = findViewById(R.id.phone);
        mEmailET = findViewById(R.id.email);
        mPasswordET = findViewById(R.id.password);
        mPlaceOfStudyET = findViewById(R.id.place_of_study);
        mTypeOfStudyET = findViewById(R.id.type_of_study);
        mOnGoingCB = findViewById(R.id.ongoing);
        mUploadCVBtn = findViewById(R.id.uploadCV);
        mSignUpBtn = findViewById(R.id.btnSignup);
        mToLoginBtn = findViewById(R.id.btnLinkToLoginScreen);
        mToRegisterAdvertiserBtn = findViewById(R.id.btnLinkToRegisterAdvertiser);

        genderRG = findViewById(R.id.gender_selector);

        mBirthDateET = findViewById(R.id.birth);
        mStudyStartDateET = findViewById(R.id.start_of_study);
        mStudyEndDateET = findViewById(R.id.end_of_study);


        mStudyEndDateET.setEnabled(false);
        mStudyEndDateET.setText("");
        mStudyEndDateET.setHint("- - - - - -");
        onGoing = true;
        mOnGoingCB.setChecked(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        gender = Constants.MALE;
        genderRG.check(R.id.male);

        mToLoginBtn.setOnClickListener(v->{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        mToRegisterAdvertiserBtn.setOnClickListener(v->{
            startActivity(new Intent(this, AdvertiserSignupActivity.class));
            finish();
        });

        mBirthDateET.setOnClickListener(v -> {
            final DatePickerDialog  picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = "";
                    if(String.valueOf(monthOfYear).length()<2){
                        month = "0" + monthOfYear;
                    }else{
                        month = "" + monthOfYear;
                    }
                    String day = "";
                    if(String.valueOf(dayOfMonth).length()<2){
                        day = "0"+ dayOfMonth;
                    }else{
                        day = ""+dayOfMonth;
                    }
                    mBirthDateET.setText(""+year+"-"+month+"-"+day);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        mStudyStartDateET.setOnClickListener(v -> {
            final DatePickerDialog  picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = "";
                    if(String.valueOf(monthOfYear).length()<2){
                        month = "0" + monthOfYear;
                    }else{
                        month = "" + monthOfYear;
                    }
                    String day = "";
                    if(String.valueOf(dayOfMonth).length()<2){
                        day = "0"+ dayOfMonth;
                    }else{
                        day = ""+dayOfMonth;
                    }
                    mStudyStartDateET.setText(""+year+"-"+month+"-"+day);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        mStudyEndDateET.setOnClickListener(v -> {
            final DatePickerDialog  picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = "";
                    if(String.valueOf(monthOfYear).length()<2){
                        month = "0" + monthOfYear;
                    }else{
                        month = "" + monthOfYear;
                    }
                    String day = "";
                    if(String.valueOf(dayOfMonth).length()<2){
                        day = "0"+ dayOfMonth;
                    }else{
                        day = ""+dayOfMonth;
                    }
                    mStudyEndDateET.setText(""+year+"-"+month+"-"+day);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        mOnGoingCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                mStudyEndDateET.setEnabled(false);
                mStudyEndDateET.setText("");
                mStudyEndDateET.setHint("- - - - - -");
                onGoing = true;

            }else{
                mStudyEndDateET.setEnabled(true);
                onGoing = false;
            }
        });

        genderRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.male:
                    gender = Constants.MALE;
                    break;
                case R.id.female:
                    gender = Constants.FEMALE;
                    break;
            }
        });

        mSignUpBtn.setOnClickListener(v->{
            if(Validation.validateInput(this, mUserNameET, mNameET,  mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                   , mBirthDateET, mStudyStartDateET)){
                if(onGoing){
                    if(cvSelected){
//                    verifyEmail();
                        signUp();
                    }else{
                        Toast.makeText(this, getResources().getString(R.string.please_upload_cv), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(mStudyEndDateET.getText().toString().isEmpty()){
                        Toast.makeText(this, getResources().getString(R.string.missing_fields_message), Toast.LENGTH_SHORT).show();
                    }else{
                        signUp();
                    }
                }

            }

        });
        mUploadCVBtn.setOnClickListener(v->{
            requestRead();
        });
    }

    //..................Methods for File Chooser.................
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openFileChooser();
        }
    }


    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            Log.d("filePath", String.valueOf(pdfUri));

            Uri picUri = pdfUri;
            Log.d("filePath", String.valueOf(picUri));

            filePath = FilePath.getPath(this,picUri);
            if (filePath != null) {
                Log.d("filePath", filePath);
                Toast.makeText(this, "started searching", Toast.LENGTH_SHORT).show();
                cvSelected = true;
            }
            else
            {
                Toast.makeText(this,"no image selected", Toast.LENGTH_LONG).show();
            }
        }
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
        Validation.setEnabled(this, false, mNameET, mEmailET,mPasswordET, mPhoneET,  mUserNameET, mNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                , mStudyEndDateET, mBirthDateET, mStudyStartDateET );
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

                            }else{
                                Toast.makeText(StudentSignupActivity.this, getResources().getString(R.string.verification_error), Toast.LENGTH_SHORT).show();

                            }
                            Validation.setEnabled(StudentSignupActivity.this, true, mNameET, mEmailET,mPasswordET, mPhoneET,  mUserNameET, mNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                   , mStudyEndDateET, mBirthDateET, mStudyStartDateET);
                            mSignUpBtn.setEnabled(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Validation.setEnabled(StudentSignupActivity.this, true, mNameET, mEmailET,mPasswordET, mPhoneET, mUserNameET, mNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                   , mStudyEndDateET, mBirthDateET, mStudyStartDateET);

                        }
                        mSignUpBtn.setEnabled(true);
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSignUpBtn.setEnabled(true);
                        Validation.setEnabled(StudentSignupActivity.this, true,  mUserNameET, mNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                                , mStudyEndDateET, mBirthDateET, mStudyStartDateET);
                        Toast.makeText(StudentSignupActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        String url = Urls.REGISTER_STUDENT;
        mSignUpBtn.setEnabled(false);
        pDialog.show();
        userName = mUserNameET.getText().toString().trim();
        name = mNameET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        email = mEmailET.getText().toString().trim();
        password = mPasswordET.getText().toString().trim();
        birthDate = mBirthDateET.getText().toString().trim();
        placeOfStudy = mPlaceOfStudyET.getText().toString().trim();
        typeOfStudy = mTypeOfStudyET.getText().toString().trim();
        studyStartDate = mStudyStartDateET.getText().toString().trim();
        studyEndDate = mStudyEndDateET.getText().toString().trim();

        AndroidNetworking.upload(url)
                .addMultipartFile("cv", new File(filePath))
                .addMultipartParameter("user_name", userName)
                .addMultipartParameter("name", "name")
                .addMultipartParameter("nick_name", name)
                .addMultipartParameter("email", email)
                .addMultipartParameter("phone", phone)
                .addMultipartParameter("password", password)
                .addMultipartParameter("gender", String.valueOf(gender))
                .addMultipartParameter("birth_date", birthDate)
                .addMultipartParameter("study_type", typeOfStudy)
                .addMultipartParameter("study_place", placeOfStudy)
                .addMultipartParameter("start_study", studyStartDate)
                .addMultipartParameter("end_study", studyEndDate)
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
                                SharedPrefManager.getInstance(StudentSignupActivity.this).studentLogin(
                                    new Student(
                                            Integer.parseInt(object.getString("id")),
                                            object.getString("user_name"),
                                            object.getString("nick_name"),
                                            object.getString("phone"),
                                            object.getString("email"),
                                            object.getString("birth_date").substring(0,10),
                                            Integer.parseInt(object.getString("gender")),
                                            object.getString("study_type"),
                                            object.getString("study_place"),
                                            object.getString("study_start_date").substring(0,10),
                                            object.getString("study_end_date").substring(0,10),
                                            object.getString("study_end_date").isEmpty()|| object.getString("study_end_date").equals("null"),
                                            object.getString("cv_url")
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
                            Log.e("register st catch", e.getMessage());
                            pDialog.dismiss();
                            mSignUpBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSignUpBtn.setEnabled(true);
                        Log.e("registersterror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(StudentSignupActivity.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("image_url")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("image_url").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("user_name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("user_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("nick_name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("nick_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("phone")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("phone").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("gender")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("gender").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("birth_date")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("birth_date").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("study_type")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("study_type").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("study_place")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("study_place").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("start_study")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("study_start").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("end_study")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("end_study").toString(), Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(StudentSignupActivity.this, StudentMain.class));
                    finish();                }else{
                    Toast.makeText(this, getResources().getString(R.string.error_code), Toast.LENGTH_SHORT).show();
                }
            }
        });
        verificationDialog.show();
    }
}