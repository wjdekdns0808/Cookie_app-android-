package com.example.team_project_cookie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class User_Profile_Edit extends AppCompatActivity {

    EditText et_user_name;
    Intent intent;
    ImageView iv_user_picture;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        intent = getIntent();

        et_user_name = (EditText)findViewById(R.id.profile_edit_user_name);
        et_user_name.setText(intent.getStringExtra("user_name"));

        iv_user_picture = (ImageView)findViewById(R.id.profile_edit_picture);
        iv_user_picture.setOnClickListener(iv_lis);

        Get_Storage_Image get_img = new Get_Storage_Image();

       String user_code = intent.getStringExtra("user_code");
       String img_path = "profile/"+user_code;
        get_img.read_image(this,img_path,iv_user_picture);

        ((Button)findViewById(R.id.profile_edit_ok)).setOnClickListener(ok_lis);
        ((Button)findViewById(R.id.profile_edit_cancel)).setOnClickListener(cancel_lis);

    }


    View.OnClickListener iv_lis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(i, 10);
        }

    };
    View.OnClickListener ok_lis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("user_name",et_user_name.getText().toString());
            intent.setData(selectedImageUri);

            setResult(RESULT_OK,intent);
            finish();
        }
    };
    View.OnClickListener cancel_lis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED,intent);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== 10 && data!=null) {
            selectedImageUri = data.getData();
            iv_user_picture.setImageURI(selectedImageUri);

        }else{
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_LONG).show();
        }
    }
}

