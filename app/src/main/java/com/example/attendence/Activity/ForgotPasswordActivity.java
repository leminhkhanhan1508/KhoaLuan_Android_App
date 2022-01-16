package com.example.attendence.Activity;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.example.attendence.Model.RequestForgotPassword;
import com.example.attendence.Model.ResponseMessage;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import com.example.attendence.databinding.ActivityChangePasswordBinding;
import com.example.attendence.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.theartofdev.edmodo.cropper.CropImage;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActionBar actionBar ;
    private ActivityForgotPasswordBinding binding;
    String student_code;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    //
    final int CROP_PIC = 2;
    private Uri picUri;

    String cameraPermission[];
    String storagePermission[];
//    cho phep ramdom từ những kí tự này thôi
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789qwertyuiopasdfghjklzxcvbnm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        addControls();
        addEvents();
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

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void ForgotPassword(RequestForgotPassword requestForgotPassword) {

        AppServiceFactory.getInstance();
        Call<ResponseMessage> ForgotPassword = AppServiceFactory.getAppService().ChangePassword(requestForgotPassword);
        ForgotPassword.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMassage(), Toast.LENGTH_LONG).show();
                        if (response.body().getOk()) {
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.d("ForgotPassword", "Lỗi retrofit");
            }
        });

    }

    private void addEvents() {
        binding.imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "oke nha ", Toast.LENGTH_LONG).show();
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestForgotPassword requestForgotPassword = new RequestForgotPassword();
                requestForgotPassword.setUserName(student_code);
                requestForgotPassword.setNewPassword(getRandomString(10));
                ForgotPassword(requestForgotPassword);
            }
        });
    }

    private void addControls() {
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_actionbar_resetpassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void pickFromGallery() {
        CropImage.activity().start(ForgotPasswordActivity.this);
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // Requesting camera permission
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // Requesting camera and gallery
    // permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    private void detectTextFromImage(Bitmap imageBitmap) {
//
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                .getVisionTextDetector();
        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        processTextRecognitionResult(firebaseVisionText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });


    }

    private void processTextRecognitionResult(FirebaseVisionText firebaseVisionText) {
//        String resultText = firebaseVisionText.getBlocks();

        List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(getApplicationContext(), "no text found", Toast.LENGTH_LONG).show();
            return;
        } else {
            int a = firebaseVisionText.getBlocks().size();
            FirebaseVisionText.Block mssv = firebaseVisionText.getBlocks().get(a - 2);
            //student code get to student card
            student_code = mssv.getText();

//            Toast.makeText(getApplicationContext(), mssv.getText(), Toast.LENGTH_LONG).show();
            int b = firebaseVisionText.getBlocks().get(a - 1).getLines().size();
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                String blockText = block.getText();
                Log.d("blockText", blockText);
                Float blockConfidence = block.getBoundingBox().exactCenterX();

//            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                Point[] blockCornerPoints = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();
                for (FirebaseVisionText.Line line : block.getLines()) {
                    String lineText = line.getText();
                    Log.d("lineText", lineText);
//                Float lineConfidence = line.getConfidence();
//                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                    Point[] lineCornerPoints = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();


                    for (FirebaseVisionText.Element element : line.getElements()) {
                        String elementText = element.getText();
                        Log.d("elementText", elementText);
                        ;
//                    Float elementConfidence = element.getConfidence();
//                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                        Point[] elementCornerPoints = element.getCornerPoints();
                        Rect elementFrame = element.getBoundingBox();
                    }
                }
            }
        }

    }

    //Show image took at activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                detectTextFromImage(bitmap);

//                binding.imgstudentcard.setImageBitmap(bitmap);


//                Picasso.with(this).load(resultUri).into(userpic);
            }
        }

    }
}