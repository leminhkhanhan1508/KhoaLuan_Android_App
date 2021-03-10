package com.example.attendence.Activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
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
    EditText txtUserName,txtPassword,txtUserCode,txtName,txtEmail,txtMajor;
    RadioButton rbtnStudent,rbtnLecturer;
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
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterAccount();
            }
        });



    }

    private void RegisterAccount() {
        requestRegister=new RequestRegister();
        requestRegister.setUserName(txtUserName.getText().toString());
        requestRegister.setCodeUser(txtUserCode.getText().toString());
        requestRegister.setEmail(txtEmail.getText().toString());
        requestRegister.setMajor(txtMajor.getText().toString());
        requestRegister.setPassword(txtPassword.getText().toString());
        requestRegister.setName(txtName.getText().toString());
        String type="student";
        if(rbtnLecturer.isChecked())
        {
            type="lectuter";
        }
        requestRegister.setUserType(type);

        AppServiceFactory.getInstance();
        final Call<ResponseMessage> RegisterAccount=AppServiceFactory.getAppService().Register(requestRegister);
        RegisterAccount.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
               try {
                   if(response.code()==200)
                   {
                       Toast.makeText(RegisterActivity.this,response.body().getMassage().toString(),Toast.LENGTH_LONG).show();
                   }
               }catch (Exception e)
               {
                   Log.d("Exception",e.getMessage());
               }

            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.d("Retrofit","Lỗi ");


            }
        });


    }

    private void addControl() {
        getSupportActionBar().hide();
        txtUserName=findViewById(R.id.txtUserName);
        txtPassword=findViewById(R.id.txtPassword);
        txtName=findViewById(R.id.txtName);
        txtUserCode=findViewById(R.id.txtCodeUser);
        txtEmail=findViewById(R.id.txtEmail);
        txtMajor=findViewById(R.id.txtMajor);
        rbtnStudent=findViewById(R.id.rbtnStudent);
        rbtnLecturer=findViewById(R.id.rbtnLecturer);
        btnRegister=findViewById(R.id.btn_Rigister);

    }
}