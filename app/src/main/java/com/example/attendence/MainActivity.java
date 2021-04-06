package com.example.attendence;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.attendence.Activity.LoginActivity;
import com.example.attendence.Activity.OCR_Activity;
import com.example.attendence.Activity.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    Button btn_Login;
    Button btn_Register;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_Login = (Button) findViewById(R.id.btn_Login);
        btn_Register=findViewById(R.id.btn_Rigister);
        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_Login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent_Login);
            }
        });
//        btn_Register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_Register=new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent_Register);
//            }
//        });
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Register=new Intent(MainActivity.this, OCR_Activity.class);
                startActivity(intent_Register);
            }
        });
    }


}