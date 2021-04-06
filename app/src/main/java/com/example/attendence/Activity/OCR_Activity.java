package com.example.attendence.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.attendence.R;
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

import java.io.*;
import java.text.DecimalFormat;
import java.util.List;

//import static com.googlecode.tesseract.android.TessBaseAPI.OEM_TESSERACT_ONLY;

public class OCR_Activity extends AppCompatActivity {
    ImageView imgStudentCard;
    TextView txtData;
    Button btnTake_Picture, btnDetect_Text;

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;

    final int CROP_PIC = 2;
    private Uri picUri;

    String cameraPermission[];
    String storagePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnTake_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromGallery();
                }


            }
        });
        btnDetect_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imgStudentCard.buildDrawingCache();
//                Bitmap bitmap = imgStudentCard.getDrawingCache();
//                detectTextFromImage(bitmap);

            }
        });
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


    private void addControls() {
        imgStudentCard = findViewById(R.id.imgStudentCard);
        txtData = findViewById(R.id.txtData);
        btnTake_Picture = findViewById(R.id.btnTakePicture);
        btnDetect_Text = findViewById(R.id.btnDetectText);
// allowing permissions of gallery and camera
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

//    private void showImagePicDialog() {
//        String options[] = {"Camera", "Gallery"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Pick Image From");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    if (!checkCameraPermission()) {
//                        requestCameraPermission();
//                    } else {
//                        pickFromGallery();
//                    }
//                } else if (which == 1) {
//                    if (!checkStoragePermission()) {
//                        requestStoragePermission();
//                    } else {
//                        pickFromGallery();
//                    }
//                }
//            }
//        });
//        builder.create().show();
//    }

    //    private class YourAnalyzer implements ImageAnalysis.Analyzer {
//
//        private int degreesToFirebaseRotation(int degrees) {
//            switch (degrees) {
//                case 0:
//                    return FirebaseVisionImageMetadata.ROTATION_0;
//                case 90:
//                    return FirebaseVisionImageMetadata.ROTATION_90;
//                case 180:
//                    return FirebaseVisionImageMetadata.ROTATION_180;
//                case 270:
//                    return FirebaseVisionImageMetadata.ROTATION_270;
//                default:
//                    throw new IllegalArgumentException(
//                            "Rotation must be 0, 90, 180, or 270.");
//            }
//        }
//
//        @Override
//        public void analyze(@NonNull @NotNull ImageProxy imageProxy, int rotationDegrees) {
//
//            if (imageProxy == null || imageProxy.getImage() == null) {
//                return;
//            }
//            Image mediaImage = imageProxy.getImage();
//            int rotation = degreesToFirebaseRotation(rotationDegrees);
//            FirebaseVisionImage image =
//                    FirebaseVisionImage.fromMediaImage(mediaImage, rotation);
//            detectTextFromImage(image.getBitmapForDebugging());
//        }
//    }
//    private int getCameraId() {
//        int cameraId = -1;
//        // Search for the front facing camera
//        int numberOfCameras = Camera.getNumberOfCameras();
//        for (int i = 0; i < numberOfCameras; i++) {
//            Camera.CameraInfo info = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, info);
//            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                Log.d("APP", "Camera found");
//                cameraId = i;
//                break;
//            }
//        }
//        return cameraId;
//    }

    //take a picture from your phone's camera
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } catch (ActivityNotFoundException e) {
//            Log.d("Error", e.getMessage());
//            // display error state to the user
//        }
//    }
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
                DecimalFormat dtime = new DecimalFormat("#.##");
                float x=Float.valueOf((float) (0.2/8.5));
                float y=Float.valueOf((float) ((1.9/5.3)*bitmap.getHeight()));
                float hight=Float.valueOf((float) ((2.7/5.3)*bitmap.getHeight()));
                float width=Float.valueOf((float) ((2.0/8.5)*bitmap.getWidth()));
                Bitmap resizedbitmap1;
                resizedbitmap1 = Bitmap.createBitmap(bitmap, (int) (x*bitmap.getWidth()), (int) y, (int) width, (int) hight);
                imgStudentCard.setImageBitmap(resizedbitmap1);
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
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                for (FirebaseVisionFace face : firebaseVisionFaces) {
                    Rect bounds = face.getBoundingBox();

                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees\
                    int hight=2*(bounds.bottom- bounds.top);
                    int width=2*(bounds.right- bounds.left);
                    Log.d("x",String.valueOf(bounds.right));
                    Bitmap resizedbitmap1=Bitmap.createBitmap(bitmap, 0,bounds.top-bounds.top/6,bitmap.getWidth()/4, bitmap.getHeight()/2);
                    imgStudentCard.setImageBitmap(resizedbitmap1);
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
//    private void performCrop() {
//        // take care of exceptions
//        try {
//            // call the standard crop action intent (the user device may not
//            // support it)
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            // indicate image type and Uri
//            cropIntent.setDataAndType(picUri, "image/*");
//            // set crop properties
//            cropIntent.putExtra("crop", "true");
//            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 2);
//            cropIntent.putExtra("aspectY", 1);
//            // indicate output X and Y
//            cropIntent.putExtra("outputX", 256);
//            cropIntent.putExtra("outputY", 256);
//            // retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            // start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, CROP_PIC);
//        }
//        // respond to users whose devices do not support the crop action
//        catch (ActivityNotFoundException anfe) {
//            Toast toast = Toast
//                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

    private void detectTextFromImage(Bitmap imageBitmap) {
//        TessBaseAPI baseAPI = new TessBaseAPI();
//        try {
//            prepareLanguageDir();
//
//            baseAPI.init(String.valueOf(getFilesDir()), "vie");
//
//        } catch (Exception e) {
//            // Logging here
//        }
//
//        baseAPI.setImage(imageBitmap);
//        String whitelist = "0123456789";
//        baseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
//        baseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, whitelist);
//        baseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?");
//        String result = baseAPI.getUTF8Text();
//        Log.d("Environment.getRootDirectory().getPath()s",result);
//        txtData.setText(result);
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
            txtData.setText(mssv.getText());
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
}
//    private void copyFile() throws IOException {
//        // work with assets folder
//        AssetManager assMng = getAssets();
//        InputStream is = assMng.open("tessdata/vie.traineddata");
//        OutputStream os = new FileOutputStream(getFilesDir() +
//                "/tessdata/vie.traineddata");
//        byte[] buffer = new byte[1024];
//        int read;
//        while ((read = is.read(buffer)) != -1) {
//            os.write(buffer, 0, read);
//        }
//
//        is.close();
//        os.flush();
//        os.close();
//    }
//
//    private void prepareLanguageDir() throws IOException {
//        File dir = new File(getFilesDir() + "/tessdata");
//        Log.d("dfdsfdsgfdsgfdsfds",String.valueOf(getFilesDir()));
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        File trainedData = new File(getFilesDir() + "/tessdata/vie.traineddata");
//        if (!trainedData.exists()) {
//            copyFile();
//        }
//    }


