package com.sevrep.myinstagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

public class UsersPostsActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        linearLayout = findViewById(R.id.linearLayout);

        Intent receivedUsernameObject = getIntent();
        String receivedUsername = receivedUsernameObject.getStringExtra("username");

        setTitle(receivedUsername + "'s posts");

        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username", receivedUsername);
        parseQuery.orderByDescending("createdAt");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        parseQuery.findInBackground((posts, e) -> {
            if (e == null && posts.size() > 0) {
                for (ParseObject post : posts) {

                    TextView postDescription = new TextView(UsersPostsActivity.this);
                    postDescription.setText(String.format("%s", post.get("description")));

                    ParseFile postPicture = (ParseFile) post.get("pictures");
                    assert postPicture != null;
                    postPicture.getDataInBackground((data, e1) -> {

                        if (data != null && e1 == null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                            LinearLayout.LayoutParams image_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            image_params.setMargins(0, 0, 0, 8);

                            ImageView postImageView = new ImageView(UsersPostsActivity.this);
                            postImageView.setLayoutParams(image_params);
                            postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            postImageView.setImageBitmap(bitmap);

                            LinearLayout.LayoutParams description_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            description_params.setMargins(0, 0, 0, 16);

                            postDescription.setLayoutParams(description_params);
                            postDescription.setGravity(Gravity.CENTER);
                            postDescription.setBackgroundColor(Color.BLUE);
                            postDescription.setTextColor(Color.WHITE);
                            postDescription.setTextSize(18f);

                            linearLayout.addView(postImageView);
                            linearLayout.addView(postDescription);
                        }
                    });
                }
            } else {
                FancyToast.makeText(this, receivedUsername + " doesn't have any posts.", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                finish();
            }
            progressDialog.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}