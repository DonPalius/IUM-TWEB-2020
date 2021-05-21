package com.ripetizioni.app.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ripetizioni.app.model.ServerError;

import java.io.InterruptedIOException;

import retrofit2.HttpException;

public class ErrorUtils {
    private ErrorUtils() {}

    public static void showErrorMessage(Context context, Throwable e) {
        Toast.makeText(context, getErrorMessage(e), Toast.LENGTH_LONG).show();
    }


    public static String getErrorMessage( Throwable e) {
        if (e instanceof HttpException){
            try {
                ServerError serverError = new Gson().fromJson(((HttpException) e).response().errorBody().string(), ServerError.class);
                return serverError.getMessage();

            } catch (Exception ex) { }
        } else if (e instanceof InterruptedIOException) {
            return  "Impossibile contattare il server!";

        }

       return e.getMessage();
    }
}
