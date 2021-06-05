package com.sevrep.myinstagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName;
    private EditText edtPunchSpeed;
    private EditText edtPunchPower;
    private EditText edtKickSpeed;
    private EditText edtKickPower;

    private StringBuilder allKickboxers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);

        Button btnGetAllData = findViewById(R.id.btnGetAllData);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnNextActivity = findViewById(R.id.btnNextActivity);

        btnGetAllData.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnNextActivity.setOnClickListener(this);

        TextView txtGetData = findViewById(R.id.txtGetData);
        txtGetData.setOnClickListener(v -> {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Kickboxer");
            parseQuery.getInBackground("06chIsc8gm", (object, e) -> {
                if (object != null && e == null) {
                    String stringData = "Name: " + object.get("name") + "\n" + "Punch speed: " + object.get("punchSpeed") + "\n" + "Punch power: " + object.get("punchPower") + "\n" + "Kick speed: " + object.get("kickSpeed") + "\n" + "Kick speed: " + object.get("kickPower");
                    txtGetData.setText(stringData);
                } else {
                    FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
            });
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnSave) {
            try {
                final ParseObject kickBoxer = new ParseObject("Kickboxer");
                kickBoxer.put("name", edtName.getText().toString().trim());
                kickBoxer.put("punchSpeed", Integer.parseInt(edtPunchSpeed.getText().toString().trim()));
                kickBoxer.put("punchPower", Integer.parseInt(edtPunchPower.getText().toString().trim()));
                kickBoxer.put("kickSpeed", Integer.parseInt(edtKickSpeed.getText().toString().trim()));
                kickBoxer.put("kickPower", Integer.parseInt(edtKickPower.getText().toString().trim()));
                kickBoxer.saveInBackground(e -> {
                    if (e == null) {
                        FancyToast.makeText(SignUpActivity.this, kickBoxer.get("name") + " saved successfully!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                });
            } catch (Exception e) {
                FancyToast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            }
        } else if (viewId == R.id.btnGetAllData) {
            allKickboxers = new StringBuilder();
            ParseQuery<ParseObject> parseQueryAll = ParseQuery.getQuery("Kickboxer");
            parseQueryAll.whereGreaterThanOrEqualTo("punchPower", 1500);
            parseQueryAll.setLimit(2);
            parseQueryAll.findInBackground((objects, e) -> {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject kickboxers : objects)
                            allKickboxers.append(kickboxers.get("name")).append(" punchPower: ").append(kickboxers.get("punchPower")).append("\n");
                        FancyToast.makeText(SignUpActivity.this, allKickboxers, Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(SignUpActivity.this, "objects is empty", Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                } else {
                    FancyToast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
            });
        } else if (viewId == R.id.btnNextActivity) {
            Intent nextActivity = new Intent(this, SignUpLoginActivity.class);
            startActivity(nextActivity);
            this.finish();
        }
    }
}