package com.example.attendence.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.attendence.R;
import com.google.android.gms.vision.barcode.Barcode;
import java.util.List;
import info.bideens.barcode.BarcodeReader;

public class CameraScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Camera Scanner");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        barcodeReader.onDestroyView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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


    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        final String scannedCode = barcode.displayValue;
        if (scannedCode.length() != 0) {
//            sendMessage(requestCode);

            Intent intent_ScanCamera = new Intent(CameraScanActivity.this, JoinCourseActivity.class);
            intent_ScanCamera.putExtra("request", scannedCode);
            startActivity(intent_ScanCamera);
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }
}