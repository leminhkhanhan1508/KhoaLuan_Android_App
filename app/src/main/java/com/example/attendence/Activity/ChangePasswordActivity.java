package com.example.attendence.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.attendence.Model.RequestChangePassword;
import com.example.attendence.Model.RequestForgotPassword;
import com.example.attendence.Model.ResponseMessage;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import com.example.attendence.databinding.ActivityChangePasswordBinding;
import com.example.attendence.databinding.ActivityOcrBinding;
import org.checkerframework.checker.units.qual.A;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    String userCode;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        addcontrols();
        addEvents();
    }


    private void addEvents() {
        binding.txtFogotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestChangePassword requestChangePassword = new RequestChangePassword();
                if (binding.txtCurrentPassword.getText().toString().equals("")) {
//                    Log.d("đâs",requestRegister.getName());
                    binding.notificationNoCurrentpassword.setText("***Enter information");
//                    txt_notification_no_UserName.setTextColor(R.color.red_btn_bg_color);
                }
                if (binding.txtNewPassword.getText().toString().equals("")) {
//                    Log.d("đâs",requestRegister.getName());
                    binding.notificationNoNewPassword.setText("***Enter information");
//                    txt_notification_no_UserName.setTextColor(R.color.red_btn_bg_color);
                }
                if (binding.txtReTypeNewPassword.getText().toString().equals("")) {
//                    Log.d("đâs",requestRegister.getName());
                    binding.notificationNoReTypeNewPassword.setText("***Enter information");
//                    txt_notification_no_UserName.setTextColor(R.color.red_btn_bg_color);
                }
                requestChangePassword.setUserName(userCode);
                requestChangePassword.setOldPassword(binding.txtCurrentPassword.getText().toString());
                if (binding.txtNewPassword.getText().toString().equals(binding.txtReTypeNewPassword.getText().toString())) {
                    requestChangePassword.setNewPassword(binding.txtNewPassword.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "New password is incorrect", Toast.LENGTH_LONG).show();
                }
                ChangePassword(requestChangePassword);
            }
        });
    }

    private void ChangePassword(RequestChangePassword requestChangePassword) {

        AppServiceFactory.getInstance();
        Call<ResponseMessage> ChangePassword = AppServiceFactory.getAppService().ChangePassword(requestChangePassword);
        ChangePassword.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMassage(), Toast.LENGTH_LONG).show();
                        if (response.body().getOk()) {
                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.d("ChangePassword", "Lỗi retrofit");
            }
        });

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

    private void addcontrols() {
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.changepassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences prefs = getSharedPreferences("Info_User", Context.MODE_PRIVATE);
        userCode = prefs.getString("UserCode", null);
    }
}