package com.example.team_project_cookie;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class Get_Storage_Image {
    Get_Storage_Image() {

    }
     void read_image( Context context, String img_path, ImageView imageView){


        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        // 위의 저장소를 참조하는 파일명으로 지정
        StorageReference storageReference = firebaseStorage.getReference().child(img_path);//사진경로
        //StorageReference에서 파일 다운로드 URL 가져옴
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Activity activity = (Activity) context;
                    if (activity.isFinishing())
                        return;

                    Glide.with(context)
                            .load(task.getResult())
                            .into(imageView);//삽입할 이미지뷰
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    imageView.setImageResource(R.drawable.logo);
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

