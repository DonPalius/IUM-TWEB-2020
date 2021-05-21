package com.ripetizioni.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("course")
    @Expose
    private String course;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("start")
    @Expose
    private String start;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("teacher")
    @Expose
    private String teacher;


    public String getCourse() {
        return course;
    }


    public String getDateISO() {
        return date;
    }

    public String getDate() {


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

    public String getStart() {
        final String OLD_FORMAT = "kk:mm:ss";
        final String NEW_FORMAT = "kk:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date s;

        try {
            s = sdf.parse(start);
            sdf.applyPattern(NEW_FORMAT);
            return sdf.format(s);

        } catch (Exception ignored) { }

        return date;
    }

    public Date getDateTime() {
        final String OLD_FORMAT = "yyyy-MM-dd kk:mm:ss";
        try {
            return new SimpleDateFormat(OLD_FORMAT).parse(date + " " + start);
        } catch (ParseException e) {
            return null;
        }


    }

    public String getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public String getTeacher() {
        return teacher;
    }

    public boolean isConfirmed() {
        return status == 30;
    }


    public boolean isinit() {
        return status == 10;
    }

    public boolean isCancelled() {
        return status == 20;
    }
    public int getStatusNumber() {
        return status;
    }
    public String getStatus() {
        switch(status) {
            case 10:
                return "Attiva";
            case 20:
                return "Cancellata";
            case 30:
                return "Confermata";
            default:
                return null;
        }
    }

}
