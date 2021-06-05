package com.sevrep.myinstagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView txtWelcome = findViewById(R.id.txtWelcome);
        String welcomeString = "Welcome, " + ParseUser.getCurrentUser().get("username");
        txtWelcome.setText(welcomeString);

        findViewById(R.id.btnLogout).setOnClickListener(e -> {
            ParseUser.logOut();
            goBack();
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Intent signUpLoginActivity = new Intent(this, SignUpLoginActivity.class);
        startActivity(signUpLoginActivity);
        this.finish();
    }
}