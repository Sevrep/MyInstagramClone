package com.sevrep.myinstagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail;
    private EditText edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.login));

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        Button btnLoginSign = findViewById(R.id.btnLoginSign);
        Button btnLoginLogin = findViewById(R.id.btnLoginLogin);

        btnLoginSign.setOnClickListener(this);
        btnLoginLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnLoginSign) {
            goToSign();
        } else if (viewId == R.id.btnLoginLogin) {
            try {
                ParseUser.logInInBackground(edtLoginEmail.getText().toString().trim(), edtLoginPassword.getText().toString().trim(), (user, e) -> {
                    if (user != null && e == null) {
                        FancyToast.makeText(this, "Welcome, " + user.get("username") + "!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        clearLogin();
                    } else {
                        FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                });
            } catch (Exception e) {
                FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            }
            hideKeypad(v);
        }
    }

    private void hideKeypad(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void goToSign() {
        Intent nextActivity = new Intent(this, SignUpActivity.class);
        startActivity(nextActivity);
        this.finish();
    }

    private void clearLogin() {
        edtLoginEmail.setText("");
        edtLoginPassword.setText("");
    }

    @Override
    public void onBackPressed() {
        goToSign();
        this.finish();
    }
}