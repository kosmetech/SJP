package com.example.qrcodescanner;

import com.example.qrcodescanner.get_set.Item;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface apidata {


    @GET("sjp/list-ckg")
    Call<List<Item>> getsjp();

    @GET("sjp")
    Call<List<Karton>> get_data_barcode(@Query("barcode") String barcode);

    @FormUrlEncoded
    @POST("add_sjp")
    Call<ResponseBody> add_sjp(@Field("id_sjp") String id_sjp,
                                  @Field("barcode") String barcode);


}
