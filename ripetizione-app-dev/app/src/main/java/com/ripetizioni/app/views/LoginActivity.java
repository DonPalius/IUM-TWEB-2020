package com.ripetizioni.app.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.ripetizioni.app.R;
import com.ripetizioni.app.api.RetrofitClient;
import com.ripetizioni.app.utils.LoginManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private Button btnLogin;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (LoginManager.isLoggedIn()) {
            mainAct();
            return;
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> validate(etUsername.getText().toString(),etPassword.getText().toString()));
        findViewById(R.id.tvInfo).setOnClickListener(v -> mainAct());

    }

    private void validate (String userName, String userPassword){
        AlertDialog progressDialog = getProgressDialog(LoginActivity.this);
        progressDialog.show();

        Disposable subscribe = RetrofitClient.getApi().login(userName, userPassword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    (u) -> {
                        progressDialog.hide();
                        LoginManager.setUser(u);
                        mainAct();
                    },
                    (e)-> {
                        progressDialog.hide();
                        showErrorMessage(e);
                    }
            );
    }

    public void mainAct() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
