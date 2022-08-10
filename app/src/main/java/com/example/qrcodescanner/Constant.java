package com.example.qrcodescanner;


public class Constant {
    public static final String URL = "https://app.sandbox-kosme.com/api/";
    private Constant() {}


    public static APIService getAPIService() {

        return RetrofitClient.getClient(URL).create(APIService.class);
    }

}
