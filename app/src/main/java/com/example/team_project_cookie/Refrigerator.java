package com.example.team_project_cookie;

import android.content.Context;

import java.util.HashMap;

public class Refrigerator {

    private String item;
    private String count;
    private String img_path;
    private Context context;
    public static HashMap<String, String> recipe_material = new HashMap<String, String>();

    public Refrigerator(Context context, String item, String count, String img_path) {
        this.context = context;
        this.item = item;
        this.count = count;
        this.img_path = img_path;
        recipe_material.put(item,item);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
