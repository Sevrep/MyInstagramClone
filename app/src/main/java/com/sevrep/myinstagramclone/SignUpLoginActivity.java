package com.sevrep.myinstagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsernameSignUp;
    private EditText edtPasswordSignUp;
    private EditText edtUsernameLogin;
    private EditText edtPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);

        edtUsernameSignUp = findViewById(R.id.edtUsernameSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        edtUsernameLogin = findViewById(R.id.edtUsernameLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnSignUp) {
            try {
                final ParseUser appUser = new ParseUser();
                appUser.setUsername(edtUsernameSignUp.getText().toString().trim());
                appUser.setPassword(edtPasswordSignUp.getText().toString().trim());
                appUser.signUpInBackground(e -> {
                    if (e == null) {
                        FancyToast.makeText(this, appUser.get("username") + " saved successfully!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        clearSignUp();
                    } else {
                        FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                });
            } catch (Exception e) {
                FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            }
        } else if (viewId == R.id.btnLogin) {
            try {
                ParseUser.logInInBackground(edtUsernameLogin.getText().toString().trim(), edtPasswordLogin.getText().toString().trim(), (user, e) -> {
                    if (user != null && e == null) {
                        FancyToast.makeText(this, "Welcome, " + user.get("username") + "!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        clearLogin();
                        goToWelcome();
                    } else {
                        FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                });
            } catch (Exception e) {
                FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            }
        }
    }

    private void goToWelcome() {
        Intent nextActivity = new Intent(this, WelcomeActivity.class);
        startActivity(nextActivity);
        this.finish();
    }

    private void clearLogin() {
        edtUsernameLogin.setText("");
        edtPasswordLogin.setText("");
    }

    private void clearSignUp() {
        edtUsernameSignUp.setText("");
        edtPasswordSignUp.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent signUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivity);
        this.finish();
    }

}
