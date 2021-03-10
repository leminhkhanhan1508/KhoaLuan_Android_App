package com.example.attendence.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import com.example.attendence.Adapter.AdapterCourse;
import com.example.attendence.Model.Course;
import com.example.attendence.Model.Lesson;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;
import github.com.st235.lib_expandablebottombar.ExpandableBottomBarMenuItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.util.*;

public class CoursesActivity extends AppCompatActivity {
    //    ListView lvCourses;
    ArrayList<Course> listCourse;
    //    AdapterCourse adapterCourse;
    //    com.google.android.material.floatingactionbutton.FloatingActionButton btnNewCourse;
    String userType;
    private ActionBar actionBar;
    //
    private RecyclerView mRecyclerView;

    //date
    private Calendar calendar, calendar1, calendar2;
    private SimpleDateFormat dateFormat;

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
        addControls();
        addEvents();
        final ParallaxRecyclerAdapter<Course> adapter = new ParallaxRecyclerAdapter<Course>(listCourse) {


            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<Course> parallaxRecyclerAdapter, int i) {
                ((ViewHolder) viewHolder).txtcodecourse.setText(parallaxRecyclerAdapter.getData().get(i).getCode());
                ((ViewHolder) viewHolder).txtCourseName.setText(parallaxRecyclerAdapter.getData().get(i).getName());
                ((ViewHolder) viewHolder).txtLecturer.setText(parallaxRecyclerAdapter.getData().get(i).getLecturer());

                Date atStart, atEnd;
                calendar = Calendar.getInstance();
                calendar1 = Calendar.getInstance();
                calendar2 = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                int week_start_course, week_end_course;
                try {
                    atStart = dateFormat.parse(parallaxRecyclerAdapter.getData().get(i).getStart());
                    calendar.setTime(atStart);
                    week_start_course = calendar.get(Calendar.WEEK_OF_YEAR);
                    atEnd = dateFormat.parse(parallaxRecyclerAdapter.getData().get(i).getEnd());
                    calendar.setTime(atEnd);
                    week_end_course = calendar.get(Calendar.WEEK_OF_YEAR);


                    int week_now;
                    week_now = calendar1.get(Calendar.WEEK_OF_YEAR);
                    ((ViewHolder) viewHolder).progressBar_Week.setMin(0);
                    ((ViewHolder) viewHolder).progressBar_Week.setMax(week_end_course - week_start_course);
                    if (week_now >= week_start_course) {
                        if (week_now > week_end_course) {

                            ((ViewHolder) viewHolder).txtWeek.setText("Khóa Học đã kết thúc");
                            ((ViewHolder) viewHolder).progressBar_Week.setProgress(week_end_course - week_start_course);
                        } else {
                            int week = (week_now - week_start_course) + 1;

                            ((ViewHolder) viewHolder).txtWeek.setText("Tuần: " + String.valueOf(week));
                            ((ViewHolder) viewHolder).progressBar_Week.setProgress(week);
                        }

                    } else {
                        ((ViewHolder) viewHolder).txtWeek.setText("Tuần: 0 ");
                        ((ViewHolder) viewHolder).progressBar_Week.setProgress(0);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<Course> parallaxRecyclerAdapter, int i) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.item_mycourses, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<Course> parallaxRecyclerAdapter) {
                return listCourse.size();
            }

//            @Override
//            public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
//                super.setOnParallaxScroll(parallaxScroll);
//                Drawable c = actionBar.getCustomView().getBackground();
//                c.setAlpha(Math.round(2 * 255));
//                actionBar.getCustomView().setBackground(c);
//            }

//            @Override
//            public void onParallaxScroll(float percentage, float offset, View parallax) {
//                Drawable c = actionBar.getCustomView().getBackground();
//                c.setAlpha(Math.round(percentage * 255));
//                actionBar.getCustomView().setBackground(c);
//            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {


            @Override
            public void onClick(View view, int position) {
                Intent intentCourse = new Intent(CoursesActivity.this, LessonActivity.class);
                ArrayList<Lesson> listLesson = new ArrayList<>();
                listLesson.addAll(listCourse.get(position).getLessons());
                String nameCourse = listCourse.get(position).getName();
                String codeCourse = listCourse.get(position).getCode();

                intentCourse.putExtra("listLesson", listLesson);
                intentCourse.putExtra("codeCourse", codeCourse);
                intentCourse.putExtra("nameCourse", nameCourse);

                startActivity(intentCourse);
            }
        });

        HeaderLayoutManagerFixed layoutManagerFixed = new HeaderLayoutManagerFixed(this);
        mRecyclerView.setLayoutManager(layoutManagerFixed);
        View header = getLayoutInflater().inflate(R.layout.my_header, mRecyclerView, false);
        layoutManagerFixed.setHeaderIncrementFixer(header);
        adapter.setShouldClipView(false);
        adapter.setParallaxHeader(header, mRecyclerView);
//        adapter.setData(content);
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }

            case R.id.mnuadd: {
                add();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
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

    private void addEvents() {
//        GetCourses();
        Intent intent = getIntent();
        listCourse.addAll((Collection<? extends Course>) intent.getSerializableExtra("listcourse"));

//        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intentCourse = new Intent(CoursesActivity.this, LessonActivity.class);
//                ArrayList<Lesson> listLesson = new ArrayList<>();
//                listLesson.addAll(listCourse.get(position).getLessons());
//                String nameCourse = listCourse.get(position).getName();
//                String codeCourse = listCourse.get(position).getCode();
//
//                intentCourse.putExtra("listLesson", listLesson);
//                intentCourse.putExtra("codeCourse", codeCourse);
//                intentCourse.putExtra("nameCourse", nameCourse);
//
//                startActivity(intentCourse);
//            }
//        });

//        btnNewCourse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (userType.equals("student")) {
//                    Intent intent = new Intent(CoursesActivity.this, CameraScanActivity.class);
//                    startActivity(intent);
//
//                } else {
//                    Intent intent = new Intent(CoursesActivity.this, NewCourseActivity.class);
//                    startActivity(intent);
//                }
//
//            }
//        });

    }


    private void addControls() {


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        actionBar = getSupportActionBar();
        if (actionBar != null) {
//actionBar.hide();

            actionBar.setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_title_actionbar);
            View view = getSupportActionBar().getCustomView();
            TextView txttitle = view.findViewById(R.id.txtTitleActionbar);
            txttitle.setText("My Courses");
        }
//        lvCourses = findViewById(R.id.lv_MyCourses);
        listCourse = new ArrayList<>();
//        adapterCourse = new AdapterCourse(CoursesActivity.this, R.layout.item_mycourses, listCourse);
//        lvCourses.setAdapter(adapterCourse);
        //


//        btnNewCourse = findViewById(R.id.fab);
        SharedPreferences prefs = getSharedPreferences("Info_User", MODE_PRIVATE);
        userType = prefs.getString("UserType", null);
    }

    //call from retrofit
//    private void GetCourses() {
//
//        AppServiceFactory.getInstance();
//        Call<List<Course>> GetCourses = AppServiceFactory.getAppService().GetCourses();
//        GetCourses.enqueue(new Callback<List<Course>>() {
//            @Override
//            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
//                if (response.code() == 200)
//                    listCourse.addAll(response.body());
////                adapterCourse.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Course>> call, Throwable t) {
//                Log.d("Thất bại", "Oh No");
//
//            }
//        });
//    }
}