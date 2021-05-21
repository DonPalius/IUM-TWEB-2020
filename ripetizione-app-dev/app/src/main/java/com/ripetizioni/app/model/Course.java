package com.ripetizioni.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class Course {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("teachers")
    @Expose
    private ArrayList<Teacher> teachers;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
