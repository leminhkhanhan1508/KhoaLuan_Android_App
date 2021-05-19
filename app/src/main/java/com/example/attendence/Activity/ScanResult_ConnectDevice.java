package com.example.attendence.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.attendence.R;

import java.io.*;
import java.net.Socket;

public class ScanResult_ConnectDevice extends AppCompatActivity {
    Button btnSend,btnDisconnect;
    String data, response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Connect Device");
        setContentView(R.layout.activity_scan_result__connect_device);
        addControl();
        addEvent();
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



    private void addEvent() {
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtDisconnect="DE"+data.substring(2);
                sendMessage(txtDisconnect);
                Log.d("txtdisconext",txtDisconnect);
                btnSend.setVisibility(View.VISIBLE);
                btnDisconnect.setVisibility(View.GONE);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendMessage(data);
                Toast.makeText(getApplicationContext(),"Kết nối thành công",Toast.LENGTH_LONG).show();
                btnSend.setVisibility(View.GONE);
                btnDisconnect.setVisibility(View.VISIBLE);

//                if (response != null) {
//                    Intent intent = new Intent(ScanResult_Activity.this, Main_Activity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "ket noi that bai", Toast.LENGTH_LONG).show();
//                }

                //Retrofit
//                AppServiceFactory.create();
//                Call<MessageResponse> getImage = AppServiceFactory.getAppService().getImage();
//                getImage.enqueue(new Callback<MessageResponse>() {
//                    @Override
//                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
//                        txtRequestCode.setText();
//                    }
//
//                    @Override
//                    public void onFailure(Call<MessageResponse> call, Throwable t) {
//
//                    }
//                });


            }
        });

    }

    private void addControl() {
//        txtRequestCode = findViewById(R.id.txtrequest);
        btnDisconnect=findViewById(R.id.btn_disconnect_socket);
        btnSend = findViewById(R.id.btnsendsocket);
        Intent intent = this.getIntent();
        data = intent.getStringExtra("request");
//        txtRequestCode.setText(data);


    }

    private void sendMessage(final String toString) {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Socket s = new Socket(getApplicationContext().getString(R.string.host) , Integer.parseInt(getApplicationContext().getString(R.string.port)));

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

                    output.println(toString);
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    response = input.readLine();
                    Log.d("readline", response);
                    while (true) {
                        if (input.readLine() != null) {
                            response = input.readLine();
                            Log.d("readline", "done");
                        }
                        break;
                    }


                    output.close();
                    out.close();

                    s.close();

                } catch (IOException e) {
                    // Toast.makeText(getApplicationContext(),"Lỗi socket",Toast.LENGTH_LONG).show();


                    e.printStackTrace();
                }

            }
        });
        thread.start();

    }

}