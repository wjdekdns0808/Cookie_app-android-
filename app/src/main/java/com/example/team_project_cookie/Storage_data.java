package com.example.team_project_cookie;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

class Login_user{
    public static String login_user;
    public void setlogin_user(String user){
        this.login_user = user;
    }
}

class Get_User_Info extends AppCompatActivity {
    DatabaseReference mDatabase;
    public static Context context_main;

    String user_code = Login_user.login_user;
    public String user_name;
    public String user_email;
    public HashMap<String, HashMap<String,String>> my_material_map;
    public HashMap<String, String> my_material_all_map;
    public HashMap<String, String> material_map;
    public  HashMap review_map_key;

    public HashMap favorite_map;
    public int review_count;
    public float review_star_age;

    Get_User_Info() {
        read_material();
        read_user_info();
        read_favorite();
        context_main = this;
    }

    void read_material() {
        my_material_map = new HashMap<String, HashMap<String,String>>();
        my_material_all_map = new HashMap<String, String>();

        mDatabase = FirebaseDatabase.getInstance().getReference("User/" + user_code + "/" + "재료");
        ValueEventListener namelist_lis = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot material_class : snapshot.getChildren())
                {
                    material_map = new HashMap<String,String>();
                    for (DataSnapshot material : snapshot.child(material_class.getKey()).getChildren())
                    {
                        material_map.put(material.getKey(),material.getValue().toString());
                        my_material_all_map.put(material.getKey(),material.getValue().toString());
                    }
                    my_material_map.put(material_class.getKey(),material_map);
                }
                ((Login_Activity)Login_Activity.context_main).handler.sendEmptyMessage(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        mDatabase.addListenerForSingleValueEvent(namelist_lis);
    }

    HashMap<String, HashMap<String,String>> get_material() {
        return my_material_map;
    }
    void read_user_info(){
        mDatabase = FirebaseDatabase.getInstance().getReference("User/"+user_code);
        ValueEventListener namelist_lis = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    String value = dataSnapshot.getValue().toString();
                    if (key.equals("name"))
                    {
                        user_name = value;
                    }else if(key.equals("email"))
                    {
                        user_email = value;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.addListenerForSingleValueEvent(namelist_lis);
    }

    void read_favorite(){
        favorite_map =new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("User/"+user_code+"/즐겨찾기");
        ValueEventListener favorite_lis = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    favorite_map.put(dataSnapshot.getKey(),dataSnapshot.getValue());

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.addValueEventListener(favorite_lis);
    }
}


class Recipe_Info extends AppCompatActivity {
    String recipe_name;
    public String recipe_make;

    public HashMap<String, String> recipe_material;
    DatabaseReference mDatabase;
    DatabaseReference mDatabase_recipe;
    public static Context context_main;

    public  HashMap review_map_key;
    public HashMap review_map_value;

    public int review_count;
    public float review_star_age;

    Recipe_Info(String food_class, String recipe_name){

        recipe_get_info(food_class,recipe_name);//레시피정보가져오기
        read_review_info(food_class,recipe_name);//레시피리뷰정보가져오기

        context_main = this;
        this.recipe_name = recipe_name;
    }
    void recipe_get_info(String food_class, String recipe_name){
        recipe_material = new HashMap<String, String>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Recipe/"+food_class+"/" + recipe_name+"/"+"재료");
        ValueEventListener recipe_info_lis = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    String value = dataSnapshot.getValue().toString();
                    recipe_material.put(key, value);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mDatabase_recipe = FirebaseDatabase.getInstance().getReference("Recipe/"+food_class+"/" + recipe_name);
        ValueEventListener recipe_info_name_lis = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    String value = dataSnapshot.getValue().toString();
                    if (key.equals("레시피"))
                    {
                    recipe_make = value;}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mDatabase.addListenerForSingleValueEvent(recipe_info_lis);
        mDatabase_recipe.addListenerForSingleValueEvent(recipe_info_name_lis);
    }

    void read_review_info(String food_class, String recipe_name){
        mDatabase = FirebaseDatabase.getInstance().getReference("Recipe/"+food_class+"/" + recipe_name+"/"+"리뷰");
        ValueEventListener review_lis = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float star_count=0;
                review_map_key = new HashMap();
                for(DataSnapshot dataSnapshot_key : snapshot.getChildren()) {

                    review_map_value = new HashMap();
                    for (DataSnapshot dataSnapshot : snapshot.child(dataSnapshot_key.getKey()).getChildren()) {

                        review_map_value.put(dataSnapshot.getKey(),dataSnapshot.getValue());
                        if(dataSnapshot.getKey().equals("star")) {
                            star_count += Float.parseFloat(dataSnapshot.getValue().toString());

                        }

                    }
                    review_map_key.put(dataSnapshot_key.getKey(),review_map_value);
                }
                review_count = review_map_key.size();

                review_star_age =   star_count/review_count;
                review_star_age = ((float)Math.round(review_star_age*10)/10);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(review_lis);
    }

}

class Recipe_Input extends AppCompatActivity{

}
class Review_data{
    String name;
    float star ;
    String review_text;
    String review_date;
    String uid;
    String food_name;
    String food_class;
    String push_uid;

