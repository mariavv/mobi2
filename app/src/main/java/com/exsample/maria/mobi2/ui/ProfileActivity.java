package com.exsample.maria.mobi2.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.exsample.maria.mobi2.tools.BlurBuilder;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;


public class ProfileActivity extends MvpAppCompatActivity implements ProfileView, View.OnFocusChangeListener {

    private static final int PERMISSIONS_REQUEST_CAMERA = 1;

    private ImageView photoIv;
    private TextInputLayout emailLayout;
    private EditText emailEd;
    private TextInputLayout displayNameLayout;
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

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        //        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        presenter.onActivityCreate(this);

        initViews();
    }

    private void addPhoneEdMask() {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(getString(R.string.profile_phone_mask));
        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher.installOn(phoneNumberEd);
    }

    private void initViews() {
        photoIv = findViewById(R.id.photoIv);

        emailLayout = findViewById(R.id.emailLayout);
        emailEd = findViewById(R.id.emailEd);

        displayNameLayout = findViewById(R.id.displayNameLayout);
        displayNameEd = findViewById(R.id.displayNameEd);

        phoneNumberEd = findViewById(R.id.phoneNumberEd);

        Button changePhotoBtn = findViewById(R.id.changePhotoBtn);
        Button saveBtn = findViewById(R.id.saveBtn);

        setBlurImage();

        addPhoneEdMask();

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onChangePhotoBtnPressed(v);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveBtnPressed(ProfileActivity.this,
                        getString(emailEd), getString(displayNameEd), getString(phoneNumberEd));
            }
        });
    }

    private void setBlurImage() {
        ImageView resultImage = findViewById(R.id.blurPhotoIv);
        Bitmap resultBmp = BlurBuilder.blur(
                this, BitmapFactory.decodeResource(getResources(), R.drawable.img));
        resultImage.setImageBitmap(resultBmp);
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
        /*File imagePath = new File(Environment.getExternalStorageDirectory(), "image");
        File newFile = new File(imagePath, "colorful_houses.jpg");
        Uri uri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", newFile);

        setImage(uri);*/


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
        startActivityForResult(intent, reguestCode);
    }

    public void onPhotoChanged() {
        presenter.onPhotoChanged(photoIv);
    }

    @Override
    public void configEditFields() {
        displayNameEd.setOnFocusChangeListener(this);
        emailEd.setOnFocusChangeListener(this);
    }

    @Override
    public void setImage(Bitmap img_path) {
        photoIv.setImageBitmap(img_path);
    }

    @Override
    public void setImage(Uri uri) {
        photoIv.setImageURI(uri);
    }

    @Override
    public void getFromCamera(int photoCameraRequest) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                say(R.string.profile_camera_pernission_needs);
            } else {
                requestPermission();
            }
        } else {
            requestPermission();
        }

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //presenter.onIntentGetFromCameraCreated(intent.resolveActivity(getPackageManager()), intent);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSIONS_REQUEST_CAMERA);
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == displayNameEd && hasFocus) Toast.makeText(this, "name has", Toast.LENGTH_SHORT).show();
        if (v == displayNameEd && presenter.isDisplayNameEmpty(getString(displayNameEd))) {
            displayNameLayout.setErrorEnabled(true);
            displayNameLayout.setError(getResources().getString(R.string.profile_user_name_error));
            return;
        } else {
            displayNameLayout.setErrorEnabled(false);
        }
        if (v == emailEd && presenter.isEmailValid(emailEd.getText().toString())) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError(getResources().getString(R.string.profile_email_error));
        } else {
            emailLayout.setErrorEnabled(false);
        }
    }

    private String getString(TextView textView) {
        return textView.getText().toString();
    }
}
