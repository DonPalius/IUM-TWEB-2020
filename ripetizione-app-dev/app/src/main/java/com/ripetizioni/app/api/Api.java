package com.ripetizioni.app.api;

import com.ripetizioni.app.model.Booking;
import com.ripetizioni.app.model.Course;
import com.ripetizioni.app.model.Slot;
import com.ripetizioni.app.model.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {


    @POST("login")
    Single<User> login(
            @Query("username") String username,
            @Query("password") String password
    );


    @POST("logout")
    Single<Result<Void>> logout();


    @GET("courses")
    Single<List<Course>> getCourses();



    @GET("available_slots")
    Single<List<Slot>> getSlots(
            @Query("teacher") int idTeacher,
            @Query("course") int idCourse
    );

    @GET("bookings")
    Single<List<Booking>> getBookings();

    @POST("bookings")
    Single<Booking> createBookings(
            @Query("course") int idCourse,
            @Query("slot") int idSlot
    );

    @DELETE("bookings")
    Single<Response<Void>> deleteBookings(
            @Query("id") int idBooking
    );

    @PUT("bookings")
    Single<Booking> confirmBookings(
            @Query("id") int idBooking
    );



}