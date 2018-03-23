package com.exsample.maria.mobi2.mvp.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.arellomobile.mvp.MvpView;

/**
 * Created by maria on 09.03.2018
 */

public interface ProfileView extends MvpView {
    void fillFields(final String email, final String displayName, final String phoneNumber);

    void say(int messageRes);

    void say(String message);

    void close();

    void showPhotoPopupMenu(View v);

    void getFromGalary(final int reguestCode);

    void setImage(Bitmap img_path);

    void setImage(Uri uri);

    void getFromCamera(int photoCameraRequest);

    void startCameraActivity(Intent intent, int photoCameraRequest);

    void onPhotoChanged();

    void configEditFields();
}
