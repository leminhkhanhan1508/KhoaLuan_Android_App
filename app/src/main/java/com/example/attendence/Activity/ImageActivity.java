package com.example.attendence.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.attendence.Adapter.BitMap_Adapter;
import com.example.attendence.Model.*;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.attendence.R.drawable.people;

public class ImageActivity extends AppCompatActivity {
    TextView txtCodeCourse, txtDate, txtCodeStudent;
    ImageView imgCheck;
    String url;
    Bitmap imageCheck;
    String userCode;
    ArrayList<ResponseImage> listUrl;
    ArrayList<ResponseInfoLesson> listInfo;


    ArrayList<Bitmap> listBitmap;
    BitMap_Adapter bitMap_adapter;
    GridView gridView;


    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        addControls();
        addEvents();
//        clearData();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_infor_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.mnuexport:
                export();

        }
        return super.onOptionsItemSelected(item);
    }

    private void clearData() {
        //xóa bộ nhớ đệm
        listUrl.clear();
        listBitmap.clear();
    }

    public void export() {
        RequestInfoLesson requestInfoLesson=new RequestInfoLesson();

        requestInfoLesson.setCodeCourse(txtCodeCourse.getText().toString());
        requestInfoLesson.setDateAttend(txtDate.getText().toString());
        AppServiceFactory.getInstance();
        Call<List<ResponseInfoLesson>>GetInfoLesson=AppServiceFactory.getAppService().GetInfoLesson(requestInfoLesson);
        GetInfoLesson.enqueue(new Callback<List<ResponseInfoLesson>>() {
            @Override
            public void onResponse(Call<List<ResponseInfoLesson>> call, Response<List<ResponseInfoLesson>> response) {
                if(response.code()==200)
                {
                    listInfo.addAll(response.body());

                    //generate data
                    StringBuilder data = new StringBuilder();
                    data.append("Id,MSSV,Date,Note");
                    for (int i = 0; i < listInfo.size(); i++) {
                        data.append("\n" + String.valueOf(i) + "," + listInfo.get(i).getCodeStudent()+","+
                                listInfo.get(i).getDate()+","+response.body().get(i).getNote());
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
                        fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
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
        if(listUrl.size()<=0)
            imgCheck.setImageResource(R.drawable.people);


        if (listUrl.size() == 1) {
            getImage(listUrl.get(0).getUrlImage());
            listUrl.clear();

        } else {
            getListImage();
            imgCheck.isEnabled();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create dialog to show image
                AlertDialog.Builder imagedialog = new AlertDialog.Builder(ImageActivity.this);
                //LayoutInflater là đọc xml layout file và chuyển đổi các thuộc tính của nó thành 1 View
                LayoutInflater inflater = getLayoutInflater();
                View imageView = inflater.inflate(R.layout.itemdialog, null);
                ImageView imageView1 = imageView.findViewById(R.id.imgImageDialog);
                imageView1.setImageBitmap(listBitmap.get(position));
                Button btnReport,btnAccept;
                imagedialog.setView(imageView);
                final AlertDialog alert = imagedialog.create();
                btnReport=imageView.findViewById(R.id.btnReport);
                btnAccept=imageView.findViewById(R.id.btnAccept);
                final int idImage=listUrl.get(position).getId();
                final RequestReportImage requestReportImage=new RequestReportImage();
                requestReportImage.setNote("MSSV sai,MSSV sinh vien la :"+txtCodeStudent.getText().toString());
                final RequestRenameImage requestRenameImage=new RequestRenameImage();
                requestRenameImage.setCodeStudent(txtCodeStudent.getText().toString());
                btnReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppServiceFactory.getInstance();
                        Call<ResponseMessage> ReportImage=AppServiceFactory.getAppService().ReportImage(idImage,requestReportImage);
                        ReportImage.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                if(response.code()==200){
                                    Toast.makeText(ImageActivity.this,response.body().getMassage(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                Log.d("loi","Loi");
                            }
                        });
                        alert.cancel();
                    }
                });
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppServiceFactory.getInstance();

                        Call<ResponseMessage>RenameImage = AppServiceFactory.getAppService().RenameImage(idImage,requestRenameImage);
                        RenameImage.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                if(response.code()==200){
                                    Toast.makeText(ImageActivity.this,response.body().getMassage(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                Log.d("loi","Loi");

                            }
                        });
                        alert.cancel();
                    }
                });

                alert.show();


            }
        });


    }


    private void SetTitle() {
        Intent intent = getIntent();
        txtCodeCourse.setText(intent.getStringExtra("codeCourse"));
        txtCodeStudent.setText(intent.getStringExtra("userCode"));
        txtDate.setText(intent.getStringExtra("date"));
        listUrl.addAll((Collection<? extends ResponseImage>) intent.getSerializableExtra("listUrl"));
        Log.d("listUrl", listUrl.get(0).getUrlImage());
    }

    private void addControls() {
        actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_inf_attendance);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        txtCodeCourse = findViewById(R.id.txtcodecourse);
        txtCodeStudent = findViewById(R.id.txtcodestudent);
        txtDate = findViewById(R.id.txtdate_of_lesson);
        imgCheck = findViewById(R.id.imgCheckImage);

        listUrl = new ArrayList<>();
        listBitmap = new ArrayList<>();
        bitMap_adapter = new BitMap_Adapter(ImageActivity.this, R.layout.item_bitmap, listBitmap);
        gridView = findViewById(R.id.gvImage);
        gridView.setAdapter(bitMap_adapter);
//        SetTitle();
        Intent intent = getIntent();
        txtCodeCourse.setText(intent.getStringExtra("codeCourse"));
        txtCodeStudent.setText(intent.getStringExtra("userCode"));
        txtDate.setText(intent.getStringExtra("date"));
        listUrl.addAll((Collection<? extends ResponseImage>) intent.getSerializableExtra("listUrl"));


//        btnExport=findViewById(R.id.btnExport);
        listInfo=new ArrayList<>();



    }

    private void getImage(final String urlimage) {
        final Thread threadgetImage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //load Image from listURL
                    String image = "http://192.168.1.104:8000" + urlimage;
                    //create URL of Image
                    URL url = new URL(image);
                    //create bitmap to load Image from URL
                    final Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    //add image in listImage
                    imageCheck = bitmap;
                    imgCheck.post(new Runnable() {
                        @Override
                        public void run() {
                            imgCheck.setImageBitmap(imageCheck);
                        }
                    });

                    //use Runnable to change interface of ListImageActivity


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        threadgetImage.start();
    }

    private void getListImage() {
        final Thread threadgetImage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //load Image from listURL
                    for (int i = 0; i < listUrl.size(); i++) {
                        //create URL of Image
                        URL url = new URL("http://192.168.1.104:8000" + listUrl.get(i).getUrlImage());
                        //create bitmap to load Image from URL
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        //add image in listImage
                        listBitmap.add(bitmap);
                    }

                    //use Runnable to change interface of ListImageActivity
                    gridView.post(new Runnable() {
                        @Override
                        public void run() {
                            //Notice there is a change for Adapter
                            bitMap_adapter.notifyDataSetChanged();
                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        threadgetImage.start();
    }
}