package com.example.students_job_app.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.students_job_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StudentSignupActivity extends AppCompatActivity {

    EditText birthDateET, studyStartDateET, studyEndDateET;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        birthDateET = findViewById(R.id.birth);
        studyStartDateET = findViewById(R.id.start_of_study);
        studyEndDateET = findViewById(R.id.end_of_study);

        birthDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDateET.setSelected(true);
                studyStartDateET.setSelected(false);
                studyEndDateET.setSelected(false);

                new DatePickerDialog(StudentSignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        studyStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDateET.setSelected(false);
                studyStartDateET.setSelected(true);
                studyEndDateET.setSelected(false);
                new DatePickerDialog(StudentSignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        studyEndDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDateET.setSelected(false);
                studyStartDateET.setSelected(false);
                studyEndDateET.setSelected(true);

                new DatePickerDialog(StudentSignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)){

                }.show();

            }
        });
    }

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(birthDateET.isSelected()){
            birthDateET.setText(sdf.format(myCalendar.getTime()));
        }else if(studyStartDateET.isSelected()){
            studyStartDateET.setText(sdf.format(myCalendar.getTime()));
        }else{
            studyEndDateET.setText(sdf.format(myCalendar.getTime()));

        }
    };
}