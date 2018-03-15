package com.exsample.maria.mobi2.mvp.present;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.manager.AuthManager;
import com.exsample.maria.mobi2.manager.DbManager;
import com.exsample.maria.mobi2.mvp.model.User;
import com.exsample.maria.mobi2.mvp.view.ProfileView;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by maria on 01.03.2018
 */

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView>
        implements AuthManager.Listener, DbManager.Listener {

    private static final int PHOTO_GALARY = 1;

    public void onActivityCreate(Context context) {
        if (!(new AuthManager(this).userExists())) {
            getViewState().close();
        }
        //AuthManager manager = new AuthManager(this);
        //getViewState().fillFields(manager.getEmail(), manager.getDisplayName(), manager.getPhoneNumber());


        (new DbManager(this))
               .read(context.getString(R.string.db_users_table), (new AuthManager(this)).getUserId(), User.class);
    }

    public void onSaveBtnPressed(Context context, String email, String displayName, String phoneNumber) {
        if ((Patterns.EMAIL_ADDRESS.matcher(email).matches()) /*&& (displayName.length() > 0)*/) {
            (new DbManager(this))
                    .write(context.getString(R.string.db_users_table),
                            (new AuthManager(this)).getUserId(),
                            (new User(email, displayName, phoneNumber)));

            //AuthManager manager = new AuthManager(this);
            //manager.updateEmail(email);
            getViewState().say(R.string.saved);
        } else {
            getViewState().say(R.string.not_valid);
        }
    }

    @Override
    public void error(String message) {
        getViewState().say(message);
    }

    @Override
    public void onDataChange(Object value) {
        User user = (User) value;
        if (user != null) {
            //getViewState().say(user.getEmail());
        } else {
            //getViewState().say("user null");
        }
    }

    @Override
    public void onError(String error) {
        getViewState().say(error);
    }

    public void onChangePhotoBtnPressed(View v) {
        getViewState().showPhotoPopupMenu(v);
    }

    public void onMenuItemPhotoFromGalaryPressed() {
        getViewState().getFromGalary(PHOTO_GALARY);
    }

    public void activityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_GALARY) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                String img_path = "";
                try {
                    img_path = getFilePath(context, imageUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                File imgFile = new File(img_path);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    getViewState().setImage(myBitmap);
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public /*static*/ String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
