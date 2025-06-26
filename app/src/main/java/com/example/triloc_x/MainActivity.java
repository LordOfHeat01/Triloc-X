package com.example.triloc_x;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private PreviewView previewView;
    private OverlayView overlayView;
    private EditText heightInput;
    private Button selectObjectBtn, calculateBtn;
    private TextView resultText;

    private float focalLength = 0f; // mm
    private RectF boundingBox = null;
    private boolean isSelecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewView = findViewById(R.id.previewView);
        overlayView = findViewById(R.id.overlayView);
        heightInput = findViewById(R.id.inputRealHeight);
        selectObjectBtn = findViewById(R.id.selectObjectButton);
        calculateBtn = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.outputText);

        previewView.setOnTouchListener((v, event) -> {
            if (!isSelecting) return false;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    boundingBox = new RectF(event.getX(), event.getY(), event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    boundingBox.right = event.getX();
                    boundingBox.bottom = event.getY();
                    overlayView.setBoundingBox(boundingBox);
                    break;
                case MotionEvent.ACTION_UP:
                    isSelecting = false;
                    selectObjectBtn.setText("Select Object");
                    overlayView.setBoundingBox(boundingBox);
                    break;
            }
            return true;
        });

        selectObjectBtn.setOnClickListener(v -> {
            isSelecting = true;
            boundingBox = null;
            overlayView.setBoundingBox(null);
            selectObjectBtn.setText("Drawing...");
            Toast.makeText(this, "Draw box around object", Toast.LENGTH_SHORT).show();
        });

        calculateBtn.setOnClickListener(v -> calculateDistance());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.bindToLifecycle(this, cameraSelector, preview);
                fetchFocalLength();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(this, "Camera failed", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void fetchFocalLength() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraMetadata.LENS_FACING_BACK) {
                    float[] lengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                    if (lengths != null && lengths.length > 0) {
                        focalLength = lengths[0];
                        break;
                    }
                }
            }
            if (focalLength == 0f) focalLength = 3.5f;
        } catch (Exception e) {
            focalLength = 3.5f;
        }
    }

    private void calculateDistance() {
        if (boundingBox == null) {
            Toast.makeText(this, "Select an object first", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float realHeight = Float.parseFloat(heightInput.getText().toString());
            float pixelHeight = boundingBox.height();

            if (pixelHeight <= 0) {
                Toast.makeText(this, "Invalid box height", Toast.LENGTH_SHORT).show();
                return;
            }

            float sensorHeightMm = 4.8f;
            int previewHeightPx = previewView.getHeight();
            float distance = (realHeight * focalLength * previewHeightPx) / (pixelHeight * sensorHeightMm);

            resultText.setText(String.format("Distance: %.2f meters", distance));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid height", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
