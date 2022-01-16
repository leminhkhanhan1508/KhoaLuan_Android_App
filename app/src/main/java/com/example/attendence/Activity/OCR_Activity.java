package com.example.attendence.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.example.attendence.Model.RequestRegister;
import com.example.attendence.Model.RequestRenameImage;
import com.example.attendence.Model.RequestReportImage;
import com.example.attendence.Model.ResponseMessage;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import com.example.attendence.databinding.ActivityOcrBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions;
//import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
//import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
//import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
//import com.googlecode.tesseract.android.TessBaseAPI;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

//import static com.googlecode.tesseract.android.TessBaseAPI.OEM_TESSERACT_ONLY;

public class OCR_Activity extends AppCompatActivity {
    //    ImageView imgStudentCard;
//    TextView txtData;
//    Button btnTake_Picture, btnDetect_Text;
    Bitmap studentcard,avatar;
    private ActivityOcrBinding binding;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 800;
    //
    final int CROP_PIC = 2;
    private Uri picUri;

    String cameraPermission[];
    String storagePermission[];
    RequestRegister requestRegister;
    String student_code;
    private String image_from_student_card;
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789qwertyuiopasdfghjklzxcvbnm";
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOcrBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        addControls();
        addEvents();
    }


    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void RegisterAccount() {

        requestRegister.setUserName(student_code);
        requestRegister.setCodeUser(student_code);
        requestRegister.setEmail(student_code + "@gm.uit.edu.vn");
        requestRegister.setMajor(binding.txtMajor.getText().toString());
//        random password for account
        String password = getRandomString(10);
        requestRegister.setPassword(getRandomString(10));

        requestRegister.setName(binding.txtName.getText().toString());
        String type = "student";
        requestRegister.setAvatar(image_from_student_card);

        requestRegister.setUserType(type);
//show dialog loading until response code is 200
        final SweetAlertDialog pDialog = new SweetAlertDialog(OCR_Activity.this, SweetAlertDialog.PROGRESS_TYPE);


        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();
//create a  account
        AppServiceFactory.getInstance();
        final Call<ResponseMessage> RegisterAccount = AppServiceFactory.getAppService().Register(requestRegister);
        RegisterAccount.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.code() == 200) {

                        pDialog.cancel();
                        Toast.makeText(OCR_Activity.this, response.body().getMassage().toString(), Toast.LENGTH_LONG).show();
                        binding.txtName.setText("");
                        binding.txtMajor.setText("");
                        binding.imgshowAvatar.setVisibility(View.GONE);
                        binding.imgshowAvatar1.setVisibility(View.GONE);
                        requestRegister=null;

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

    private void addEvents() {
        binding.imgAvatar.setOnClickListener(new View.OnClickListener() {
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
        binding.imgAvatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
//
                }
            }
        });
        binding.btnRigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterAccount();
            }
        });

        binding.imgshowAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagestudentcard(studentcard);
            }
        });
        binding.imgshowAvatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAvatar(avatar);
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }

    private void showImagestudentcard(Bitmap bitmap) {

        AlertDialog.Builder imagedialog = new AlertDialog.Builder(OCR_Activity.this);
        //LayoutInflater là đọc xml layout file và chuyển đổi các thuộc tính của nó thành 1 View
        LayoutInflater inflater = getLayoutInflater();
        View dialog_View = inflater.inflate(R.layout.itemdialog_image_student, null);
        ImageView img_Image = dialog_View.findViewById(R.id.imgImageDialog);
        img_Image.setImageBitmap(bitmap);
        Button btnReport, btnAccept;
        imagedialog.setView(dialog_View);
        final AlertDialog alert = imagedialog.create();
        btnReport = dialog_View.findViewById(R.id.btnReport);
        btnAccept = dialog_View.findViewById(R.id.btnAccept);
        btnAccept.setText("Accept");
        btnReport.setText("Cancel");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();
    }

    private void showImageAvatar(Bitmap bitmap) {

        AlertDialog.Builder imagedialog = new AlertDialog.Builder(OCR_Activity.this);
        //LayoutInflater là đọc xml layout file và chuyển đổi các thuộc tính của nó thành 1 View
        LayoutInflater inflater = getLayoutInflater();
        View dialog_View = inflater.inflate(R.layout.itemdialogimage, null);
        ImageView img_Image = dialog_View.findViewById(R.id.imgImageDialog);
        img_Image.setImageBitmap(bitmap);
        Button btnReport, btnAccept;
        imagedialog.setView(dialog_View);
        final AlertDialog alert = imagedialog.create();
        btnReport = dialog_View.findViewById(R.id.btnReport);
        btnAccept = dialog_View.findViewById(R.id.btnAccept);
        btnAccept.setText("Accept");
        btnReport.setText("Cancel");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();
    }


    private void addControls() {
        actionBar=getSupportActionBar();
        actionBar.hide();
//
// allowing permissions of gallery and camera
        requestRegister = new RequestRegister();
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void pickFromGallery() {
        CropImage.activity().start(OCR_Activity.this);
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
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

    //Show image took at activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bitmap imageBitmap = null;
//            Log.d("typeintent",data.getType().toString());
            if (data.getExtras() != null) {

                Bundle extras = data.getExtras();
                if (extras.get("data") != null) {
                    imageBitmap = (Bitmap) extras.get("data");
                    avatar=imageBitmap;
                    binding.imgshowAvatar1.setVisibility(View.VISIBLE);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    image_from_student_card = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                }


            }
        }
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
                if (bitmap != null) {
                    studentcard=bitmap;
                    binding.imgshowAvatar.setVisibility(View.VISIBLE);
                }
                detectTextFromImage(bitmap);
                DecimalFormat dtime = new DecimalFormat("#.##");
                float x = Float.valueOf((float) (0.2 / 8.5));
                float y = Float.valueOf((float) ((1.9 / 5.3) * bitmap.getHeight()));
                float hight = Float.valueOf((float) ((2.7 / 5.3) * bitmap.getHeight()));
                float width = Float.valueOf((float) ((2.0 / 8.5) * bitmap.getWidth()));
                Bitmap resizedbitmap1;
                resizedbitmap1 = Bitmap.createBitmap(bitmap, (int) (x * bitmap.getWidth()), (int) y, (int) width, (int) hight);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resizedbitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();



//                imgStudentCard.setImageBitmap(resizedbitmap1);
//                detectFaceFromImage(bitmap);
                detectObjectFromImage(bitmap);


//                Picasso.with(this).load(resultUri).into(userpic);
            }
        }

    }

    private void detectObjectFromImage(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);


    }

    private void detectFaceFromImage(final Bitmap bitmap) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                for (FirebaseVisionFace face : firebaseVisionFaces) {
                    Rect bounds = face.getBoundingBox();

                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees\
                    int hight = 2 * (bounds.bottom - bounds.top);
                    int width = 2 * (bounds.right - bounds.left);
                    Log.d("x", String.valueOf(bounds.right));
                    Bitmap resizedbitmap1 = Bitmap.createBitmap(bitmap, 0, bounds.top - bounds.top / 6, bitmap.getWidth() / 4, bitmap.getHeight() / 2);
//                    imgStudentCard.setImageBitmap(resizedbitmap1);
                    Log.d("Head is rotated to the right rotY degrees", String.valueOf(rotY) + String.valueOf(rotZ));
                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                    if (leftEar != null) {
                        FirebaseVisionPoint leftEarPos = leftEar.getPosition();
                    }

                    // If contour detection was enabled:
//                    List<FirebaseVisionPoint> leftEyeContour =
//                            face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
//                    List<FirebaseVisionPoint> upperLipBottomContour =
//                            face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();

                    // If classification was enabled:
                    if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        float smileProb = face.getSmilingProbability();
                    }
                    if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        float rightEyeOpenProb = face.getRightEyeOpenProbability();
                    }

                    // If face tracking was enabled:
                    if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                        int id = face.getTrackingId();
                    }
                }
            }
        });
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
//        List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
//        if (blocks.size() == 0) {
//            Toast.makeText(getApplicationContext(), "no text found", Toast.LENGTH_LONG).show();
//            return;
//        } else {
//            for (FirebaseVisionText.Block block : blocks) {
//                for (FirebaseVisionText.Line line : block.getLines()) {
//                    //...
//                    txtData.setText(line.getText());
//                    Log.d("line", line.getText());
//                }
//            }
//        }
    }


