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
import com.example.attendence.Model.Lesson;
import com.example.attendence.R;


import java.util.List;

public class AdapterLesson extends ArrayAdapter<Lesson> {
    @NonNull
    Activity context;
    int resource;
    @NonNull
    List<Lesson> lessons;
    public AdapterLesson(@NonNull Context context, int resource, @NonNull List<Lesson> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resource = resource;
        this.lessons = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        TextView txtNameLesson = row.findViewById(R.id.txtnamelesson);
        TextView txtDate = row.findViewById(R.id.txtdate_of_lesson);
        txtNameLesson.setText(this.lessons.get(position).getName());
        txtDate.setText(this.lessons.get(position).getDate());
        return row;

    }
}
