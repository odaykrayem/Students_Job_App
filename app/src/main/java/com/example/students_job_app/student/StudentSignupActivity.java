package com.example.students_job_app.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
                final DatePickerDialog  picker = new DatePickerDialog(StudentSignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthDateET.setText(""+year+"/"+monthOfYear+"/"+dayOfMonth);
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        });

        studyStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog  picker = new DatePickerDialog(StudentSignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        studyStartDateET.setText(""+year+"/"+monthOfYear+"/"+dayOfMonth);
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        });
        studyEndDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog  picker = new DatePickerDialog(StudentSignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        studyEndDateET.setText(""+year+"/"+monthOfYear+"/"+dayOfMonth);
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }

        });
    }


}