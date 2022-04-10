package com.example.students_job_app.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.students_job_app.R;


public class Validation {

    public static boolean validateInput(Context ctx, EditText...fields){
        for (EditText editText: fields) {
            if (editText.getText().toString().trim().isEmpty()){
                Toast.makeText(ctx, ctx.getResources().getString(R.string.missing_fields_message), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    public static void setEnabled(Context ctx,boolean isEnabled ,EditText...fields){
        for (EditText editText: fields) {
           editText.setEnabled(isEnabled);
        }
    }

}