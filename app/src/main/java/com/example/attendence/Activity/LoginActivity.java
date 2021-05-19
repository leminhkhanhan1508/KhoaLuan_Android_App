package com.example.attendence.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.example.attendence.Model.Course;
import com.example.attendence.Model.RequestLogin;
import com.example.attendence.Model.ResponseLogin;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtUserName,txtPassword;
    TextView txtForgotPassword;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionBar=getSupportActionBar();
        actionBar.hide();
        txtForgotPassword=findViewById(R.id.tv_FogotPassword_log);
        txtUserName=findViewById(R.id.txtUserName);
        txtPassword=findViewById(R.id.txtPassword);
        btnLogin=findViewById(R.id.btn_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog loading...
                final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Login();
                        pDialog.cancel();
                    }
                },1000);

                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading ...");
                pDialog.setCancelable(true);
                pDialog.show();


            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    //Call retrofit Login
    private void Login() {
        RequestLogin requestLogin=new RequestLogin();
        requestLogin.setUserName(txtUserName.getText().toString());
        requestLogin.setPassword(txtPassword.getText().toString());

        AppServiceFactory.getInstance();
        Call<ResponseLogin> Login=AppServiceFactory.getAppService().Login(requestLogin);
        Login.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if(response.code()==200)
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("Info_User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserCode",response.body().getCodeUser());
                    editor.putString("UserType",response.body().getUserType());
                    editor.apply();
                    List<Course> listCourse=new ArrayList<>();
                    listCourse.addAll(response.body().getCourses());

                    Intent intent=new Intent(LoginActivity.this, CoursesActivity.class);
                    intent.putExtra("listcourse", (Serializable) listCourse);

                    startActivity(intent);

                }
                if(response.code()==404){
                    Toast.makeText(LoginActivity.this,"Sai thông tin đăng nhập",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.d("Lỗi retrofit login","Lỗi");

            }
        });

    }
}