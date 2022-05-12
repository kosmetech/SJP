package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrcodescanner.adapter.adapter_sjp;
import com.example.qrcodescanner.get_set.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class data_sjp extends AppCompatActivity implements adapter_sjp.data_kirim {
    private RecyclerView recyclerView;
    private RestManager restManager;
    SearchView search;
    private adapter_sjp adapter;
    SwipeRefreshLayout refreshLayout;
    ProgressDialog loading;
    List<Item> sjp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sjp);
        recyclerView = findViewById(R.id.list_sjp);
        refreshLayout = findViewById(R.id.swipe_refresh);

        recyclerView.setHasFixedSize(true);
        //search = findViewById(R.id.search_bar);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        adapter = new adapter_sjp(this);
        recyclerView.setAdapter(adapter);
        get_data_sjp();

        sjp = new ArrayList<>();

//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                adapter.getFilter().filter(s);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapter.getFilter().filter(s);
//                return true;
//            }
//        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                get_data_sjp();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void get_data_sjp() {
        restManager = new RestManager();
        Call<List<Item>> listCall = restManager.ambil_data().getsjp();
        loading = ProgressDialog.show(data_sjp.this, null, "Harap Tunggu...", true, false);
        listCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    sjp = response.body();
                    for (int i = 0 ; i < sjp.size() ;i++){
                        Item daftar  = sjp.get(i);
                        adapter.adddata(daftar);
                        loading.dismiss();
                    }
                    loading.dismiss();

                }else {
                    loading.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "cek koneksi internet", Toast.LENGTH_SHORT).show();
                Log.d("hasilnya ", t.toString());
            }

        });
    }
    @Override
    public void onClick(int position) {
//        Item kirim_id = adapter.getAmbildata(position);
//        Intent intent = new Intent(data_sjp.this, MainActivity.class);
//        intent.putExtra("kirim", kirim_id);
//        startActivity(intent);

        // for development purpose only
        Item kirim_id = adapter.getAmbildata(position);
        Intent intent = new Intent(data_sjp.this, ScannerActivity.class);
        intent.putExtra("kirim", kirim_id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Item> itemFilter = new ArrayList<>();
                for (Item model : sjp){
                    String no = model.getNomor_sjp().toLowerCase();
                    if (no.contains(newText)){
                        itemFilter.add(model);
                    }
                }
                adapter.setFilter(itemFilter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}