package com.exsample.maria.mobi2.manager.db;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by maria on 19.03.2018
 */

class ContentManager {

    private final Listener listener;

    public interface Listener {
        void onPhotoDownload(byte[] bytes);
    }

    ContentManager(Listener listener) {
        this.listener = listener;
    }

    void uploadPhoto(ImageView photo) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("house2.jpg");
        //StorageReference spaceRef = storageRef.child("images/photo.jpg");

        photo.setDrawingCacheEnabled(true);
        photo.buildDrawingCache();
        Bitmap bitmap = photo.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    void downloadPhoto() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("house2.jpg");
        //StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/photo.jpg");
        //StorageReference httpsReference =
        //        storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20photo.jpg");

        storageRef
                .child("house.jpg")
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                listener.onPhotoDownload(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }
}
