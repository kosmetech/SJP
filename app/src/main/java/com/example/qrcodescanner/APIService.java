package com.example.qrcodescanner;

import com.example.qrcodescanner.get_set.Item_sjp;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

//    @Headers({"Content-Type: application/json; charset=UTF-8"})
//    @POST("add_sjp")
//    @FormUrlEncoded
//    Call<ResponseSukses> savePost(@Field("id_sjp") String id_sjp,
//                             @Field("barcode") ArrayList<String> barcode);

//    @Headers({"Content-Type: application/json; charset=UTF-8"})
//    @POST("add_sjp")
//    @FormUrlEncoded
//    Call<ArrayList<String>> savePost(@Field("id_sjp") String id_sjp,
//                                  @Field("barcode") ArrayList<String> barcode);

    @GET("add_sjp/{id_sjp}/{barcode}")
    Call<Response> savePost(@Path("id_sjp") String id_sjp,
                                     @Path(value = "barcode", encoded = true) String barcode);


}
