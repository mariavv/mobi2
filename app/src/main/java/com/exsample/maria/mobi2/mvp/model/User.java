package com.exsample.maria.mobi2.mvp.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maria on 10.03.2018
 */

@IgnoreExtraProperties
public class User{
    private String email;
    private String name;
    private String phone;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public User() {
    }

    public User(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", email);
        result.put("author", name);
        result.put("title", phone);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
