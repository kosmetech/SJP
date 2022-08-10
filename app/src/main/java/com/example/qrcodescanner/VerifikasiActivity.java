package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodescanner.adapter.BatchAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifikasiActivity extends AppCompatActivity {

    String id, barcode, bar;
    APIService apiService;
    BatchAdapter adapter;
    ArrayList<ResponseKonfirmasi> list;
    TextView nama, serialisasi, totalperkarton, totalpcs;
    RecyclerView recyclerView;
    Button konfirmasi, batal;
    ResponseVer responseVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        id = getIntent().getStringExtra("id");
        barcode = getIntent().getStringExtra("barcode");

        nama = findViewById(R.id.txt_nama);
        serialisasi = findViewById(R.id.txt_serialisasi);
        totalperkarton = findViewById(R.id.txt_total_perkarton);
        totalpcs = findViewById(R.id.txt_total);
        recyclerView = findViewById(R.id.rv_batch);
        konfirmasi = findViewById(R.id.btn_konfirmasi);
        batal = findViewById(R.id.btn_batal);

        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        apiService = Api.getClient2().create(APIService.class);

        getData(barcode, id);

        if (barcode.contains("/") || barcode.contains(",")) {
            bar = barcode.replace("/", "replace").replace(",", "koma");
            konfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Api.getClient().create(APIService.class).verifikasi(id, bar).enqueue(new Callback<ResponseVer>() {
                        @Override
                        public void onResponse(Call<ResponseVer> call, Response<ResponseVer> response) {
                            responseVer = response.body();
                            Toast.makeText(VerifikasiActivity.this, responseVer.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(VerifikasiActivity.this, ScannerFGActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseVer> call, Throwable t) {
                            Toast.makeText(VerifikasiActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            konfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Api.getClient().create(APIService.class).verifikasi(id, barcode).enqueue(new Callback<ResponseVer>() {
                        @Override
                        public void onResponse(Call<ResponseVer> call, Response<ResponseVer> response) {
                            responseVer = response.body();
                            Toast.makeText(VerifikasiActivity.this, responseVer.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(VerifikasiActivity.this, ScannerFGActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseVer> call, Throwable t) {
                            Toast.makeText(VerifikasiActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        list = new ArrayList<>();



        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerifikasiActivity.this, ScannerFGActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void getData(String barcode, String id){
        apiService.getData(barcode).enqueue(new Callback<ArrayList<ResponseKonfirmasi>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseKonfirmasi>> call, Response<ArrayList<ResponseKonfirmasi>> response) {
                list = response.body();
                String substr = list.get(0).getBarcode().substring(list.get(0).getBarcode().length() - 12);
                serialisasi.setText(substr);
                totalperkarton.setText(list.get(0).getTotalKarton() + " Karton");
                totalpcs.setText(list.get(0).getTotalPcs() + " Pcs");
                nama.setText(list.get(0).getNama());
                adapter = new BatchAdapter(VerifikasiActivity.this, list);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<ResponseKonfirmasi>> call, Throwable t) {

            }
        });
    }
}