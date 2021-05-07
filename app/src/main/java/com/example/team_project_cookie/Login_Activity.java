package com.example.team_project_cookie;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText login_id_et;
    EditText login_password_et;
    public static Context context_main;
    Handler handler;
    ProgressDialog pd;
    String id ="";
    String password="";
    CheckBox ch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        context_main = this;


        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what==0){

                    pd =new ProgressDialog(context_main, android.R.style.Theme_Material_Dialog_Alert);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    new Get_User_Info();
                }else if(msg.what == 1){
                    Intent i = new Intent(Login_Activity.this, HomeMenu.class);
                    pd.dismiss();
                    startActivity(i);
                    finish();
                }
            }
        };



        ch = (CheckBox)findViewById(R.id.user_save_cb);
        ch.setOnCheckedChangeListener(check_lis);

        SharedPreferences pref = getSharedPreferences("check",MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        if (pref.getBoolean("check",false)) {
            auto_login();
        }

        login_id_et = (EditText)findViewById(R.id.id_et);
        login_password_et=(EditText)findViewById(R.id.password_et);
        ((Button)findViewById(R.id.login_btn)).setOnClickListener(login_btn);
        ((Button)findViewById(R.id.join_btn)).setOnClickListener(join_btn);
    }

    CompoundButton.OnCheckedChangeListener check_lis = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences pref = getSharedPreferences("check",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("check",isChecked);
            editor.commit();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(
                "check",MODE_PRIVATE);
        boolean check = pref.getBoolean("check",false);

        CheckBox cb = (CheckBox)findViewById(R.id.user_save_cb);
        cb.setChecked(check);
        pref.registerOnSharedPreferenceChangeListener(myListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pref = getSharedPreferences(
                "check",MODE_PRIVATE);
        pref.unregisterOnSharedPreferenceChangeListener(myListener);
    }
    SharedPreferences.OnSharedPreferenceChangeListener myListener =new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            boolean check = sharedPreferences.getBoolean(key, false);
            String str = (check) ? "설정":"해체";
            Toast.makeText(Login_Activity.this,"자동로그인" + str + "되었습니다.",Toast.LENGTH_SHORT).show();
        }
    };


    View.OnClickListener login_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setEnabled(false);
            id = login_id_et.getText().toString();
            password = login_password_et.getText().toString();
            login(id,password);
        }
    };


    View.OnClickListener join_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i =new Intent(Login_Activity.this,Join_Activity.class);
            startActivity(i);
        }
    };

    void auto_login(){
                SharedPreferences pref_user = getPreferences(MODE_PRIVATE);
                id = pref_user.getString("user_id", "");
                password = pref_user.getString("user_password", "");
                login(id, password);
                Log.i("kihoon", id + "비밀번호" +password + "아이디랑 패스워드");
    }

    void login(String id, String password){
       this.id = id;
       this.password = password;


        if(id.isEmpty()){// 아이디가 공백일시
            Toast.makeText(this,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
            ((Button)findViewById(R.id.login_btn)).setEnabled(true);
            return;
        }
        else if(password.isEmpty()){ // 비밀번호가 공복일시
            Toast.makeText(this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
            ((Button)findViewById(R.id.login_btn)).setEnabled(true);
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(id,password).addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showProgressDialog();

                if(task.isSuccessful()){

                    FirebaseUser userid = firebaseAuth.getCurrentUser();
                    String userUid = userid.getUid();
                    Login_user user = new Login_user();
                    if (ch.isChecked()){
                        write_user_info(id,password);

                    }

                    user.setlogin_user(userUid);
                    handler.sendEmptyMessage(0);
                    Toast.makeText(Login_Activity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                }else{
                    ((Button)findViewById(R.id.login_btn)).setEnabled(true);
                    Toast.makeText(Login_Activity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void write_user_info(String id,String password)
    {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_id",id);
        editor.putString("user_password",password);
        editor.commit();
    }
    private void showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로그인중입니다.");
        dialog.show();
    }
}