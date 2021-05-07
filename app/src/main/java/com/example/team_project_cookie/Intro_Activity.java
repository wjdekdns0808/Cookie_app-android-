package com.example.team_project_cookie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Intro_Activity extends AppCompatActivity {


    public static Context context_main;
    ProgressBar pb;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                new Get_Sever_Data();
                pb.setVisibility(View.VISIBLE);

            }else if (msg.what ==1){
                new Get_Recipe_List();

            }
            else if(msg.what ==2){
                Intent intent = new Intent(Intro_Activity.this, Login_Activity.class);
                pb.setVisibility(View.VISIBLE);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_);
        context_main  = this;
        pb = (ProgressBar)findViewById(R.id.progressBar);

        Log.i("kihoon","2인트로오나요?");
        OnProgram();
    }

    public void OnProgram(){

        handler.sendEmptyMessage(0);
    }



}