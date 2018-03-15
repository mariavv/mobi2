package com.exsample.maria.mobi2.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.mvp.present.ProfilePresenter;
import com.exsample.maria.mobi2.mvp.view.ProfileView;

public class ProfileActivity extends MvpAppCompatActivity implements ProfileView {

    private EditText emailEd;
    private TextView displayNameEd;
    private TextView phoneNumberEd;

    @InjectPresenter
    ProfilePresenter presenter;

    public static Intent start(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        presenter.onActivityCreate(this);
        initViews();
    }

    private void initViews() {
        emailEd = findViewById(R.id.emailEd);
        displayNameEd = findViewById(R.id.displayNameEd);
        phoneNumberEd = findViewById(R.id.phoneNumberEd);
        Button changePhotoBtn = findViewById(R.id.changePhotoBtn);
        Button saveBtn = findViewById(R.id.saveBtn);

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onChangePhotoBtnPressed(v);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveBtnPressed(ProfileActivity.this, emailEd.getText().toString(),
                        displayNameEd.getText().toString(),
                        phoneNumberEd.getText().toString());
            }
        });
    }

    @Override
    public void fillFields(String email, String displayName, String phoneNumber) {
        emailEd.setText(email);
        displayNameEd.setText(displayName);
        phoneNumberEd.setText(phoneNumber);
    }

    @Override
    public void say(int messageRes) {
        say(getString(messageRes));
    }

    @Override
    public void say(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showPhotoPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.photo_popup_menu);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.photoFromGalary:
                        presenter.onMenuItemPhotoFromGalaryPressed();
                        return true;
                    case R.id.photoFromCamera:
                        presenter.onMenuItemPhotoFromCameraPressed();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    @Override
    public void getFromGalary(final int reguestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
        startActivityForResult(intent, reguestCode);
    }

    private ImageView getPhotoView() {
        return findViewById(R.id.photoIv);
    }

    @Override
    public void setImage(Bitmap img_path) {
        ImageView img = getPhotoView();
        img.setImageBitmap(img_path);
    }

    @Override
    public void setImage(Uri uri) {
        ImageView img = getPhotoView();
        img.setImageURI(uri);
    }

    @Override
    public void getFromCamera(int photoCameraRequest) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        presenter.onIntentGetFromCameraCreated(intent.resolveActivity(getPackageManager()), intent);
    }

    @Override
    public void startCameraActivity(Intent intent, int photoCameraRequest) {
        startActivityForResult(intent, photoCameraRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.activityResult(this, requestCode, resultCode, data);
    }
}
