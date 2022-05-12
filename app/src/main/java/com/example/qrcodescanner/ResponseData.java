package com.example.qrcodescanner;

import com.example.qrcodescanner.get_set.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseData implements Serializable {
    ArrayList<Item> item;

    public ArrayList<Item> getItem() {
        return item;
    }

    public void setItem(ArrayList<Item> item) {
        this.item = item;
    }
}
