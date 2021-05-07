package com.example.team_project_cookie;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Join_Activity extends AppCompatActivity {
    EditText join_id_et;
    EditText join_name_et;
    EditText join_password_et;
    FirebaseAuth firebaseAuth;
    ArrayList list_name = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_);

        read_data();
        firebaseAuth = FirebaseAuth.getInstance();
        join_name_et = (EditText)findViewById(R.id.join_name_et);
        join_id_et = (EditText)findViewById(R.id.join_id_et);
        join_password_et = (EditText)findViewById(R.id.join_password_et);
        ((Button)findViewById(R.id.join_join_btn)).setOnClickListener(join_lis);

    }

    View.OnClickListener join_lis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerUser();
        }
    };



    // 계정생성 메서드
    void registerUser(){
        String join_id = join_id_et.getText().toString();
        String join_password = join_password_et.getText().toString();
        String join_name = join_name_et.getText().toString();
        if(check_name(join_name)){
            Toast.makeText(this,"중복된아이디입니다",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(join_id.isEmpty()){// 아이디가 공백일시
            Toast.makeText(this,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(join_password.isEmpty()){ // 비밀번호가 공복일시
            Toast.makeText(this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }else if(join_password.length()<6){ // 비밀버노가 6자리가안될때
            Toast.makeText(this,"6자리 이상에 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
            return;

        }

        // 계정생성
        firebaseAuth.createUserWithEmailAndPassword(join_id,join_password).addOnCompleteListener(Join_Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){ //계정생성 성공


                    String name = join_name;
                    String id_code = firebaseAuth.getUid();
                    String email = join_id;
                    UserHashmap user = new UserHashmap(name,email);
                    HashMap user_hash = user.datainput();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference table_user = database.getReference("User");
                    table_user.child(id_code).setValue(user_hash);
                    DatabaseReference table_cookIg = database.getReference("User/"+id_code);


                    Toast.makeText(Join_Activity.this,"가입성공",Toast.LENGTH_SHORT).show();

                    finish();
                }else{ // 계정생성 실패

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(Join_Activity.this,"이메일 형식에 맞지 않습니다.",Toast.LENGTH_SHORT).show();

                    }catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(Join_Activity.this,"이미 등록된 아이디",Toast.LENGTH_SHORT).show();

                    }catch (Exception e) {
                        Toast.makeText(Join_Activity.this,"다시 시도해주세요",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void read_data() {
        boolean result = false;
        DatabaseReference nameDb;
        nameDb = FirebaseDatabase.getInstance().getReference("User");


        ValueEventListener namelist_lis = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_name.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();

                    list_name.add(key);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        nameDb.addListenerForSingleValueEvent(namelist_lis);
    }

    boolean check_name(String name){

        boolean result = list_name.contains(name);
        return result;
    }
}


class UserHashmap{

    String name;
    String email;

    public  UserHashmap(String name,String email){
        this.name = name;
        this.email = email;
    }

    public HashMap datainput(){
        HashMap<String,String> user = new HashMap<>();
        user.put("name",name);
        user.put("email",email);

        return user;
    }
}