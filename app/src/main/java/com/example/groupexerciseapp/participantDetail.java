package com.example.groupexerciseapp;

public class participantDetail {
    String title, describe, post_name, post_date;

    public participantDetail(String title, String describe, String post_name, String post_date) {
        this.title = title;
        this.describe = describe;
        this.post_name = post_name;
        this.post_date = post_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
