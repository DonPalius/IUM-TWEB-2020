package com.ripetizioni.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Slot implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("date")
    @Expose
    private String date;

    private String start;

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDateParsed(){
        return parseDate(date);
    }

    public String getStart() {
        return parseStart(start);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.date);
        dest.writeString(this.start);
    }


    protected Slot(Parcel in) {
        this.id = in.readInt();
        this.date = in.readString();
        this.start = in.readString();
    }

    private String parseDate(String date){

        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d;

        try {
            d = sdf.parse(date);
            sdf.applyPattern(NEW_FORMAT);
            return sdf.format(d);

        } catch (Exception ignored) { }

        return date;

    }

    private String parseStart(String start){

        final String OLD_FORMAT = "kk:mm:ss";
        final String NEW_FORMAT = "kk:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date s;

        try {
            s = sdf.parse(start);
            sdf.applyPattern(NEW_FORMAT);
            return sdf.format(s);

        } catch (Exception ignored) { }

        return start;

    }

    public static final Parcelable.Creator<Slot> CREATOR = new Parcelable.Creator<Slot>() {
        @Override
        public Slot createFromParcel(Parcel source) {
            return new Slot(source);
        }

        @Override
        public Slot[] newArray(int size) {
            return new Slot[size];
        }
    };
}
