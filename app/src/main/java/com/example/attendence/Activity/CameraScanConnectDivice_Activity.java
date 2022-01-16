package com.example.attendence.Activity;

import android.content.Intent;
import android.util.SparseArray;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.attendence.R;
import com.google.android.gms.vision.barcode.Barcode;
import info.bideens.barcode.BarcodeReader;

import java.util.List;

public class CameraScanConnectDivice_Activity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private BarcodeReader barcodeReader;

    String codeSubject;
    String sizeCodeSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan_connect);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Camera Scaner");
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner_connect_device);
        Intent intent = this.getIntent();
        codeSubject = intent.getStringExtra("CodeSubject");
//because length have 2 char so if <10 must add 0 ex: 09,10 ,11
        if (codeSubject.length() < 10) {
            sizeCodeSubject = "0" + codeSubject.length();
        } else {
            sizeCodeSubject = String.valueOf(codeSubject.length());
        }
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
//request from sever require format ...
        String requestCode = "TK" + scannedCode + sizeCodeSubject + codeSubject.toUpperCase();
        if (scannedCode.length() != 0) {
//            sendMessage(requestCode);

            Intent intent_ScanCamera = new Intent(CameraScanConnectDivice_Activity.this, ScanResult_ConnectDevice.class);
            intent_ScanCamera.putExtra("request", "TK" + scannedCode + sizeCodeSubject + codeSubject.toUpperCase());
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