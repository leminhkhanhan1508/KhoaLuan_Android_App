package com.example.attendence.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.example.attendence.Adapter.AdapterLesson;
import com.example.attendence.Model.*;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class LessonActivity extends AppCompatActivity {
    ListView lvLesson;
    ArrayList<Lesson> listLesson;
    AdapterLesson adapterLesson;
    TextView txtNameCourse;
    String codeCourse, userCode;
    ArrayList<ResponseImage> listUrl;


    //    Button btnExport;
    ActionBar actionBar;


    //    date picker
    Calendar calendar = Calendar.getInstance();
    java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        addControls();
        addEvents();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void ExportFile() {
        final RequestInfoCourse requestInfoCourse = new RequestInfoCourse();
        requestInfoCourse.setCodeCourse(codeCourse);
        AppServiceFactory.getInstance();
        Call<List<ResponseInfoLesson>> GetInfoCourse = AppServiceFactory.getAppService().GetInfoCourse(requestInfoCourse);
        GetInfoCourse.enqueue(new Callback<List<ResponseInfoLesson>>() {
            @Override
            public void onResponse(Call<List<ResponseInfoLesson>> call, Response<List<ResponseInfoLesson>> response) {
                if (response.code() == 200) {
                    //generate data
                    StringBuilder data = new StringBuilder();
                    data.append("Id,MSSV,Date,Note");
                    for (int i = 0; i < response.body().size(); i++) {
                        data.append("\n" + String.valueOf(i) + "," + response.body().get(i).getCodeStudent() + ","
                                + response.body().get(i).getDate() + "," + response.body().get(i).getNote());
                    }
                    try {
                        //saving the file into device
                        FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                        out.write((data.toString()).getBytes());
                        out.close();

                        //exporting
                        Context context = getApplicationContext();
                        File filelocation = new File(getFilesDir(), "data.csv");
                        Uri path = FileProvider.getUriForFile(context, "com.example.attendence.fileprovider", filelocation);
                        Intent fileIntent = new Intent(Intent.ACTION_SEND);
                        fileIntent.setType("text/csv");
                        fileIntent.putExtra(Intent.EXTRA_SUBJECT, requestInfoCourse.getCodeCourse());
                        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                        startActivity(Intent.createChooser(fileIntent, "Send mail"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResponseInfoLesson>> call, Throwable t) {

            }
        });
    }

    private void addEvents() {


        final Intent intentLesson = new Intent(LessonActivity.this, ImageActivity.class);

        lvLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestImage requestImage = new RequestImage();
                requestImage.setDateAttend(listLesson.get(position).getDate());
                requestImage.setCodeCourse(codeCourse);
                requestImage.setCodeStudent(userCode);
                intentLesson.putExtra("codeCourse", codeCourse);
                intentLesson.putExtra("date", listLesson.get(position).getDate());
                intentLesson.putExtra("userCode", userCode);
                AppServiceFactory.getInstance();
                Call<List<ResponseImage>> GetUrlImage = AppServiceFactory.getAppService().GetImage(requestImage);
                GetUrlImage.enqueue(new Callback<List<ResponseImage>>() {
                    @Override
                    public void onResponse(Call<List<ResponseImage>> call, Response<List<ResponseImage>> response) {
                        if (response.code() == 200) {
                            //xóa bộ nhớ đệm của listUrl;
                            listUrl.clear();
                            listUrl.addAll(response.body());

                            intentLesson.putExtra("listUrl", listUrl);

                            startActivity(intentLesson);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<ResponseImage>> call, Throwable t) {

                    }
                });

            }
        });


    }


    private void NewLesson() {
        AlertDialog.Builder newLessonDialog = new AlertDialog.Builder(LessonActivity.this);

        //LayoutInflater là đọc xml layout file và chuyển đổi các thuộc tính của nó thành 1 View
        LayoutInflater inflater = getLayoutInflater();
        View newLessonView = inflater.inflate(R.layout.itemdialognewlesson, null);
        newLessonDialog.setView(newLessonView);
        final AlertDialog alert = newLessonDialog.create();
        final EditText txtdate, txtname;
        txtdate = newLessonView.findViewById(R.id.txtdate_of_lesson);
        txtname = newLessonView.findViewById(R.id.txtnamelesson);
        Button btnCancel, btnAccept;
        btnCancel = newLessonView.findViewById(R.id.btnCancel);
        btnAccept = newLessonView.findViewById(R.id.btnAccept);

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        txtdate.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(LessonActivity.this,
                        callBack,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        final RequestNewLesson requestNewLesson = new RequestNewLesson();
        requestNewLesson.setCodeCourse(codeCourse);
        requestNewLesson.setCodeUser(userCode);


//        if (txtdate.getText().toString() == "") {
//            txtdate.setHint(R.string.hintnovalue);
//            txtdate.setHintTextColor(R.color.colorPrimaryLight);
//            return;
//        } else {
//            requestNewLesson.setDateLesson(txtdate.getText().toString());
//        }
//        if (txtname.getText().toString() == "") {
//            txtname.setHint(R.string.hintnovalue);
//            txtname.setHintTextColor(R.color.colorPrimaryLight);
//            return;
//        } else {
//            requestNewLesson.setNameLesson(txtname.getText().toString());
//        }
//                Log.d("inf", requestNewLesson.getCodeCourse() + requestNewLesson.getNameLesson() + requestNewLesson.getDateLesson() + requestNewLesson.getCodeUser());
//        requestNewLesson.setDateLesson("2021-01-20");
//        requestNewLesson.setNameLesson("Buôi Test App");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNewLesson.setDateLesson(txtdate.getText().toString());
                requestNewLesson.setNameLesson(txtname.getText().toString());
                AppServiceFactory.getInstance();
                Call<ResponseMessage> NewLesson = AppServiceFactory.getAppService().NewLesson(requestNewLesson);
                NewLesson.enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        try {
                            if (response.code() == 200) {
                                Toast.makeText(LessonActivity.this, response.body().getMassage(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.d("Lỗi", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {

                    }
                });
                alert.cancel();

            }
        });

        alert.show();


    }

    private void addControls() {

        lvLesson = findViewById(R.id.lv_mylessons);
        listLesson = new ArrayList<>();
        adapterLesson = new AdapterLesson(LessonActivity.this, R.layout.item_mylesssons, listLesson);
        lvLesson.setAdapter(adapterLesson);
        Intent intentCourse = getIntent();
        listLesson.addAll((Collection<? extends Lesson>) intentCourse.getSerializableExtra("listLesson"));
        adapterLesson.notifyDataSetChanged();
//        txtNameCourse = findViewById(R.id.txtnamecourse);

//        txtNameCourse.setText(intentCourse.getStringExtra("nameCourse"));

        codeCourse = intentCourse.getStringExtra("codeCourse");
        SharedPreferences prefs = getSharedPreferences("Info_User", MODE_PRIVATE);
        userCode = prefs.getString("UserCode", null);
        String user_Type = prefs.getString("UserType", null);
        if(user_Type.equals("lecturer"))
        {
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_actionbar_lesson_activity);
            View view_actionbar_lesson = getSupportActionBar().getCustomView();
            TextView txtTitle=view_actionbar_lesson.findViewById(R.id.txtTitleActionbar);
            ImageView imgAdd=view_actionbar_lesson.findViewById(R.id.img_add);
            ImageView imgExport=view_actionbar_lesson.findViewById(R.id.imgExport);
            txtTitle.setText(codeCourse);
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewLesson();
                }
            });
            imgExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExportFile();
                }
            });
        }
        else {
            getSupportActionBar().setTitle(codeCourse);
        }
        listUrl = new ArrayList<>();
//        btnNewLesson = findViewById(R.id.fablesson);
        actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}