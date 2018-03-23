package com.exsample.maria.mobi2.manager.db;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by maria on 14.03.2018
 */

public class DbManager implements ContentManager.Listener {

    private final Listener listener;

    public interface Listener {
        void onReadNullError();

        void onError(String error);

        void onPhotoDownload(byte[] bytes);

        void onDataLoad(HashMap value);

        void writeSuccessful();
    }

    public DbManager(Listener listener) {
        this.listener = listener;
    }

    public void write(String table, String uId, Object value) {
        FbReference()
                .child(table)
                .child(uId)
                .setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.writeSuccessful();
                } else {
                    showError(task);
                }
            }
        });
    }

    private void showError(Task<Void> task) {
        if (task.getException() != null) {
            listener.onError(task.getException().getMessage());
        }
    }

    private static DatabaseReference FbReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public void uploadPhoto(ImageView photo) {
        ContentManager cm = new ContentManager(this);
        cm.uploadPhoto(photo);
    }

    public void read(String table, String uId) {
        FbReference()
                .child(table)
                .child(uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap map = (HashMap) dataSnapshot.getValue();
                        if (map != null) {
                            listener.onDataLoad(map);
                        } else {
                            listener.onReadNullError();
                        }
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
