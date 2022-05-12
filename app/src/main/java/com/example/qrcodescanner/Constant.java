package com.example.qrcodescanner;


public class Constant {
    //public static final String URL = "https://ckr.sandbox-kosme.com/api/";
    public static final String URL = "https://app.sandbox-kosme.com/api/";
    public static final String URL_barcode = "http://tracker.kosme.co.id/gudang/";
    private Constant() {}


    public static APIService getAPIService() {

        return RetrofitClient.getClient(URL).create(APIService.class);
    }

}