    public void setdata(String name,String review_text,float star, String review_date,String uid,String food_name,String food_class){
        this.name = name;
        this.review_text = review_text;
        this.star =star;
        this.review_date = review_date;
        this.uid = uid;
        this.food_name = food_name;
        this.food_class=food_class;
    }

    public void setdata(String name,String review_text,float star, String review_date,String uid,String food_name,String food_class,String push_uid){
        this.name = name;
        this.review_text = review_text;
        this.star =star;
        this.review_date = review_date;
        this.uid = uid;
        this.food_name = food_name;
        this.food_class = food_class;
        this.push_uid = push_uid;
    }
    public void setdata(String name,String review_text,float star, String review_date){
        this.name = name;
        this.review_text = review_text;
        this.star =star;
        this.review_date = review_date;

    }
    public HashMap<String,Object> tomap(){
        HashMap<String,Object> reviewdata_map = new HashMap<>();
        reviewdata_map.put("uid",uid);
        reviewdata_map.put("text",review_text);
        reviewdata_map.put("star",star);
        reviewdata_map.put("name",name);
        reviewdata_map.put("date",review_date);
        reviewdata_map.put("food_name",food_name);
        reviewdata_map.put("food_class",food_class);
        return reviewdata_map;
    }

}


class Get_Recipe_List{

    DatabaseReference mDatabase;
    public static Get_Recipe_List context_main;

    public HashMap recipe_list_class ;
    public HashMap recipe_list_food;
    public HashMap recipe_simple_info;

    public HashMap reviedata;
    public ArrayList recipe_name_list;

    int count= 0 ;
    Get_Recipe_List(){
        get_list();
        context_main = this;
    }
    void get_list(){
        reviedata = new HashMap();
        mDatabase = FirebaseDatabase.getInstance().getReference("Recipe/");
        recipe_name_list = new ArrayList();
        ValueEventListener Recipe_list_lis = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipe_list_class = new HashMap();

                for(DataSnapshot dataSnapshot_class : snapshot.getChildren()) {
                    recipe_list_food = new HashMap();
                    for (DataSnapshot dataSnapshot_food : snapshot.child(dataSnapshot_class.getKey()).getChildren()) {
                        recipe_simple_info = new HashMap();
                        recipe_name_list.add(dataSnapshot_food.getKey());
                        for (DataSnapshot dataSnapshot_info : snapshot.child(dataSnapshot_class.getKey()).child(dataSnapshot_food.getKey()).getChildren()) {
                            recipe_simple_info.put(dataSnapshot_info.getKey(),dataSnapshot_info.getValue());
                            for (DataSnapshot dataSnapshot_review : snapshot.child(dataSnapshot_class.getKey()).child(dataSnapshot_food.getKey()).child("리뷰").getChildren()){
                                reviedata.put(dataSnapshot_review.getKey(),dataSnapshot_review.getValue());
                            }
                        }
                        recipe_list_food.put(dataSnapshot_food.getKey(),recipe_simple_info);
                    }
                    recipe_list_class.put(dataSnapshot_class.getKey(),recipe_list_food);
                }
                count++;
                if (count == 1)
                    ((Intro_Activity)Intro_Activity.context_main).handler.sendEmptyMessage(2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(Recipe_list_lis);
    }
}

class Get_Sever_Data extends AppCompatActivity{
    public static Context context_main;
    DatabaseReference mDatabase;
    public HashMap material_category;
    public HashMap material;
    public HashMap material_all;
    int count=0;
    Get_Sever_Data(){
        get_material();
        context_main = this;

    }

    void get_material(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Material/");

        material_category = new HashMap();
        material_all = new HashMap();
        ValueEventListener namelist_lis = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot dataSnapshot_key : snapshot.getChildren()) {
                    material = new HashMap();

                    for (DataSnapshot dataSnapshot : snapshot.child(dataSnapshot_key.getKey()).getChildren()) {

                        material.put(dataSnapshot.getKey(),dataSnapshot.getValue());
                        material_all.put(dataSnapshot.getKey(),dataSnapshot.getValue());

                    }
                    material_category.put(dataSnapshot_key.getKey(),material);

                }count++;

                if (count==1)
                    ((Intro_Activity) Intro_Activity.context_main).handler.sendEmptyMessage(1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.addListenerForSingleValueEvent(namelist_lis);
    }

}