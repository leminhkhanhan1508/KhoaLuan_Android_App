package com.example.attendence.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendence.Model.RequestJoinCourse;
import com.example.attendence.Model.ResponseMessage;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinCourseActivity extends AppCompatActivity {
    String scanData;
    RequestJoinCourse request ;
    Button btnJoin;
    EditText txtCoursecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_course);

        addControls();
        addEvent();


    }

    private void addEvent() {
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.setCodeCourse(txtCoursecode.getText().toString());
                if (request.getKeyCourse() != null) {
                    JoinCourse(request);
                }

            }
        });

    }

    private void JoinCourse(RequestJoinCourse requestJoinCourse) {

        AppServiceFactory.getInstance();
        Call<ResponseMessage> JoinCourse = AppServiceFactory.getAppService().JoinCourse(requestJoinCourse);
        JoinCourse.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), response.body().getMassage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.d("Thất Bai", "fdsgdsgdeg");
            }
        });
    }

    private void addControls() {
        Intent intent = getIntent();
        btnJoin = findViewById(R.id.btnJoinCourse);

        scanData = intent.getStringExtra("request");
        txtCoursecode = findViewById(R.id.txtCodeCourse);
        SharedPreferences prefs = getSharedPreferences("Info_User", MODE_PRIVATE);
        String userCode = prefs.getString("UserCode", null);
        request=new RequestJoinCourse();
        request.setKeyCourse(scanData);
        request.setCodeUser(userCode);

    }
}