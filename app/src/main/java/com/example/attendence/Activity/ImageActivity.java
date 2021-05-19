package com.example.attendence.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ImageActivity extends AppCompatActivity {
    String txtCodeCourse, txtDate, txtCodeStudent;
    ImageView imgCheck;
    String url;
    Bitmap imageCheck;
    String userCode;
    String user_Type;
    ArrayList<ResponseImage> listUrl;
    ArrayList<ResponseInfoLesson> listInfo;
    //layout scan camera
    LinearLayout layoutScanCamera;
    Button btnConnectScanCamera;


    ArrayList<Information_Attend> List_Info_Attend;
    BitMap_Adapter bitMap_adapter;
    GridView gridView;
    LinearLayout layoutImage;

    ActionBar actionBar;

    //    button in actionBar
    ImageView imgback, imgSearch, imgExport;
    TextView date;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        addControls();
        addEvents();
//        clearData();


    }


    //set on click for menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
        List_Info_Attend.clear();
    }

    //export information of attendence on this day from data to file.csv
    public void export() {
        RequestInfoLesson requestInfoLesson = new RequestInfoLesson();
        listInfo = new ArrayList<>();
        requestInfoLesson.setCodeCourse(txtCodeCourse);
        requestInfoLesson.setDateAttend(txtDate);
        AppServiceFactory.getInstance();
        Call<List<ResponseInfoLesson>> GetInfoLesson = AppServiceFactory.getAppService().GetInfoLesson(requestInfoLesson);
        GetInfoLesson.enqueue(new Callback<List<ResponseInfoLesson>>() {
            @Override
            public void onResponse(Call<List<ResponseInfoLesson>> call, Response<List<ResponseInfoLesson>> response) {
                if (response.code() == 200) {
                    listInfo.addAll(response.body());

                    //generate data
                    StringBuilder data = new StringBuilder();
                    data.append("Id,MSSV,Date,Note");
                    for (int i = 0; i < listInfo.size(); i++) {
                        data.append("\n" + String.valueOf(i + 1) + "," + listInfo.get(i).getCodeStudent() + "," +
                                listInfo.get(i).getDate() + "," + response.body().get(i).getNote());
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

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setVisibility(View.GONE);
                txtSearch.setVisibility(View.VISIBLE);
            }
        });
        //not image
        if (listUrl.size() <= 0) {
//            imgCheck.setImageResource(R.drawable.people);

            if(user_Type.equals("lecturer")) {
                layoutScanCamera.setVisibility(View.VISIBLE);

                btnConnectScanCamera.setVisibility(View.VISIBLE);
                btnConnectScanCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //chuyển sang màn hình scan mã QR
                        Intent intent_CameraScan = new Intent(ImageActivity.this, CameraScanConnectDivice_Activity.class);
                        intent_CameraScan.putExtra("CodeSubject",txtCodeCourse);
                        if (txtCodeCourse.length() != 0) {
                            startActivity(intent_CameraScan);

                        }

                        startActivity(intent_CameraScan);
                    }
                });
            }


        }

