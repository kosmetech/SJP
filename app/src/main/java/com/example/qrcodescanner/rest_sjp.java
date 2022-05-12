package com.example.qrcodescanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class rest_sjp {
    private apidata data ;
    private Retrofit retrofit = null;

    private OkHttpClient getRequestHeader() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public Retrofit ambil_data_barcode(){

            Retrofit retrofit =new Retrofit.Builder()
                    .baseUrl(Constant.URL_barcode)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .client(getRequestHeader())
                    .build();


            //data = retrofit.create(apidata.class);

        return retrofit;
        //return data;
    }
}
