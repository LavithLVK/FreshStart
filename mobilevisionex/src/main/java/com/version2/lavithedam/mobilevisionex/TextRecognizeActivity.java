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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class TextRecognizeActivity extends AppCompatActivity {


	SurfaceView cameraSurface;
	TextView txtResult;
	TextRecognizer textRecognizer;
	CameraSource cameraSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_recognize);
		cameraSurface = findViewById(R.id.surfaceCamera);
		txtResult = findViewById(R.id.txtRecognizeResult);
		startRecognize();
	}

	private void startRecognize() {
		textRecognizer = new TextRecognizer.Builder(this).build();

		if (!textRecognizer.isOperational()) {
			Toast.makeText(this, "Unable to load Recognizer dependencies", Toast.LENGTH_LONG).show();
			return;
		}


		cameraSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder surfaceHolder) {
				if (ActivityCompat.checkSelfPermission(TextRecognizeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
					if (ActivityCompat.shouldShowRequestPermissionRationale(TextRecognizeActivity.this,
						  Manifest.permission.CAMERA)) {
						AlertDialog.Builder builder = new AlertDialog.Builder(TextRecognizeActivity.this);
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
								Toast.makeText(TextRecognizeActivity.this, "Nee Karma", Toast.LENGTH_LONG).show();
							}
						}).show();
					} else {
						ActivityCompat.requestPermissions(TextRecognizeActivity.this,
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
				cameraSource.stop();
			}
		});

		textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
			@Override
			public void release() {

			}

			@Override
			public void receiveDetections(Detector.Detections<TextBlock> detections) {
				final SparseArray<TextBlock> items = detections.getDetectedItems();
				if (items.size() != 0) {
					txtResult.post(new Runnable() {
						@Override
						public void run() {
							StringBuilder stringBuilder = new StringBuilder();
							for (int i = 0; i < items.size(); i++) {
								TextBlock item = items.valueAt(i);
								stringBuilder.append(item.getValue());
								stringBuilder.append("\n");
							}
							if(stringBuilder.toString().length()>0){
								Intent intent = new Intent();
								intent.putExtra("barcode", stringBuilder.toString());
								setResult(CommonStatusCodes.SUCCESS, intent);
								finish();
							}
						}
					});
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
		try {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
			cameraSource = new CameraSource.Builder(this, textRecognizer)
				  .setFacing(CameraSource.CAMERA_FACING_BACK)
				  .setRequestedPreviewSize(1280, 1024)
				  .setRequestedFps(2.0f)
				  .setAutoFocusEnabled(true)
				  .build();
			cameraSource.start(cameraSurface.getHolder());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
