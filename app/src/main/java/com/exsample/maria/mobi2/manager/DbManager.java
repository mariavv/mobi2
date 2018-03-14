package com.exsample.maria.mobi2.manager;

import com.exsample.maria.mobi2.mvp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by maria on 14.03.2018
 */

public class DbManager {

    private final Listener listener;

    public interface Listener {
        void onDataChange(Object value);

        void onError(String error);
    }

    public DbManager(Listener listener) {
        this.listener = listener;
    }

    public void write(String table, String uId, Object value) {
        (FirebaseDatabase.getInstance().getReference())
                .child(table)
                .child(/*uId*/"2")
                .setValue(value);
    }

    public void read(String table, String uId, final Class clazz) {
        (FirebaseDatabase.getInstance().getReference())
                .child(table)
                .child(/*uId*/"1")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(/*Object.class*/User.class);
                        //listener.onError("on data change");
                        if (user != null) {
                            listener.onError(user.getEmail());
                        } else {
                            listener.onError("user null");
                        }

                        //listener.onDataChange(dataSnapshot.getValue(/*Object.class*/clazz));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onError(databaseError.getMessage());
                    }
                });
    }
}
