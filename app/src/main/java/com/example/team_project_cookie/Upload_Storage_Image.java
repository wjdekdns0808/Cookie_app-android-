package com.example.team_project_cookie;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;

public class Upload_Storage_Image {

    Upload_Storage_Image(){

    }
    void input_image(Uri imgUrl,String img_path) {
        if (imgUrl != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

            StorageReference imgRef = firebaseStorage.getReference(img_path);

            UploadTask uploadTask = imgRef.putFile(imgUrl);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("kihoon", "성공");
                }
            });
        }
    }
}

