package com.version2.lavithedam.mobilevisionex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	TextView txtQRResult;
	Button btnScanQR;
	Button btnFaceDetect;
	Button btnTextRecognize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtQRResult = findViewById(R.id.txtQRResult);
		btnScanQR = findViewById(R.id.btnScanQR);
		btnScanQR.setOnClickListener(this);
		btnFaceDetect = findViewById(R.id.btnFaceDetect);
		btnFaceDetect.setOnClickListener(this);
		btnTextRecognize = findViewById(R.id.btnTextRecognize);
		btnTextRecognize.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnScanQR:
				Intent btnScanintent = new Intent(this, ScanBarcodeActivity.class);
				startActivityForResult(btnScanintent, 0);
				break;
			case R.id.btnFaceDetect:
				Intent btnFaceintent = new Intent(this, FaceDetectionActivity.class);
				startActivity(btnFaceintent);
				break;
			case R.id.btnTextRecognize:
				Intent btnTextintent = new Intent(this, TextRecognizeActivity.class);
				startActivityForResult(btnTextintent, 0);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == 0) {
			if (resultCode == CommonStatusCodes.SUCCESS) {
				if (data != null) {
					txtQRResult.setText(data.getStringExtra("barcode"));
				} else {
					txtQRResult.setText("No Barcode Found");
				}
			} else {
				txtQRResult.setText("No Barcode Found");
//				Toast.makeText(this, "No Barcode Found", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
