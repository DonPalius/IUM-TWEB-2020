package com.ripetizioni.app.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ripetizioni.app.R;
import com.ripetizioni.app.adapter.BookingsAdapter;
import com.ripetizioni.app.api.RetrofitClient;
import com.ripetizioni.app.model.Booking;
import com.ripetizioni.app.utils.BookingCreated;
import com.ripetizioni.app.utils.ErrorUtils;
import com.ripetizioni.app.utils.LoginManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class BookingsFragment extends Fragment implements BookingsAdapter.BookingConfirmation {


    private BookingsAdapter bookingsAdapter;
    RecyclerView recyclerView;
    LinearLayout llProgress, llError;
    TextView txtError;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        llProgress = view.findViewById(R.id.linearProgressive);
        llProgress.setVisibility(View.VISIBLE);

        llError = view.findViewById(R.id.llError);
        txtError = view.findViewById(R.id.txtError);
        view.findViewById(R.id.btnRetry).setOnClickListener((v) -> loadBookings());


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingsAdapter = new BookingsAdapter(this);
        recyclerView.setAdapter(bookingsAdapter);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public final void onBookingCreatedEvent(BookingCreated e) {
        loadBookings();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }

    private void showResult(Throwable e) {
        llProgress.setVisibility(View.GONE);

        llError.setVisibility(View.GONE);

        if (e == null) {
            llError.setVisibility(View.GONE);
        } else {
            llError.setVisibility(View.VISIBLE);
            showError(e);
        }
    }


    private void showError(Throwable e) {
        String errorMessage = ErrorUtils.getErrorMessage(e);
        txtError.setText(errorMessage);

    }



    public void loadBookings() {
        if(!LoginManager.isLoggedIn()) {
            showResult(new Exception("Login necessario."));
            return;
        }

        llProgress.setVisibility(View.VISIBLE);

        RetrofitClient.getApi().getBookings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                (list)-> {
                    bookingsAdapter.setmData(list);
                   showResult(null);

                },
                (e) -> showResult(e)
        );


    }



    private void bookingDelete(Booking b) {

        RetrofitClient.getApi().deleteBookings(b.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                (u)-> loadBookings(),
                (e) -> ErrorUtils.showErrorMessage(getContext(), e));
    }


    private void bookingConfirmed(Booking b) {

        RetrofitClient.getApi().confirmBookings(b.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                (u)-> loadBookings(),
                (e) -> ErrorUtils.showErrorMessage(getContext(), e));
    }


    @Override
    public void onBookingDeleted(Booking b) {
        bookingDelete(b);
    }

    @Override
    public void onBookingConfirmed(Booking b) {
        bookingConfirmed(b);
    }

}
