package com.sevrep.myinstagramclone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private EditText edtProfileName;
    private EditText edtBio;
    private EditText edtProfession;
    private EditText edtHobbies;
    private EditText edtSport;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final ParseUser parseUser = ParseUser.getCurrentUser();

        ConstraintLayout profileConstraintLayout = view.findViewById(R.id.profileConstraintLayout);
        profileConstraintLayout.setOnClickListener(e -> hideKeyboard(getActivity()));

        edtProfileName = view.findViewById(R.id.edtProfileName);
        if (parseUser.get("profileName") != null) {
            edtProfileName.setText(Objects.requireNonNull(parseUser.get("profileName")).toString());
        }

        edtBio = view.findViewById(R.id.edtBio);
        if (parseUser.get("bio") != null) {
            edtBio.setText(Objects.requireNonNull(parseUser.get("bio")).toString());
        }

        edtProfession = view.findViewById(R.id.edtProfession);
        if (parseUser.get("profession") != null) {
            edtProfession.setText(Objects.requireNonNull(parseUser.get("profession")).toString());
        }

        edtHobbies = view.findViewById(R.id.edtHobbies);
        if (parseUser.get("hobbies") != null) {
            edtHobbies.setText(Objects.requireNonNull(parseUser.get("hobbies")).toString());
        }

        edtSport = view.findViewById(R.id.edtSport);
        if (parseUser.get("sport") != null) {
            edtSport.setText(Objects.requireNonNull(parseUser.get("sport")).toString());
        }

        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(e -> {
            String profileName = edtProfileName.getText().toString().trim();
            try {
                parseUser.put("profileName", profileName);
                parseUser.put("bio", edtBio.getText().toString().trim());
                parseUser.put("profession", edtProfession.getText().toString().trim());
                parseUser.put("hobbies", edtHobbies.getText().toString().trim());
                parseUser.put("sport", edtSport.getText().toString().trim());

                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Updating profile: " + profileName);
                progressDialog.show();

                parseUser.saveInBackground(ee -> {
                    if (ee == null) {
                        FancyToast.makeText(getContext(), parseUser.get("profileName") + " updated successfully!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(getContext(), ee.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                    progressDialog.dismiss();
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            hideKeyboard(getActivity());
        });
        return view;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}