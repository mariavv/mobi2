package com.exsample.maria.mobi2.manager.db;

import android.widget.ImageView;

import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.mvp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by maria on 14.03.2018
 */

public class DbManager implements ContentManager.Listener {

    private final Listener listener;

    public interface Listener {
        void onError(int error);
        void onError(String error);

        void onPhotoDownload(byte[] bytes);

        void onDataLoad(User user);
    }

    public DbManager(Listener listener) {
        this.listener = listener;
    }

    public void write(String table, String uId, Object value) {
        FbReference()
                .child(table)
                .child(/*uId*/"2")
                .setValue(value);
    }

    private static DatabaseReference FbReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public void uploadPhoto(ImageView photo) {
        ContentManager cm = new ContentManager(this);
        cm.uploadPhoto(photo);
    }

    public void read(String table, String uId, final Class clazz) {
        FbReference()
                .child(table)
                .child(/*uId*/"1")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(/*Object.class*/User.class);
                        //listener.onError("on data change");
                        if (user != null) {
                            listener.onDataLoad(user);
                        } else {
                            listener.onError(R.string.user_null);
                        }

                        //listener.onDataChange(dataSnapshot.getValue(/*Object.class*/clazz));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onError(databaseError.getMessage());
                    }
                });

        (new ContentManager(this)).downloadPhoto();
    }

    @Override
    public void onPhotoDownload(byte[] bytes) {
        listener.onPhotoDownload(bytes);
    }
}
