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
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.students_job_app.LoginActivity;
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

    EditText mUserNameET, mNameET, mNickNameET, mPhoneET, mEmailET,mPasswordET, mPlaceOfStudyET, mTypeOfStudyET;
    EditText mBirthDateET, mStudyStartDateET, mStudyEndDateET;
    String userName, name , nickName, phone, email,password,placeOfStudy, typeOfStudy,birthDate, studyStartDate, studyEndDate ;
    int gender = -1;

    ProgressDialog pDialog;
    boolean onGoing , cvSelected;

    CheckBox mOnGoingCB;
    Button mUploadCVBtn, mSignUpBtn, mToLoginBtn, mToRegisterAdvertiserBtn;
    RadioGroup genderRG;

    Uri pdfUri;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mUserNameET = findViewById(R.id.user_name);
        mNameET = findViewById(R.id.name);
        mNickNameET = findViewById(R.id.nick_name);
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
                    mBirthDateET.setText(""+year+"/"+monthOfYear+"/"+dayOfMonth);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        mStudyStartDateET.setOnClickListener(v -> {
            final DatePickerDialog  picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mStudyStartDateET.setText(""+year+"/"+monthOfYear+"/"+dayOfMonth);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        mStudyEndDateET.setOnClickListener(v -> {
            final DatePickerDialog  picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mStudyEndDateET.setText(""+year+"/"+monthOfYear+"/"+dayOfMonth);
                }

            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        mOnGoingCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            if(Validation.validateInput(this, mUserNameET, mNameET, mNickNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                   , mStudyEndDateET, mBirthDateET, mStudyStartDateET)){
                if(cvSelected){
                    verifyEmail();
                }else{
                    Toast.makeText(this, getResources().getString(R.string.please_upload_cv), Toast.LENGTH_SHORT).show();
                }
            }

        });
        mUploadCVBtn.setOnClickListener(v->{
            requestRead();
//            uploadCV();

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

            String filePath = FilePath.getPath(this,picUri);
            if (filePath != null) {
                Log.d("filePath", filePath);
                Toast.makeText(this, "started searching", Toast.LENGTH_SHORT).show();
                uploadCV(filePath);
            }
            else
            {
                Toast.makeText(this,"no image selected", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void uploadCV(String filePath) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        File file = new File(filePath);

        mSignUpBtn.setEnabled(false);
        AndroidNetworking.upload(Urls.UPLOAD_CV)
                .addMultipartFile("pdf", file)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();
                        try {
                            //converting response to json object
                            JSONObject obj = response;

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                cvSelected = true;
                                mUploadCVBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_input_selected));
                            } else {
                                Toast.makeText(StudentSignupActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                            mSignUpBtn.setEnabled(true);
                            pDialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mSignUpBtn.setEnabled(true);
                            pDialog.hide();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("Error", error.getMessage());
                        pDialog.hide();
                        mSignUpBtn.setEnabled(true);

                    }
                });
    }

    private void signUp() {

        String url = Urls.REGISTER_STUDENT;
        userName = mUserNameET.getText().toString().trim();
        name = mNameET.getText().toString().trim();
        nickName = mNickNameET.getText().toString().trim() + " " + mNickNameET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        email = mEmailET.getText().toString().trim();
        password = mPasswordET.getText().toString().trim();
        birthDate = mBirthDateET.getText().toString().trim();
        placeOfStudy = mPlaceOfStudyET.getText().toString().trim();
        typeOfStudy = mTypeOfStudyET.getText().toString().trim();
        studyStartDate = mStudyStartDateET.getText().toString().trim();
        studyEndDate = mStudyEndDateET.getText().toString().trim();
//        onGoing
        AndroidNetworking.post(url)
                .addBodyParameter("user_name", userName)
                .addBodyParameter("name", name)
                .addBodyParameter("nick_name", nickName)
                .addBodyParameter("email", email)
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", password)
                .addBodyParameter("gender", String.valueOf(gender))
                .addBodyParameter("birth_date", birthDate)
                .addBodyParameter("study_type", typeOfStudy)
                .addBodyParameter("study_place", placeOfStudy)
                .addBodyParameter("study_start", studyStartDate)
                .addBodyParameter("study_end_date", studyEndDate)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            SharedPrefManager.getInstance(StudentSignupActivity.this).studentLogin(
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("amn catch", e.getMessage() );
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(StudentSignupActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
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
        Validation.setEnabled(this, false, mNameET, mEmailET,mPasswordET, mPhoneET,  mUserNameET, mNameET, mNickNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
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
                                signUp();
                            }else{
                                Toast.makeText(StudentSignupActivity.this, getResources().getString(R.string.verification_error), Toast.LENGTH_SHORT).show();

                            }
                            Validation.setEnabled(StudentSignupActivity.this, true, mNameET, mEmailET,mPasswordET, mPhoneET,  mUserNameET, mNameET, mNickNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                   , mStudyEndDateET, mBirthDateET, mStudyStartDateET);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Validation.setEnabled(StudentSignupActivity.this, true, mNameET, mEmailET,mPasswordET, mPhoneET, mUserNameET, mNameET, mNickNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                   , mStudyEndDateET, mBirthDateET, mStudyStartDateET);

                        }
                        mSignUpBtn.setEnabled(true);
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSignUpBtn.setEnabled(true);
                        Validation.setEnabled(StudentSignupActivity.this, true,  mUserNameET, mNameET, mNickNameET, mPhoneET, mEmailET, mPlaceOfStudyET, mTypeOfStudyET
                                , mStudyEndDateET, mBirthDateET, mStudyStartDateET);
                        Toast.makeText(StudentSignupActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}