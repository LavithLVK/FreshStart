package com.version2.lavithedam.mobilevisionex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class FaceDetectionActivity extends AppCompatActivity implements View.OnClickListener {


	Button btnStartFaceDetect;
	ImageView imgFaceDetect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_face_detection);
		imgFaceDetect = findViewById(R.id.imgFaceDetect);
		btnStartFaceDetect = findViewById(R.id.btnStartFaceDetect);
		btnStartFaceDetect.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), 321);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == 321) {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Uri uri = data.getData();
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
						detectFaces(bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void detectFaces(Bitmap bitmap) {
		Paint myPaint = new Paint();
		myPaint.setStrokeWidth(5);
		myPaint.setColor(Color.RED);
		myPaint.setStyle(Paint.Style.STROKE);

		Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
		Canvas tempCanvas = new Canvas(tempBitmap);
		tempCanvas.drawBitmap(bitmap, 0, 0, null);

		FaceDetector faceDetector = new FaceDetector.Builder(this).setTrackingEnabled(false).build();

		if (!faceDetector.isOperational()) {
			new AlertDialog.Builder(this).setMessage("Could not set up the face detection").show();
			return;
		}

		Frame frame = new Frame.Builder().setBitmap(bitmap).build();
		SparseArray<Face> faces = faceDetector.detect(frame);
		for (int i = 0; i < faces.size(); i++) {
			Face currentFace = faces.valueAt(i);
			float x1 = currentFace.getPosition().x;
			float y1 = currentFace.getPosition().y;
			float x2 = x1 + currentFace.getWidth();
			float y2 = y1 + currentFace.getHeight();
			tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myPaint);
		}

		if (faces.size() == 0) {
			Toast.makeText(this, "No Faces Detected in this image", Toast.LENGTH_LONG).show();
		}

		imgFaceDetect.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
	}

}
