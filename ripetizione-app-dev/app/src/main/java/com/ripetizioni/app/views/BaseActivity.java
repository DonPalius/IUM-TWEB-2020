package com.ripetizioni.app.views;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ripetizioni.app.R;
import com.ripetizioni.app.utils.ErrorUtils;
import com.ripetizioni.app.utils.LoginManager;
import com.ripetizioni.app.utils.UnauthorizedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public abstract class BaseActivity extends AppCompatActivity {

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
    public final void onUnauthorizedEvent(UnauthorizedEvent e) {
        handleUnauthorizedEvent();
    }

    protected void handleUnauthorizedEvent() {
        LoginManager.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showErrorMessage(Throwable e) {
        ErrorUtils.showErrorMessage(getApplicationContext(), e);
    }

    public AlertDialog getProgressDialog(Context context) {
        return getProgressDialog(context, null);
    }

    public AlertDialog getProgressDialog(Context context, String title) {

        View view = getLayoutInflater().inflate(R.layout.progress_dialog, null);
        TextView t = view.findViewById(R.id.progressTitle);
        if (title != null)
        t.setText(title);

        return new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog)
                .setView(view).setCancelable(false).create();
    }
}

