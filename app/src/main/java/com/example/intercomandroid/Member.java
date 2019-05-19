package com.example.intercomandroid;

import android.media.Image;



public class Member {
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public android.media.Image getImage() {
        return Image;
    }

    public void setImage(android.media.Image image) {
        Image = image;
    }

    private String Name;
    private Integer Age;
    private Image Image;
}
