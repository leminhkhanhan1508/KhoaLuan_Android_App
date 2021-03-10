package com.example.attendence.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.attendence.Model.Course;
import com.example.attendence.R;


import java.util.List;

public class AdapterCourse extends ArrayAdapter<Course> {
    @NonNull
    Activity context;
    int resource;
    @NonNull
    List<Course> courses;

    public AdapterCourse(@NonNull Context context, int resource, @NonNull List<Course> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resource = resource;
        this.courses = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        TextView txtCodeCourse = row.findViewById(R.id.txtCodeCourse);
        TextView txtNameCourse = row.findViewById(R.id.txtNameCourse);
        TextView txtLecturer = row.findViewById(R.id.txtLecturer);
        txtCodeCourse.setText(this.courses.get(position).getName());
        txtNameCourse.setText(this.courses.get(position).getCode());
        txtLecturer.setText(this.courses.get(position).getLecturer());
        return row;
    }
}
