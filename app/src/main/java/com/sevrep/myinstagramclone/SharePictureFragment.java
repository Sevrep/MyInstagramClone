package com.sevrep.myinstagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SharePictureFragment extends Fragment implements View.OnClickListener {

    private ImageView imgPlace;
    private EditText edtDescription;

    private Bitmap receivedImage;

    public SharePictureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_share_picture, container, false);

        imgPlace = view.findViewById(R.id.imgPlace);
        imgPlace.setOnClickListener(SharePictureFragment.this);

        edtDescription = view.findViewById(R.id.edtDescription);

        Button btnShareImage = view.findViewById(R.id.btnShareImage);
        btnShareImage.setOnClickListener(SharePictureFragment.this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.imgPlace) {
            if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
            } else {
                getChosenImage();
            }
        } else if (viewId == R.id.btnShareImage) {
            if (receivedImage != null) {

                String description = edtDescription.getText().toString().trim();

                if (TextUtils.isEmpty(description)) {
                    edtDescription.setError("Description is required...");
                } else {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    receivedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();

                    ParseFile parseFile = new ParseFile("pictagram.png", bytes);
                    ParseObject parseObject = new ParseObject("Photo");
                    parseObject.put("pictures", parseFile);
                    parseObject.put("description", description);
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    parseObject.saveInBackground(e -> {
                        if (e == null) {
                            FancyToast.makeText(getContext(), "Image uploaded!!!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        } else {
                            FancyToast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }
                        progressDialog.dismiss();
                    });
                    hideKeyboard(getActivity());
                }

            } else {
                FancyToast.makeText(getContext(), "You must select an image to upload...", Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    receivedImage = BitmapFactory.decodeFile(picturePath);
                    imgPlace.setImageBitmap(receivedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getChosenImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
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