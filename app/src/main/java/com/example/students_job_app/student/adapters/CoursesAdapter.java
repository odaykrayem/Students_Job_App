package com.example.students_job_app.student.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.students_job_app.R;
import com.example.students_job_app.model.Course;
import com.example.students_job_app.model.JobOpportunity;
import com.example.students_job_app.student.OnButtonClickedListener;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    Context context;
    private List<Course> list;

    public CoursesAdapter(Context context, ArrayList<Course> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_course, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Course item = list.get(position);

        holder.courseName.setText(item.getName());
        holder.institution.setText(item.getInstitution());
        holder.startDate.setText(item.getStartDate());
        holder.endDate.setText(item.getEndDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView courseName, institution, startDate, endDate;

        public ViewHolder(View itemView) {
            super(itemView);
            this.courseName = itemView.findViewById(R.id.course_name);
            this.institution = itemView.findViewById(R.id.institution);
            this.startDate = itemView.findViewById(R.id.start_date);
            this.endDate = itemView.findViewById(R.id.end_date);
        }
    }
}