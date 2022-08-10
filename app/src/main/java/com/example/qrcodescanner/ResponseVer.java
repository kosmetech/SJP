package com.example.qrcodescanner;

import com.google.gson.annotations.SerializedName;

import java.nio.file.WatchService;

public class ResponseVer {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("barcode")
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
