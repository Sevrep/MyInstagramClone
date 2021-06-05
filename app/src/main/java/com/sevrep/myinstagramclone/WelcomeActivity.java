package com.sevrep.myinstagramclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void onBackPressed() {
        Intent signUpLoginActivity = new Intent(this, SignUpLoginActivity.class);
        startActivity(signUpLoginActivity);
        this.finish();
    }
}