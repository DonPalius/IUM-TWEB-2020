package com.ripetizioni.app.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ripetizioni.app.R;
import com.ripetizioni.app.model.Booking;
import com.ripetizioni.app.model.User;
import com.ripetizioni.app.utils.LoginManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {

    public interface BookingConfirmation {
        void onBookingDeleted(Booking b);
        void onBookingConfirmed(Booking b);
    }


    private BookingConfirmation bookingConfirmation;

    private List<Booking> mData;
    private String username;

    public BookingsAdapter(BookingConfirmation bookingConfirmation) {
        mData = new ArrayList<>();
        this.bookingConfirmation = bookingConfirmation;
        User user = LoginManager.getUser();
        username = user.getName();
    }


    public void setmData(List<Booking> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_slot, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Booking booking = mData.get(position);

        boolean ownBooking = booking.getUser().equals(username);

        holder.bookTextView.setText(booking.getDate());
        holder.courseTextView.setText(booking.getCourse());
        holder.startTextView.setText(booking.getStart());
        holder.teacherTextView.setText(booking.getTeacher() + "" + booking.getStatusNumber());
        holder.statusTextView.setText(booking.getStatus());
        holder.tvUser.setText(booking.getUser());
        holder.tvUser.setVisibility(ownBooking ? View.GONE : View.VISIBLE);

        holder.confirmButton.setVisibility(View.GONE);
        holder.statusTextView.setVisibility(View.GONE);


        if (booking.isCancelled()) {
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setTextColor(Color.parseColor("#DF0B0B"));
        } else if(booking.isConfirmed()) {
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setTextColor(ContextCompat.getColor(holder.bookTextView.getContext(), R.color.colorPrimary));
        }

        if(booking.isinit()) {


            Date dateTime = booking.getDateTime();

            if (Calendar.getInstance().getTime().before(dateTime)) {

                    holder.confirmButton.setVisibility(View.VISIBLE);
                    holder.confirmButton.setText(R.string.annulla);
                    holder.confirmButton.setOnClickListener(view -> bookingConfirmation.onBookingDeleted(booking));
            } else if (ownBooking) {
                holder.confirmButton.setVisibility(View.VISIBLE);
                holder.confirmButton.setText(R.string.confirm);
                holder.confirmButton.setOnClickListener(view -> bookingConfirmation.onBookingConfirmed(booking));
            }
        }



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookTextView;
        TextView startTextView;
        TextView statusTextView;
        TextView teacherTextView;
        TextView courseTextView,tvUser;
        Button confirmButton;


        MyViewHolder(View itemView) {
            super(itemView);
            bookTextView = itemView.findViewById(R.id.tvDate);
            startTextView = itemView.findViewById(R.id.tvStart);
            statusTextView = itemView.findViewById(R.id.tvStatus);
            teacherTextView = itemView.findViewById(R.id.tvTeacher);
            courseTextView = itemView.findViewById(R.id.tvCourse);
            confirmButton = itemView.findViewById(R.id.btnConfirm);
            tvUser = itemView.findViewById(R.id.tvUser);

        }

    }
}
