package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.qrcodescanner.adapter.Adapter_Scan;
import com.example.qrcodescanner.get_set.Item_sjp;
import com.example.qrcodescanner.get_set.data_scan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class hasil_scan extends AppCompatActivity {
    Adapter_Scan adapter;
    private ArrayList<data_scan> data_scan_ArrayList = new ArrayList<>();
    RecyclerView list_scan;
    String bar;
    Button btn_simpan;
    String data_kirim, json;
    apidata mApiService;
    RestManager restManager;
    ProgressDialog loading;
    private APIService mAPIService;
    ArrayList<String> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_scan);
        Bundle extras = getIntent().getExtras();
        data_kirim= extras.getString("kirim");
        list_scan = findViewById(R.id.list_scan);
        btn_simpan = findViewById(R.id.btn_simpan);
        restManager = new RestManager();
        mAPIService = restManager.ambil_data_sjp();

        builder = new AlertDialog.Builder(this);
        list = new ArrayList<>();



        loadData();
        buildRecyclerView();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(hasil_scan.this, null, "Harap Tunggu...", true, false);
                //Toast.makeText(hasil_scan.this, new Gson().toJson(data_scan_ArrayList), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < data_scan_ArrayList.size(); i++) {
                    bar = replaceString(data_scan_ArrayList.get(i).getDatascan());
                    Toast.makeText(hasil_scan.this, bar, Toast.LENGTH_LONG).show();
                    if (bar.contains("/") || bar.contains(",")) {
                        bar = data_scan_ArrayList.get(i).getDatascan().replace("/", "replace").replace(",", "koma");
                        list.add(bar);
                        sendPost(data_kirim, bar, adapter);
                        btn_simpan.setEnabled(false);
                    } else {
                        list.add(bar);
                        sendPost(data_kirim, bar, adapter);
                        btn_simpan.setEnabled(false);
                    }

                }
            }
        });
    }


    public void sendPost(String id_sjp, String barcode, Adapter_Scan adapter) {
        mAPIService.savePost(id_sjp, barcode).enqueue(new Callback<com.example.qrcodescanner.Response>() {
            @Override
            public void onResponse(Call<com.example.qrcodescanner.Response> call, Response<com.example.qrcodescanner.Response> response) {
                loading.dismiss();
                com.example.qrcodescanner.Response data = response.body();
                sharedPreferences = getSharedPreferences("shared preferences", 0);
                sharedPreferences.edit().remove("datanya").commit();
                adapter.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(hasil_scan.this, new Gson().toJson(data), Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<com.example.qrcodescanner.Response> call, Throwable t) {
                Toast.makeText(hasil_scan.this, "Error Mssg : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        });
    }

    private void buildRecyclerView() {
        adapter = new Adapter_Scan(data_scan_ArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list_scan.setHasFixedSize(true);
        list_scan.setLayoutManager(manager);
        adapter.setOnItemListenerListener(new Adapter_Scan.OnItemListener() {
            @Override
            public void OnItemLongClickListener(View view, int position) {
                builder.setMessage("Yakin ingin menghapus data?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                data_scan_ArrayList.remove(position);
                                list_scan.setAdapter(adapter);

                                sharedPreferences = getSharedPreferences("shared preferences", 0);
                                sharedPreferences.edit().remove("datanya").commit();

                                sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(data_scan_ArrayList);
                                editor.putString("datanya", json);
                                editor.commit();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Hapus");
                alertDialog.show();
            }
        });

        list_scan.setAdapter(adapter);
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("datanya", null);
        Type type = new TypeToken<ArrayList<data_scan>>() {}.getType();
        data_scan_ArrayList = gson.fromJson(json, type);
            
            if (data_scan_ArrayList == null) {
                data_scan_ArrayList = new ArrayList<>();
                btn_simpan.setEnabled(false);
            }
    }

    String replaceString(String string) {
        return string.replaceAll("[^a-zA-Z0-9-/,.]","");
    }
}

