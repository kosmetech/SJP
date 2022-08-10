package com.example.qrcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcodescanner.get_set.Item;
import com.example.qrcodescanner.get_set.data_scan;
import com.google.gson.Gson;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView mScannerView;
    FrameLayout camera;
    Item dtlnya;
    private ArrayList<data_scan> data_scan_ArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        camera = findViewById(R.id.frame_layout_camera);
        Intent intent = getIntent();
        dtlnya = (Item) intent.getSerializableExtra("kirim");

        initScannerView();

    }

    @Override
    public void onResume(){
        super.onResume();
        initView();
    }

    private void initScannerView() {
        mScannerView = new ZXingScannerView(ScannerActivity.this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(this);
        camera.addView(mScannerView);
        mScannerView.startCamera();
    }

    @Override
    public void onStart() {
        mScannerView.startCamera();
        doRequestPermission();
        super.onStart();
    }

    private void doRequestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScannerView();
            } else {

            }
        }
    }

    @Override
    public void onPause(){
        mScannerView.stopCamera();
        super.onPause();
    }

    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data_scan_ArrayList.add(new data_scan(result));
                for (int i = 0; i < data_scan_ArrayList.size(); i++) {
                    for (int j = i + 1 ; j < data_scan_ArrayList.size(); j++) {
                        if (data_scan_ArrayList.get(i).getDatascan().equals(data_scan_ArrayList.get(j).getDatascan())) {
                            data_scan_ArrayList.remove(i);
                        }
                    }
                }


                sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(data_scan_ArrayList);
                editor.putString("datanya", json);
                editor.commit();
                initView();

            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void initView(){
        mScannerView.resumeCameraPreview(this::handleResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lihat_data:
                Intent in = new Intent(ScannerActivity.this, hasil_scan.class);
                in.putExtra("kirim", dtlnya.getId_sjp());
                startActivity(in);
                break;

        }
        return true;
    }
}
