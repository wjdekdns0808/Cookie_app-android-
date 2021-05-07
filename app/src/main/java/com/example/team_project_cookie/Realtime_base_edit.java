package com.example.team_project_cookie;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Realtime_base_edit {
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    Realtime_base_edit(){

    }

    void upload_data(String db_path, String value)
    {
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(db_path);
        databaseReference.setValue(value);
    }
    void delete_data(String db_path,String value){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(db_path);
        databaseReference.child(value).removeValue();


    }
}
