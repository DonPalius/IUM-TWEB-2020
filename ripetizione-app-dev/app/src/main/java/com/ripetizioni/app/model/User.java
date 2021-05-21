package com.ripetizioni.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String name;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("is_admin")
    private boolean admin;


    public String getName() {
        return name;
    }


    public String getToken() {
        return token;
    }

    public boolean isAdmin() {
        return admin;
    }
}