//    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
//
//    static {
//        ORIENTATIONS.append(Surface.ROTATION_0, 90);
//        ORIENTATIONS.append(Surface.ROTATION_90, 0);
//        ORIENTATIONS.append(Surface.ROTATION_180, 270);
//        ORIENTATIONS.append(Surface.ROTATION_270, 180);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private int getRotationCompensation(String cameraId, Activity activity, Context context)
//            throws CameraAccessException {
//        // Get the device's current rotation relative to its "native" orientation.
//        // Then, from the ORIENTATIONS table, look up the angle the image must be
//        // rotated to compensate for the device's rotation.
//        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        int rotationCompensation = ORIENTATIONS.get(deviceRotation);
//
//        // On most devices, the sensor orientation is 90 degrees, but for some
//        // devices it is 270 degrees. For devices with a sensor orientation of
//        // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
//        CameraManager cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
//        int sensorOrientation = cameraManager
//                .getCameraCharacteristics(cameraId)
//                .get(CameraCharacteristics.SENSOR_ORIENTATION);
//        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360;
//
//        // Return the corresponding FirebaseVisionImageMetadata rotation value.
//        int result;
//        switch (rotationCompensation) {
//            case 0:
//                result = FirebaseVisionImageMetadata.ROTATION_0;
//                break;
//            case 90:
//                result = FirebaseVisionImageMetadata.ROTATION_90;
//                break;
//            case 180:
//                result = FirebaseVisionImageMetadata.ROTATION_180;
//                break;
//            case 270:
//                result = FirebaseVisionImageMetadata.ROTATION_270;
//                break;
//            default:
//                result = FirebaseVisionImageMetadata.ROTATION_0;
//                Log.e("TAG", "Bad rotation value: " + rotationCompensation);
//        }
//        return result;
//    }
//
//    private void detectTextFromImage(Bitmap bitmap) {
//        int rotation = 0;
//        try {
//            rotation = getRotationCompensation(getCameraId() + "", this, this);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
////        ImageProxy mediaImage = (ImageProxy) imgStudentCard.getDrawable();
////
////        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage.getImage(), rotation);
//
//        final FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
//        FirebaseVision detector = FirebaseVision.getInstance();
//        FirebaseVisionTextDetector firebaseVisionTextRecognizer=detector.getVisionTextDetector();
//        firebaseVisionTextRecognizer.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//            @Override
//            public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
//                if (blocks.size() == 0) {
//                    Log.d("TAG", "No text found");
//                    return;
//                }
//                else {
//                    for(FirebaseVisionText.Block block: firebaseVisionText.getBlocks()){
//                        Log.d("line",block.getText());
//                        Log.d("line", String.valueOf(block.getLines()));
//                    }
//                }
//            }
//        });
//        Task<FirebaseVisionText> task=firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
//        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//            @Override
//            public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                txtData.setText(firebaseVisionText.getText());
//            }
//        });
//        FirebaseVisionCloudDocumentRecognizerOptions options =
//                new FirebaseVisionCloudDocumentRecognizerOptions.Builder()
//                        .setLanguageHints(Arrays.asList("en", "vi"))
//                        .build();
//
//        FirebaseVisionDocumentTextRecognizer firebaseVisionTextDetector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer(options);
//        firebaseVisionTextDetector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionDocumentText>() {
//            @Override
//            public void onSuccess(FirebaseVisionDocumentText firebaseVisionDocumentText) {
//                displayTextFromImage(firebaseVisionDocumentText);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//
//            }
//        });
//    }

}



