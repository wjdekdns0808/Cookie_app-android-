package com.example.team_project_cookie;

import android.content.Context;

public class Recipe_list_Info {
    private String name;
    private String intro;
    private String time;
    private String rating;
    private String img_path;
    private Context context;

    public Recipe_list_Info(Context context, String img_path, String name, String intro, String time, String rating) {
        this.img_path = img_path;
        this.name = name;
        this.intro = intro;
        this.time = time;
        this.rating = rating;
        this.context = context;
    }

    public Recipe_list_Info() {}

    public Context getContext() { return context; }

    public void getContext(Context context) { this.context = context; }

    public String getImg_path() { return img_path; }

    public void getImg_path(String img_path) { this.img_path = img_path; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

