package com.ripetizioni.app.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.ripetizioni.app.R;
import com.ripetizioni.app.adapter.SlotsAdapter;
import com.ripetizioni.app.adapter.WrappedSlot;
import com.ripetizioni.app.api.RetrofitClient;
import com.ripetizioni.app.model.Course;
import com.ripetizioni.app.model.Slot;
import com.ripetizioni.app.model.Teacher;
import com.ripetizioni.app.utils.BookingCreated;
import com.ripetizioni.app.utils.ErrorUtils;
import com.ripetizioni.app.utils.LoginManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class CalendarFragment extends Fragment implements SlotsAdapter.SlotSelected {

    private ArrayAdapter<Course> coursesAdapter;
    private ArrayAdapter<Teacher> teachersAdapter;
    private Course selectedCourse;
    private Teacher selectedTeacher;
    private SlotsAdapter slotsAdapter;
    private Spinner spinnerTeachers;
    RecyclerView recyclerView;
    LinearLayout llProgress, llError;
    TextView  txtError, noAvailability;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        llProgress = view.findViewById(R.id.linearProgressive);
        llProgress.setVisibility(View.VISIBLE);

        llError = view.findViewById(R.id.llError);
        txtError = view.findViewById(R.id.txtError);
        noAvailability = view.findViewById(R.id.noAvailability);

        Spinner courses = view.findViewById(R.id.courses_spinner);

        view.findViewById(R.id.btnRetry).setOnClickListener((v) -> loadCourses());

        coursesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        courses.setAdapter(coursesAdapter);

        courses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseChanged(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTeachers = view.findViewById(R.id.teachers_spinner);

        teachersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        spinnerTeachers.setAdapter(teachersAdapter);

        spinnerTeachers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                teacherChanged(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        slotsAdapter = new SlotsAdapter(this);
        recyclerView.setAdapter(slotsAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadCourses();

        return view;
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

    private void changeData(List<Slot> slots) {

        List<WrappedSlot> collect = slots.stream().collect(groupingBy(Slot::getDate, toList())).values().stream().map((x) -> {
            String date = x.get(0).getDate();
            return new WrappedSlot(date, x);
        }).sorted((w1,w2)-> w1.getTitle().compareTo(w2.getTitle())) .collect(toList());

        noAvailability.setVisibility(collect.isEmpty() ? View.VISIBLE : View.GONE);
        slotsAdapter.changeData(collect);

    }


    private void courseChanged(int i) {
        selectedCourse = coursesAdapter.getItem(i);
        teachersAdapter.clear();
        teachersAdapter.addAll(selectedCourse.getTeachers());
        spinnerTeachers.setVisibility(View.VISIBLE);
        teacherChanged(0);

    }

    private void loadCourses() {
        llProgress.setVisibility(View.VISIBLE);
        RetrofitClient.getApi().getCourses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                (list)-> {
                    coursesAdapter.addAll(list);
                    courseChanged(0);
                },
                this::showResult

        );
    }

    private void teacherChanged(int i) {
        selectedTeacher = teachersAdapter.getItem(i);
        changeData(new ArrayList<>());
        loadSlots();
    }



    private void loadSlots(){
        RetrofitClient.getApi().getSlots(selectedTeacher.getId(), selectedCourse.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                (u) -> {
                    changeData(u);
                    this.showResult(null);
                },
                this::showResult
        );

    }

    private void confirmBooking(Slot s) {

        if(LoginManager.isLoggedIn()) {
            String m = selectedCourse.getName() + " - " + selectedTeacher.getName() +
                    "\n" + s.getDate() + " - " + s.getStart();
            new AlertDialog.Builder(getContext())
                    .setTitle("Confermi la prenotazione?")
                    .setMessage(m)
                    .setPositiveButton(R.string.conferma, (dialog, whichButton) ->
                            RetrofitClient.getApi().createBookings(selectedCourse.getId(),s.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                    (u) -> bookingreated(),
                                    (e) -> ErrorUtils.showErrorMessage(getContext(), e))
                    ).show();
        }
        else{
            new AlertDialog.Builder(getContext())
                    .setTitle("Impossibile effettuare la prenotazione")
                    .setMessage("Effettua il login per poseguire")
                    .setNeutralButton(R.string.login,(dialog, whichButton) -> startActivity(new Intent(getContext(),LoginActivity.class))).show();
        }
    }

    private void bookingreated() {
        llProgress.setVisibility(View.VISIBLE);
        loadSlots();

        EventBus.getDefault().post(BookingCreated.instance());
        try {
            TabLayout tabs = getActivity().findViewById(R.id.tab_layout);

            BadgeDrawable badge = tabs.getTabAt(1).getOrCreateBadge();

            badge.setVisible(true);

        } catch(Exception ignored) {}
    }

    @Override
    public void onSlotsSelected(Slot s) {
        confirmBooking(s);

    }
}
