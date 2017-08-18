package com.tvz.tomislav.qwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.tvz.tomislav.qwaiter.nfc.NFCReader;
import com.tvz.tomislav.qwaiter.qrcode.BarcodeCaptureActivity;

public class ScanActivity extends AppCompatActivity {

    private Button mNFCButton;
    private Button mQRCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNFCButton = (Button) findViewById(R.id.nfc_button);
        mQRCodeButton = (Button) findViewById(R.id.qr_code_button);

        mNFCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NFCReader.class);
                startActivity(intent);
            }
        });
        mQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BarcodeCaptureActivity.class);
                startActivity(intent);
            }
        });
    }

}
