package com.sevrep.myinstagramclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseObject;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName;
    private EditText edtPunchSpeed;
    private EditText edtPunchPower;
    private EditText edtKickSpeed;
    private EditText edtKickPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            final ParseObject kickBoxer = new ParseObject("Kickboxer");
            kickBoxer.put("name", edtName.getText().toString().trim());
            kickBoxer.put("punchSpeed", Integer.parseInt(edtPunchSpeed.getText().toString().trim()));
            kickBoxer.put("punchPower", Integer.parseInt(edtPunchPower.getText().toString().trim()));
            kickBoxer.put("kickSpeed", Integer.parseInt(edtKickSpeed.getText().toString().trim()));
            kickBoxer.put("kickPower", Integer.parseInt(edtKickPower.getText().toString().trim()));
            kickBoxer.saveInBackground(e -> {
                if (e == null) {
                    FancyToast.makeText(SignUpActivity.this, kickBoxer.get("name") + " saved successfully!", Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                } else {
                    FancyToast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR,true).show();
        }

    }
}