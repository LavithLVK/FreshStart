package com.version2.lavithedam.mobilevisionex;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanBarcodeActivity extends AppCompatActivity {

	SurfaceView cameraPreview;
	BarcodeDetector barcodeDetector;
	CameraSource cameraSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_barcode);
		cameraPreview = findViewById(R.id.cameraPreview);
		createCameraPreview();
	}

	private void createCameraPreview() {

		barcodeDetector = new BarcodeDetector.Builder(this).build();

		cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder surfaceHolder) {
				if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
					if (ActivityCompat.shouldShowRequestPermissionRationale(ScanBarcodeActivity.this,
						  Manifest.permission.CAMERA)) {
						AlertDialog.Builder builder = new AlertDialog.Builder(ScanBarcodeActivity.this);
						builder.setMessage(R.string.requestPermission).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								Intent intent = new Intent();
								intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", getPackageName(), null);
								intent.setData(uri);
								startActivity(intent);
							}
						}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								Toast.makeText(ScanBarcodeActivity.this, "Nee Karma", Toast.LENGTH_LONG).show();
							}
						}).show();
					} else {
						ActivityCompat.requestPermissions(ScanBarcodeActivity.this,
							  new String[]{Manifest.permission.CAMERA},
							  1234);
					}
				} else {
					startCamera();
				}

			}

			@Override
			public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
				if (cameraSource != null) {
					cameraSource.stop();
				}
			}
		});

		barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
			@Override
			public void release() {

			}

			@Override
			public void receiveDetections(Detector.Detections<Barcode> detections) {
				final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
				if (barcodeSparseArray.size() > 0) {
					Intent intent = new Intent();
					intent.putExtra("barcode", barcodeSparseArray.valueAt(0).displayValue);
					setResult(CommonStatusCodes.SUCCESS, intent);
					finish();
				}
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 1234) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				startCamera();
			}
		}
	}

	private void startCamera() {

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		try {
			cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true)
				  .setRequestedPreviewSize(1600, 1024).build();
			cameraSource.start(cameraPreview.getHolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
