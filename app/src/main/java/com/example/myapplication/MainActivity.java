package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    TextView tvCardText;
    Button btStartScan;
    TextView textView;
    Spinner enpointSpinner;
    // ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btStartScan = (Button) findViewById(R.id.scanButton);
        tvCardText = (TextView)findViewById(R.id.tvCardText);
        textView = (TextView) findViewById(R.id.text);
        btStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();

                // Add the request to the RequestQueue.
            }
        });


        enpointSpinner = (Spinner) findViewById(R.id.spinner1);
    }
    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                updateText(result.getContents());

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);

                String url ="http://192.168.43.85:5000/" + String.valueOf(enpointSpinner.getSelectedItem());


                // Request a string response from the provided URL.

                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                textView.setText("Response is: "+ response.toString());
                                Log.v("new", response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.toString());
                        Log.v("new", error.toString());
                    }
                });
                queue.add(stringRequest);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateText(String scanCode) {
        tvCardText.setText(scanCode);
    }


}

