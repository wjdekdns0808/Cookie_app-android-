package com.example.team_project_cookie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddproductActivity extends AppCompatActivity {
    ArrayList category_list;
    ArrayList category_fruit = new ArrayList();
    ArrayList category_vegetable = new ArrayList();
    ArrayList category_meat = new ArrayList();
    ArrayList category_fish = new ArrayList();
    ArrayList category_sauce = new ArrayList();
    ArrayList category_etc = new ArrayList();


    ArrayAdapter aa_big ;
    ArrayAdapter aa_small ;

    HashMap category_value;
    Spinner spinner_material_category;
    Spinner spinner_material;
    Intent i ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);


        HashMap category = ((Get_Sever_Data)Get_Sever_Data.context_main).material_category;
        category_list = new ArrayList();

        for(Object key : category.keySet()){
            category_list.add(key.toString());
            category_value = (HashMap) category.get(key);
            if(key.toString().equals("과일")){
                for(Object key_value: category_value.keySet()){
                    category_fruit.add(key_value.toString());
                }
            }
            if(key.toString().equals("채소")){
                for(Object key_value: category_value.keySet()){
                    category_vegetable.add(key_value.toString());
                }

            }
            if(key.toString().equals("육류")){
                for(Object key_value: category_value.keySet()){
                    category_meat.add(key_value.toString());
                }

            }
            if(key.toString().equals("소스")){
                for(Object key_value: category_value.keySet()){
                    category_sauce.add(key_value.toString());
                }

            }
            if(key.toString().equals("수산물")){
                for(Object key_value: category_value.keySet()){
                    category_fish.add(key_value.toString());
                }
            }
            if(key.toString().equals("기타")){
                for(Object key_value: category_value.keySet()){
                    category_etc.add(key_value.toString());
                }
            }
        }



        i = getIntent();
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 1.0); //Display 사이즈의 70%

        int height = (int) (display.getHeight() * 0.5);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;

        getWindow().getAttributes().height = height;


        spinner_material_category = (Spinner)findViewById(R.id.spin1);
        spinner_material = (Spinner)findViewById(R.id.spin2);

        aa_big = new ArrayAdapter(com.example.team_project_cookie.AddproductActivity.this, android.R.layout.simple_list_item_1,category_list);
        spinner_material_category.setAdapter(aa_big);
        spinner_material_category.setOnItemSelectedListener(ls1);



    }
    AdapterView.OnItemSelectedListener ls1 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            EditText et = (EditText)findViewById(R.id.et_ea);

            String category = category_list.get(position).toString();
            if(category.equals("소스")) {
                aa_small = new ArrayAdapter(com.example.team_project_cookie.AddproductActivity.this, android.R.layout.simple_list_item_1, category_sauce);
                spinner_material.setAdapter(aa_small);
                et.setClickable(false);
                et.setFocusable(false);
            }else if(category.equals("채소")){
                aa_small = new ArrayAdapter(com.example.team_project_cookie.AddproductActivity.this, android.R.layout.simple_list_item_1, category_vegetable);
                spinner_material.setAdapter(aa_small);
                et.setFocusableInTouchMode(true);
                et.setFocusable(true);
            }else if (category.equals("수산물")){
                aa_small = new ArrayAdapter(com.example.team_project_cookie.AddproductActivity.this, android.R.layout.simple_list_item_1, category_fish);
                spinner_material.setAdapter(aa_small);
                et.setFocusableInTouchMode(true);
                et.setFocusable(true);
            }else if(category.equals("과일")){
                aa_small = new ArrayAdapter(com.example.team_project_cookie.AddproductActivity.this, android.R.layout.simple_list_item_1, category_fruit);
                spinner_material.setAdapter(aa_small);
                et.setFocusableInTouchMode(true);
                et.setFocusable(true);
            }else if (category.equals("육류")){
                aa_small = new ArrayAdapter(com.example.team_project_cookie.AddproductActivity.this, android.R.layout.simple_list_item_1, category_meat);
                spinner_material.setAdapter(aa_small);
                et.setFocusableInTouchMode(true);
                et.setFocusable(true);
            }
            else {
                aa_small = new ArrayAdapter(com.example.team_project_cookie.AddproductActivity.this, android.R.layout.simple_list_item_1, category_etc);
                spinner_material.setAdapter(aa_small);
                et.setFocusableInTouchMode(true);
                et.setFocusable(true);
            }
            spinner_material.setClickable(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void onClickExit(View view) {
        finish();
    }

    public void onClickAdd(View view) {
        HashMap material_map = new HashMap();

        EditText et = (EditText)findViewById(R.id.et_ea);
        String select_material_category =spinner_material_category.getSelectedItem().toString() ;
        String select_material = spinner_material.getSelectedItem().toString();

        String material_count = et.getText().toString();
        if(et.getText().toString().equals("")&&(!select_material_category.equals("소스"))&&(!select_material_category.equals("기타"))){

            Toast.makeText(this,"수량을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        else if (et.getText().toString().equals(""))
        {
            material_count = "0";
        }
        DatabaseReference user_material_db = FirebaseDatabase.getInstance().getReference("User/"+Login_user.login_user+"/재료/"+select_material_category);

        if(!((Get_User_Info)Get_User_Info.context_main).my_material_map.containsKey(select_material_category)){
            material_map.put(select_material,material_count);
            ((Get_User_Info)Get_User_Info.context_main).my_material_map.put(select_material_category,material_map);
        }

        for(Object key:((Get_User_Info)Get_User_Info.context_main).my_material_map.keySet()){


            if(key.equals(select_material_category)){
                material_map = ((Get_User_Info)Get_User_Info.context_main).my_material_map.get(key);


                if(material_map.containsKey(select_material)){

                    material_map.put(select_material,Integer.parseInt(material_map.get(select_material).toString())+Integer.parseInt(material_count));
                }else{
                    material_map.put(select_material,material_count);
                }

                ((Get_User_Info)Get_User_Info.context_main).my_material_map.put(key.toString(),material_map);
                user_material_db.setValue(material_map);
            }

        }



        i.putExtra("categoryBig",select_material_category);
        i.putExtra("categorySmall",select_material);
        i.putExtra("categoryAmount",material_count);
        ((Get_User_Info) Get_User_Info.context_main).my_material_all_map.put(select_material,material_count);
        Fragment3.additem(select_material,material_count,select_material_category);
        Fragment3.adapter_ref.notifyDataSetChanged();
        finish();
    }
}