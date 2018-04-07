package com.exsample.maria.mobi2.mvp.present;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.manager.AuthManager;
import com.exsample.maria.mobi2.manager.db.DbManager;
import com.exsample.maria.mobi2.manager.ImageProvider;
import com.exsample.maria.mobi2.mvp.model.User;
import com.exsample.maria.mobi2.mvp.view.ProfileView;

import java.util.HashMap;

/**
 * Created by maria on 01.03.2018
 */

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView>
        implements  DbManager.Listener, ImageProvider.Listener {

    private static final int GALARY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    public void onActivityCreate(Context context) {
        if (!AuthManager.userExists()) {
            getViewState().close();
        }

        //getViewState().fillFields("", "", AuthManager.getPhoneNumber());

        // todo надо загрузить из FirebaseAuth
        (new DbManager(this))
                .read(context.getString(R.string.db_users_table), AuthManager.getUserId());
    }

    public void onSaveBtnPressed(Context context, String email, String displayName, String phoneNumber) {
        if ((isEmailValid(email)) && (!isDisplayNameEmpty(displayName))) {
            User user = new User(email, displayName, phoneNumber);

            (new DbManager(this))
                    .write(context.getString(R.string.db_users_table), AuthManager.getUserId(), user);

            AuthManager.updateUser(user.getEmail(), "");
        } else {
            getViewState().say(R.string.profile_not_valid);
        }
    }

    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isDisplayNameEmpty(String displayName) {
        return displayName.isEmpty();
    }

    @Override
    public void onReadNullError() {
        //в бд нет записи о пользователе

        //getViewState().say(R.string.user_null);
    }

    @Override
    public void onError(String error) {
        getViewState().say(error);
    }

    @Override
    public void onPhotoDownload(byte[] bytes) {
        getViewState().setImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    @Override
    public void onDataLoad(HashMap value) {
        getViewState().fillFields(value.get("email").toString(), value.get("name").toString(), value.get("phone").toString());
        getViewState().configEditFields();
    }

    @Override
    public void writeSuccessful() {
        getViewState().say(R.string.profile_saved);
    }

    public void onChangePhotoBtnPressed(View v) {
        getViewState().showPhotoPopupMenu(v);
    }

    public void onMenuItemPhotoFromGalaryPressed() {
        getViewState().getFromGalary(GALARY_REQUEST);
    }

    public void activityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == GALARY_REQUEST) {
            (new ImageProvider(this)).getBitmap(context, data.getData());
            //getViewState().setImage(data.getData());

            //getViewState().onPhotoChanged();
        } else if (requestCode == CAMERA_REQUEST) {
            //TODO
            getViewState().setImage(data.getData());
            getViewState().onPhotoChanged();
        }
    }

    @Override
    public void bitmap(Bitmap bitmap) {
        getViewState().setImage(bitmap);
    }

    public void onMenuItemPhotoFromCameraPressed() {
        getViewState().getFromCamera(CAMERA_REQUEST);
    }

    public void onIntentGetFromCameraCreated(ComponentName resolveActivity, Intent intent) {
        if (resolveActivity != null) {
            getViewState().startCameraActivity(intent, CAMERA_REQUEST);
        }
    }

    public void onPhotoChanged(ImageView photoIv) {
        (new DbManager(this)).uploadPhoto(photoIv);
    }
}
