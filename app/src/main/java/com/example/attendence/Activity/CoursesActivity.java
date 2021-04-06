package com.example.attendence.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.attendence.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoursesActivity extends AppCompatActivity {
    String userType;

    //create view RecyclerView for Course
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtcodecourse, txtCourseName, txtLecturer, txtWeek;
        ProgressBar progressBar_Week;

        public ViewHolder(View itemView) {
            super(itemView);
            txtcodecourse = (TextView) itemView.findViewById(R.id.txtCodeCourse);
            txtCourseName = (TextView) itemView.findViewById(R.id.txtNameCourse);
            txtLecturer = (TextView) itemView.findViewById(R.id.txtLecturer);
            txtWeek = itemView.findViewById(R.id.txtWeek);
            progressBar_Week = itemView.findViewById(R.id.progressBarWeek);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        SharedPreferences prefs = getSharedPreferences("Info_User", Context.MODE_PRIVATE);
        userType = prefs.getString("UserType", null);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_title_actionbar_listcourses_fragment);
            View view_actionbar_course = getSupportActionBar().getCustomView();
            TextView txttitle_ac_course = view_actionbar_course.findViewById(R.id.txtTitleActionbar);
            txttitle_ac_course.setText("My Courses");
            ImageButton imageButton_Add = view_actionbar_course.findViewById(R.id.ibtnAdd);
            imageButton_Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add();
                }
            });
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(new ListCourse_Fragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.item_profile:

                        fragment = new Profile_Fragment();

                        loadFragment(fragment);
                        getSupportActionBar().setDisplayShowCustomEnabled(true);
                        getSupportActionBar().setCustomView(R.layout.custom_title_actionbar_profile);
                        View view = getSupportActionBar().getCustomView();
                        TextView txttitle = view.findViewById(R.id.txtTitleActionbar);
                        txttitle.setText("Profile");



                        return true;
                    case R.id.item_courses:
                        fragment = new ListCourse_Fragment();

                        loadFragment(fragment);
                        if (getSupportActionBar() != null) {

                            getSupportActionBar().setDisplayShowCustomEnabled(true);
                            getSupportActionBar().setCustomView(R.layout.custom_title_actionbar_listcourses_fragment);
                            View view_actionbar_course = getSupportActionBar().getCustomView();
                            TextView txttitle_ac_course = view_actionbar_course.findViewById(R.id.txtTitleActionbar);
                            txttitle_ac_course.setText("My Courses");
                            ImageButton imageButton_Add = view_actionbar_course.findViewById(R.id.ibtnAdd);
                            imageButton_Add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    add();
                                }
                            });
                        }

                        return true;

                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void add() {
        if (userType.equals("student")) {
            Intent intent = new Intent(CoursesActivity.this, CameraScanActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(CoursesActivity.this, NewCourseActivity.class);
            startActivity(intent);
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_course_activity, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//


}