//a image is loading image from ulr

        if (listUrl.size() == 1) {
            imgCheck.setVisibility(View.VISIBLE);
            getImage(listUrl.get(0).getUrlImage());

            listUrl.clear();
//many url
        } else {

            getListImage();

            layoutImage.setVisibility(View.INVISIBLE);
//search when EditText Change text
            Log.d("size", String.valueOf(List_Info_Attend.size()));
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bitMap_adapter.getFilter().filter(s.toString());
                    bitMap_adapter.notifyDataSetChanged();


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("posionclick", String.valueOf(position));
                if (user_Type.equals("student")) {
                    //create dialog to show image
                    AlertDialog.Builder imagedialog = new AlertDialog.Builder(ImageActivity.this);
                    //LayoutInflater là đọc xml layout file và chuyển đổi các thuộc tính của nó thành 1 View
                    LayoutInflater inflater = getLayoutInflater();
                    View dialog_View = inflater.inflate(R.layout.itemdialog_image_student, null);
                    ImageView img_Image = dialog_View.findViewById(R.id.imgImageDialog);
                    img_Image.setImageBitmap(List_Info_Attend.get(position).getBitmap());
                    Button btnReport, btnAccept;
                    imagedialog.setView(dialog_View);
                    final AlertDialog alert = imagedialog.create();
                    btnReport = dialog_View.findViewById(R.id.btnReport);
                    btnAccept = dialog_View.findViewById(R.id.btnAccept);
                    final int idImage = listUrl.get(position).getId();
                    //create data to call retrofit
                    final RequestReportImage requestReportImage = new RequestReportImage();
                    requestReportImage.setNote("MSSV sai, MSSV sinh vien la :" + txtCodeStudent);
                    final RequestRenameImage requestRenameImage = new RequestRenameImage();
                    requestRenameImage.setCodeStudent(txtCodeStudent);
                    btnReport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppServiceFactory.getInstance();
                            Call<ResponseMessage> ReportImage = AppServiceFactory.getAppService().ReportImage(idImage, requestReportImage);
                            ReportImage.enqueue(new Callback<ResponseMessage>() {
                                @Override
                                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(ImageActivity.this, response.body().getMassage(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                    Log.d("loi", "Loi");
                                }
                            });
                            alert.cancel();
                        }
                    });
                    btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppServiceFactory.getInstance();

                            Call<ResponseMessage> RenameImage = AppServiceFactory.getAppService().RenameImage(idImage, requestRenameImage);
                            RenameImage.enqueue(new Callback<ResponseMessage>() {
                                @Override
                                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(ImageActivity.this, response.body().getMassage(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                    Log.d("loi", "Loi");

                                }
                            });
                            alert.cancel();
                        }
                    });

                    alert.show();
                } else {
                    AlertDialog.Builder imagedialog = new AlertDialog.Builder(ImageActivity.this);
                    //LayoutInflater là đọc xml layout file và chuyển đổi các thuộc tính của nó thành 1 View
                    LayoutInflater inflater = getLayoutInflater();
                    View dialog_View = inflater.inflate(R.layout.itemdialog_image_lecturers, null);

                    final EditText txtTitle = dialog_View.findViewById(R.id.txt_mssv);
//                    show information student rolled up
                    if (listUrl.get(position).getCheckInf()) {
                        txtTitle.setText(listUrl.get(position).getCodeStudent());

                    }

                    //show information get from listURL
                    final CheckBox checkBox_Attend, checkBox_Report;
                    checkBox_Attend = dialog_View.findViewById(R.id.check_attend);
                    checkBox_Report = dialog_View.findViewById(R.id.reportview);
                    checkBox_Attend.setChecked(listUrl.get(position).getCheckInf());
                    checkBox_Report.setChecked(listUrl.get(position).getReport());
                    final MultiAutoCompleteTextView txtNote = dialog_View.findViewById(R.id.txtNote);
                    txtNote.setText(listUrl.get(position).getNote());

                    ImageView img_Image = dialog_View.findViewById(R.id.imgImageDialog);
                    img_Image.setImageBitmap(List_Info_Attend.get(position).getBitmap());
                    imagedialog.setView(dialog_View);

                    Button btnCancel, btnAccept;
                    btnCancel = dialog_View.findViewById(R.id.btnCancel);
                    btnAccept = dialog_View.findViewById(R.id.btnAccept);
                    final AlertDialog alert = imagedialog.create();
                    //cancel dialog
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.cancel();
                        }
                    });
                    btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if ((listUrl.get(position).getNote().equals(txtNote.getText().toString()) &&
                                    listUrl.get(position).getCodeStudent().equals(txtTitle.getText().toString()) &&
                                    listUrl.get(position).getReport().equals(checkBox_Report.isChecked()) &&
                                    listUrl.get(position).getCheckInf().equals(checkBox_Attend.isChecked())) || txtTitle.getText().toString().equals("")) {
                                alert.cancel();
                            } else {
                                Request_Change_Info_Attendence request_change_info_attendence = new Request_Change_Info_Attendence();
                                request_change_info_attendence.setCodeStudent(txtTitle.getText().toString());
                                request_change_info_attendence.setReport(checkBox_Report.isChecked());
                                request_change_info_attendence.setCheckInf(checkBox_Attend.isChecked());
                                request_change_info_attendence.setNote(txtNote.getText().toString());
                                AppServiceFactory.getInstance();
                                Call<ResponseMessage> Change_Info_Attendence = AppServiceFactory.getAppService().ChangeInfo_Attendence(listUrl.get(position).getId(), request_change_info_attendence);
                                Change_Info_Attendence.enqueue(new Callback<ResponseMessage>() {
                                    @Override
                                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                        if (response.code() == 200) {

                                            Toast.makeText(getApplicationContext(), response.body().getMassage(), Toast.LENGTH_LONG).show();
                                            alert.cancel();
                                        } else {
                                            Log.d("Change_Info_Attendence", "Error");
                                        }
                                    }

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                        Log.d("Retrofit_Change_Info_Attendence", "Error");
                                    }
                                });
                            }
                        }
                    });


                    alert.show();

                }


            }
        });


    }


    private void SetTitle() {
        Intent intent = getIntent();

        listUrl.addAll((Collection<? extends ResponseImage>) intent.getSerializableExtra("listUrl"));
        Log.d("listUrl", listUrl.get(0).getUrlImage());
    }

    private void addControls() {
        //define for view to set view from xml file
        layoutScanCamera = findViewById(R.id.layout_connect_QRcamera);
        btnConnectScanCamera=findViewById(R.id.btn_Connect_QRcamera);



        imgCheck = findViewById(R.id.imgCheckImage);
        layoutImage = findViewById(R.id.layoutImage);

        listUrl = new ArrayList<>();
        List_Info_Attend = new ArrayList<>();
        bitMap_adapter = new BitMap_Adapter(ImageActivity.this, R.layout.item_bitmap, List_Info_Attend);
        gridView = findViewById(R.id.gvImage);
        gridView.setAdapter(bitMap_adapter);
        gridView.setTextFilterEnabled(true);
//        SetTitle();
        Intent intent = getIntent();
        txtCodeCourse = intent.getStringExtra("codeCourse");
        txtCodeStudent = intent.getStringExtra("userCode");
        txtDate = intent.getStringExtra("date");
        listUrl.addAll((Collection<? extends ResponseImage>) intent.getSerializableExtra("listUrl"));


//        btnExport=findViewById(R.id.btnExport);

        actionBar = getSupportActionBar();
        if (actionBar != null) {


            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_actionbar_image_activity);
            View view_actionbar_image = getSupportActionBar().getCustomView();
            imgback = view_actionbar_image.findViewById(R.id.imgback);
            imgSearch = view_actionbar_image.findViewById(R.id.imgsearch);
            imgExport = view_actionbar_image.findViewById(R.id.imgExport);
            date = view_actionbar_image.findViewById(R.id.txtdate_of_lesson);
            txtSearch = view_actionbar_image.findViewById(R.id.txtSearch);
            date.setText(txtDate);
            SharedPreferences prefs = getSharedPreferences("Info_User", MODE_PRIVATE);
            user_Type = prefs.getString("UserType", null);
            if (user_Type.equals("lecturer")) {
                imgExport.setVisibility(View.VISIBLE);
            }
        }


    }

    private void getImage(final String urlimage) {
        final Thread threadgetImage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //load Image from listURL
                    String image = getApplicationContext().getString(R.string.URL) + urlimage;
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
                        URL url = new URL(getApplicationContext().getString(R.string.URL) + listUrl.get(i).getUrlImage());
                        //create bitmap to load Image from URL
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        //add image in listImage
                        Information_Attend information_attend = new Information_Attend();
                        information_attend.setBitmap(bitmap);
                        information_attend.setCheck_Attend(listUrl.get(i).getCheckInf());
                        information_attend.setReport(listUrl.get(i).getReport());
                        information_attend.setStudentCode(listUrl.get(i).getCodeStudent());
                        List_Info_Attend.add(information_attend);
//                        listBitmap.add(bitmap);
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