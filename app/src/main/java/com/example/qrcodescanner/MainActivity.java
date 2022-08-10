package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qrcodescanner.adapter.Adapter_Scan;
import com.example.qrcodescanner.get_set.Item;
import com.example.qrcodescanner.get_set.data_scan;
import com.google.gson.Gson;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    boolean CameraPermission = false;
    final int CAMERA_PERM = 1;
    String hasil_qr,data_scan_terakhir;
    private Adapter_Scan adapter;
    private ArrayList<data_scan> data_scan_ArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Item dtlnya;
    private rest_sjp restManager;
    ProgressDialog loading;
    apidata mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        Intent intent = getIntent();
        dtlnya = (Item) intent.getSerializableExtra("kirim");
        mCodeScanner = new CodeScanner(this, scannerView);
        askPermission();
        if (CameraPermission) {

            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCodeScanner.startPreview();
//                    data_scan last_array = data_scan_ArrayList.get(data_scan_ArrayList.size() - 1);
                    if (hasil_qr == null){

                    }else {
                        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(data_scan_ArrayList);
                        editor.putString("datanya", json);
                        editor.commit();
                    }

                        }
            });

            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull @NotNull Result result) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hasil_qr = result.getText();
                            Toast.makeText(MainActivity.this, hasil_qr, Toast.LENGTH_SHORT).show();
                            data_scan_ArrayList.add(new data_scan(hasil_qr));
                            for (int i = 0; i < data_scan_ArrayList.size(); i++) {
                                for (int j = i + 1 ; j < data_scan_ArrayList.size(); j++) {
                                    if (data_scan_ArrayList.get(i).getDatascan().equals(data_scan_ArrayList.get(j).getDatascan())) {
                                            data_scan_ArrayList.remove(i);
                                        }
                                }
                            }
                            int data = data_scan_ArrayList.size();
                            Toast.makeText(MainActivity.this, String.valueOf(data), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });

        }
    }

    private void askPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);


            } else {

                mCodeScanner.startPreview();
                CameraPermission = true;
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == CAMERA_PERM) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mCodeScanner.startPreview();
                CameraPermission = true;
            } else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("Please provide the camera permission for using all the features of the app")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    }).create().show();

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("You have denied some permission. Allow all permission at [Settings] > [Permissions]")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();


                                }
                            }).setNegativeButton("No, Exit app", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            finish();

                        }
                    }).create().show();


                }

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        if (CameraPermission) {
            mCodeScanner.releaseResources();
        }

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //Menthod ini digunakan untuk menangani kejadian saat OptionMenu diklik
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lihat_data:
                Intent in = new Intent(MainActivity.this, hasil_scan.class);
                in.putExtra("kirim", dtlnya.getId_sjp());
                startActivity(in);
                break;

        }
        return true;
    }
}