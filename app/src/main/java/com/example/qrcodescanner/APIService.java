package com.example.qrcodescanner;

import com.example.qrcodescanner.get_set.Item_sjp;
import com.example.qrcodescanner.get_set.ListMutasi;
import com.example.qrcodescanner.get_set.ResponseFG;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("add_sjp/{id_sjp}/{barcode}")
    Call<Response> savePost(@Path("id_sjp") String id_sjp,
                                     @Path(value = "barcode", encoded = true) String barcode);

    @GET("sertem/list-gudang")
    Call<ArrayList<ResponseGudang>> getListMutasi();

    @GET("sertem/add/{id}/{barcode}")
    Call<ResponseFG> saveFG(@Path("id") String id,
                            @Path(value = "barcode", encoded = true) String barcode);

    @GET("sjp")
    Call<ArrayList<ResponseKonfirmasi>> getData(@Query("barcode") String barcode);

    @GET("sertem/verifikasi/{id_dokumen}/{barcode}")
    Call<ResponseVer> verifikasi(@Path("id_dokumen") String id,
                              @Path(value = "barcode", encoded = true) String barcode);

}
