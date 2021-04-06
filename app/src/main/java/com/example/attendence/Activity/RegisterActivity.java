package com.example.attendence.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.attendence.Model.RequestRegister;
import com.example.attendence.Model.ResponseMessage;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText txtUserName, txtPassword, txtUserCode, txtName, txtEmail, txtMajor;
    TextView txtLogIn;
    TextView txt_notification_no_typeuser, txt_notification_no_UserName,
            txt_notification_no_Password, txt_notification_no_Usercode,
            txt_notification_no_Name, txt_notification_no_Email, txt_notification_no_Major;
    RadioButton rbtnStudent, rbtnLecturer;
    RequestRegister requestRegister;
    Button btnRegister;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControl();
        addEvent();

    }

    private void addEvent() {
        txtLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //request to re-enter the data when the information is not correct
                if (txtUserName.getText().toString().equals("")) {
//                    Log.d("đâs",requestRegister.getName());
                    txt_notification_no_UserName.setText("***Enter information");
//                    txt_notification_no_UserName.setTextColor(R.color.red_btn_bg_color);
                }
                if (txtPassword.getText().toString().equals("")) {
                    txt_notification_no_Password.setText("***Enter information");
//                    txt_notification_no_Password.setTextColor(R.color.red_btn_bg_color);
                }
                if (txtName.getText().toString().equals("")) {
                    txt_notification_no_Usercode.setText("***Enter information");
//                    txt_notification_no_Usercode.setTextColor(R.color.red_btn_bg_color);
                }
                if (txtUserCode.getText().toString().equals("")) {
                    txt_notification_no_Email.setText("***Enter information");
//                    txt_notification_no_Email.setTextColor();
                }
                if (txtMajor.getText().toString().equals("")) {
                    txt_notification_no_Name.setText("***Enter information");
//                    txt_notification_no_Name.setTextColor(R.color.red_btn_bg_color);
                }
                if (txtEmail.getText().toString().equals("")) {
                    txt_notification_no_Major.setText("***Enter information");
//                    txt_notification_no_Major.setTextColor(R.color.red_btn_bg_color);
                }
//                Log.d("fdsf",String.valueOf(rbtnLecturer.isChecked()));
                if (rbtnStudent.isChecked()==false &&rbtnLecturer.isChecked()==false) {
                    txt_notification_no_typeuser.setText("***Enter information");
//                    txt_notification_no_Major.setTextColor(R.color.red_btn_bg_color);
                }


                RegisterAccount();
            }
        });


    }

    private void RegisterAccount() {
        requestRegister = new RequestRegister();
        requestRegister.setUserName(txtUserName.getText().toString());
        requestRegister.setCodeUser(txtUserCode.getText().toString());
        requestRegister.setEmail(txtEmail.getText().toString());
        requestRegister.setMajor(txtMajor.getText().toString());
        requestRegister.setPassword(txtPassword.getText().toString());
        requestRegister.setName(txtName.getText().toString());
        String type = "student";
        if (rbtnLecturer.isChecked()) {
            type = "lectuter";
        }

        requestRegister.setUserType(type);

//create a  account
        AppServiceFactory.getInstance();
        final Call<ResponseMessage> RegisterAccount = AppServiceFactory.getAppService().Register(requestRegister);
        RegisterAccount.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.code() == 200) {
                        Toast.makeText(RegisterActivity.this, response.body().getMassage().toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.d("Retrofit", "Lỗi ");


            }
        });


    }

    private void addControl() {
        getSupportActionBar().hide();
        txt_notification_no_UserName = findViewById(R.id.notification_no_username);
        txt_notification_no_Password = findViewById(R.id.notification_no_password);
        txt_notification_no_Name = findViewById(R.id.notification_no_name);
        txt_notification_no_Usercode = findViewById(R.id.notification_no_usercode);
        txt_notification_no_Email = findViewById(R.id.notification_no_mail);
        txt_notification_no_Major = findViewById(R.id.notification_no_major);
        txt_notification_no_typeuser = findViewById(R.id.notification_no_typeuser);

        txtLogIn=findViewById(R.id.txtSign_In);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtName = findViewById(R.id.txtName);
        txtUserCode = findViewById(R.id.txtCodeUser);
        txtEmail = findViewById(R.id.txtEmail);
        txtMajor = findViewById(R.id.txtMajor);
        rbtnStudent = findViewById(R.id.rbtnStudent);
        rbtnLecturer = findViewById(R.id.rbtnLecturer);
        btnRegister = findViewById(R.id.btn_Rigister);

    }
}