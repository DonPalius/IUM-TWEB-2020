package com.ripetizioni.app.model;

import com.google.gson.annotations.SerializedName;

public class ServerError {
    @SerializedName("error")
    private String message;

    public String getMessage() {
        return message;
    }
}
