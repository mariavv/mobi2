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
        //AuthManager manager = new AuthManager(this);
        //getViewState().fillFields(manager.getEmail(), manager.getDisplayName(), manager.getPhoneNumber());

        (new DbManager(this))
                .read(context.getString(R.string.db_users_table), AuthManager.getUserId(), User.class);
    }

    public void onSaveBtnPressed(Context context, String email, String displayName, String phoneNumber) {
        if ((Patterns.EMAIL_ADDRESS.matcher(email).matches()) && (displayName.length() > 0)) {
            (new DbManager(this))
                    .write(context.getString(R.string.db_users_table), AuthManager.getUserId(), (new User(email, displayName, phoneNumber)));

            //TODO
            //AuthManager manager = new AuthManager(this);
            //manager.updateEmail(email);
            getViewState().say(R.string.profile_saved);
        } else {
            getViewState().say(R.string.profile_not_valid);
        }
    }

    @Override
    public void onError(int error) {
        getViewState().say(error);
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
    public void onDataLoad(User user) {
        getViewState().fillFields(user.getEmail(), user.getName(), user.getPhone());
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
            (new ImageProvider(this)).getBitmap(context, data);
            //getViewState().setImage(data.getData());
            getViewState().onPhotoChanged();
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
