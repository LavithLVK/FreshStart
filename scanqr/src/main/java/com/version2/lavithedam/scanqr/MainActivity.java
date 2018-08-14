package com.version2.lavithedam.scanqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

	private TextView viewName;
	private TextView viewAddress;
	private Button btnScan;

	private IntentIntegrator qrScan;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewAddress = findViewById(R.id.textViewAddress);
		viewName = findViewById(R.id.textViewName);
		btnScan = findViewById(R.id.buttonScan);
		btnScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				takePicture();
			}
		});
		qrScan = new IntentIntegrator(this);
	}

	private void takePicture() {
		qrScan.initiateScan();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			//if qrcode has nothing in it
			if (result.getContents() == null) {
				Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
			} else {
				//if qr contains data
				try {
					String resultString = result.getContents();
					//converting the data to json
					JSONObject obj;
					if (isJSONValid(resultString)) {
						obj = new JSONObject(resultString);
						viewName.setText(obj.getString("name"));
						viewAddress.setText(obj.getString("address"));
					} else {
						viewName.setText(result.getContents());
						viewAddress.setText(result.getContents());
					}
				} catch (JSONException e) {
					e.printStackTrace();
					//if control comes here
					//that means the encoded format not matches
					//in this case you can display whatever data is available on the qrcode
					//to a toast
					Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
				}
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			// edited, to include @Arthur's comment
			// e.g. in case JSONArray is valid as well...
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				ex.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
