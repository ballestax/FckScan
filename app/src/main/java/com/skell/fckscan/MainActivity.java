package com.skell.fckscan;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.skell.fckscan.barcode.BarcodeCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("FCK SCAN");

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvResult.setVisibility(View.GONE);

        ImageButton btScan = (ImageButton) findViewById(R.id.btScan);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanear();
            }
        });

    }

    public void escanear(){
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    //tvResult.setText(barcode.displayValue);

                    String url = barcode.displayValue;
                    if (!url.startsWith("https://") && !url.startsWith("http://")) {
                        url = "http://" + url;
                    }
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }catch(Exception e){
                        Toast.makeText(this, "Ocurrio un error al abrir el destino", Toast.LENGTH_SHORT).show();
                    }

                } else tvResult.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }


}
