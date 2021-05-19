package com.example.attendence.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.attendence.Model.Course;
import com.example.attendence.Model.Lesson;
import com.example.attendence.R;
import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListCourse_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListCourse_Fragment extends Fragment {
    private RecyclerView mRecyclerView;
    //    ListView lvCourses;
    ArrayList<Course> listCourse;
    //    AdapterCourse adapterCourse;
    //    com.google.android.material.floatingactionbutton.FloatingActionButton btnNewCourse;

    private ActionBar actionBar;
    //
//    private RecyclerView mRecyclerView;

    //date
    private Calendar calendar, calendar1, calendar2;
    private SimpleDateFormat dateFormat;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListCourse_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListCourse_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListCourse_Fragment newInstance(String param1, String param2) {
        ListCourse_Fragment fragment = new ListCourse_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_course, container, false);
        mRecyclerView = view.findViewById(R.id.recycler);
        addControls();
        addEvents();

        final ParallaxRecyclerAdapter<Course> adapter = new ParallaxRecyclerAdapter<Course>(listCourse) {


            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<Course> parallaxRecyclerAdapter, int i) {
                ((CoursesActivity.ViewHolder) viewHolder).txtcodecourse.setText(parallaxRecyclerAdapter.getData().get(i).getCode());
                ((CoursesActivity.ViewHolder) viewHolder).txtCourseName.setText(parallaxRecyclerAdapter.getData().get(i).getName());
                ((CoursesActivity.ViewHolder) viewHolder).txtLecturer.setText(parallaxRecyclerAdapter.getData().get(i).getLecturer());

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
                    ((CoursesActivity.ViewHolder) viewHolder).progressBar_Week.setMin(0);
                    ((CoursesActivity.ViewHolder) viewHolder).progressBar_Week.setMax(week_end_course - week_start_course);
                    if (week_now >= week_start_course) {
                        if (week_now > week_end_course) {

                            ((CoursesActivity.ViewHolder) viewHolder).txtWeek.setText("Khóa Học đã kết thúc");
                            ((CoursesActivity.ViewHolder) viewHolder).progressBar_Week.setProgress(week_end_course - week_start_course);
                        } else {
                            int week = (week_now - week_start_course) + 1;

                            ((CoursesActivity.ViewHolder) viewHolder).txtWeek.setText("Tuần: " + String.valueOf(week));
                            ((CoursesActivity.ViewHolder) viewHolder).progressBar_Week.setProgress(week);
                        }

                    } else {
                        ((CoursesActivity.ViewHolder) viewHolder).txtWeek.setText("Tuần: 0 ");
                        ((CoursesActivity.ViewHolder) viewHolder).progressBar_Week.setProgress(0);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<Course> parallaxRecyclerAdapter, int i) {
                return new CoursesActivity.ViewHolder(getLayoutInflater().inflate(R.layout.item_mycourses, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<Course> parallaxRecyclerAdapter) {
                return listCourse.size();
            }


        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {


            @Override
            public void onClick(View view, int position) {
                Intent intentCourse = new Intent(getActivity(), LessonActivity.class);
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

        HeaderLayoutManagerFixed layoutManagerFixed = new HeaderLayoutManagerFixed(getActivity());
        mRecyclerView.setLayoutManager(layoutManagerFixed);
        View header = getLayoutInflater().inflate(R.layout.my_header, mRecyclerView, false);
        layoutManagerFixed.setHeaderIncrementFixer(header);
        adapter.setShouldClipView(false);
        adapter.setParallaxHeader(header, mRecyclerView);
//        adapter.setData(content);
        mRecyclerView.setAdapter(adapter);


        return view;
    }
    private void addControls() {


//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


        listCourse = new ArrayList<>();






    }

    private void addEvents() {
//        GetCourses();
        Intent intent = this.getActivity().getIntent();
        listCourse.addAll((Collection<? extends Course>) intent.getSerializableExtra("listcourse"));

//

    }

}