package com.sevrep.myinstagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSignEmail;
    private EditText edtSignUsername;
    private EditText edtSignPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(getString(R.string.sign_up));

        edtSignEmail = findViewById(R.id.edtSignEmail);
        edtSignUsername = findViewById(R.id.edtSignUsername);
        edtSignPassword = findViewById(R.id.edtSignPassword);

        Button btnSign = findViewById(R.id.btnSign);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnSign.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnSign) {

            String email = edtSignEmail.getText().toString().trim();
            String username = edtSignUsername.getText().toString().trim();
            String password = edtSignPassword.getText().toString().trim();

            try {

                final ParseUser appUser = new ParseUser();
                appUser.setEmail(email);
                appUser.setUsername(username);
                appUser.setPassword(password);

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signing up user: " + username);
                progressDialog.show();

                appUser.signUpInBackground(e -> {
                    if (e == null) {
                        FancyToast.makeText(this, appUser.get("username") + " saved successfully!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        clearSignUp();
                    } else {
                        FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                    progressDialog.dismiss();
                });
            } catch (Exception e) {
                FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            }
            hideKeypad(v);
        } else if (viewId == R.id.btnLogin) {
            goToLogin();
        }
    }

    private void hideKeypad(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void goToLogin() {
        Intent nextActivity = new Intent(this, LoginActivity.class);
        startActivity(nextActivity);
        this.finish();
    }

    private void clearSignUp() {
        edtSignEmail.setText("");
        edtSignUsername.setText("");
        edtSignPassword.setText("");
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}