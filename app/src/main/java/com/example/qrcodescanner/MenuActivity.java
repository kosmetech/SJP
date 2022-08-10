package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class MenuActivity extends AppCompatActivity {

    FrameLayout btnsjp, btnfg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnfg = findViewById(R.id.btn_mutasi);
        btnsjp = findViewById(R.id.btn_sjp);

        //btnfg.setVisibility(View.GONE);

        btnsjp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, data_sjp.class);
                startActivity(i);
            }
        });

        btnfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, DataMutasiActivity.class);
                startActivity(i);

            }
        });

    }
}