package com.example.attendence.Activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.attendence.Model.RequestNewCourse;
import com.example.attendence.Model.ResponseMessage;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewCourseActivity extends AppCompatActivity {
    EditText txtUserCode, txtCourseCode, txtName, txtSize, txtMajor, txtLecturer, txtStartAt, txtEndAt, txtKey;
    TextView txt_notification_no_coursecode, txt_notification_no_coursename,
            txt_notification_no_size, txt_notification_no_major,
            txt_notification_no_lecturer, txt_notification_no_StartAt,
            txt_notification_no_EndAt, txt_notification_no_Key;
    Button btnCreate;
    String userCode;
    private ActionBar actionBar;
    Calendar calendar = Calendar.getInstance();
    java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        addControl();
        addEvent();

    }

    private void addEvent() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCourseCode.getText().toString().equals("")) {

                    txt_notification_no_coursecode.setText("***Enter information");

                }
                if (txtName.getText().toString().equals("")) {

                    txt_notification_no_coursename.setText("***Enter information");

                }
                if (txtSize.getText().toString().equals("")) {

                    txt_notification_no_size.setText("***Enter information");

                }
                if (txtMajor.getText().toString().equals("")) {

                    txt_notification_no_major.setText("***Enter information");

                }
                if (txtLecturer.getText().toString().equals("")) {

                    txt_notification_no_lecturer.setText("***Enter information");

                }
                if (txtKey.getText().toString().equals("")) {

                    txt_notification_no_Key.setText("***Enter information");

                }
                if (txtStartAt.getText().toString().equals("")) {

                    txt_notification_no_StartAt.setText("***Enter information");

                }
                if (txtEndAt.getText().toString().equals("")) {

                    txt_notification_no_EndAt.setText("***Enter information");

                }
                RequestNewCourse requestNewCourse = new RequestNewCourse();
                requestNewCourse.setCodeUser(userCode);
                requestNewCourse.setCode(txtCourseCode.getText().toString());
                requestNewCourse.setName(txtName.getText().toString());
                if (txtSize.getText().toString().equals("") == false) {
                    requestNewCourse.setSize(Integer.parseInt(txtSize.getText().toString()));
                }
                requestNewCourse.setCodeMajor(txtMajor.getText().toString());
                requestNewCourse.setLecturer(txtLecturer.getText().toString());
                requestNewCourse.setStart(txtStartAt.getText().toString());
                requestNewCourse.setEnd(txtEndAt.getText().toString());
                requestNewCourse.setKeyCourse(txtKey.getText().toString());


                AppServiceFactory.getInstance();
                Call<ResponseMessage> CreateCourse = AppServiceFactory.getAppService().CreateCourse(requestNewCourse);
                CreateCourse.enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        if (response.code() == 200) {
                            Toast.makeText(NewCourseActivity.this, response.body().getMassage(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("Lỗi ", "Lỗi");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                        Log.d("Lỗi ", "Lỗi");

                    }
                });


            }
        });
        txtStartAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        txtStartAt.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewCourseActivity.this,
                        callBack,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        txtEndAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        txtEndAt.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewCourseActivity.this,
                        callBack,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void addControl() {
        txt_notification_no_coursecode = findViewById(R.id.notification_no_coursecode);
        txt_notification_no_coursename = findViewById(R.id.notification_no_coursename);
        txt_notification_no_size = findViewById(R.id.notification_no_size);
        txt_notification_no_major = findViewById(R.id.notification_no_major);
        txt_notification_no_lecturer = findViewById(R.id.notification_no_lecturer);
        txt_notification_no_Key = findViewById(R.id.notification_no_keycode);
        txt_notification_no_EndAt = findViewById(R.id.notification_no_EndAt);
        txt_notification_no_StartAt = findViewById(R.id.notification_no_StartAt);

        txtUserCode = findViewById(R.id.txtCodeUser);
        txtCourseCode = findViewById(R.id.txtCodeCourse);
        txtName = findViewById(R.id.txtCourseName);
        txtSize = findViewById(R.id.txtSize);
        txtMajor = findViewById(R.id.txtMajorCode);
        txtLecturer = findViewById(R.id.txtLecturer);
        txtStartAt = findViewById(R.id.txtStartAt);
        txtEndAt = findViewById(R.id.txtEndAt);
        txtKey = findViewById(R.id.txtKeyCourse);
        btnCreate = findViewById(R.id.btnCreate);
        SharedPreferences prefs = getSharedPreferences("Info_User", MODE_PRIVATE);
        userCode = prefs.getString("UserCode", null);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}