package com.example.team_project_cookie;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class RecipeActivity extends AppCompatActivity { //레시피 화면
    TextView tv_food_name;
    Dialog dialog_review;
    Intent intent;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intent = getIntent();

        ViewPager vp_recipe = (ViewPager) findViewById(R.id.recipe_viewpager);
        VPAdapter adt_recipe = new VPAdapter(getSupportFragmentManager(), RecipeActivity.this);
        vp_recipe.setAdapter(adt_recipe);

        tv_food_name = (TextView) findViewById(R.id.tv_foodname);
        tv_food_name.setText(intent.getStringExtra("recipe_name"));

        // 레시피 화면 레시피, 재료, 리뷰 탭화면 연동
        TabLayout tab_recipe = findViewById(R.id.recipeTab);
        tab_recipe.setupWithViewPager(vp_recipe);

        favoriteSet();

        //리뷰 작성 다이얼로그 생성
        dialog_review = new Dialog(RecipeActivity.this);
        dialog_review.setContentView(R.layout.layout_dialog);

        ((RatingBar) findViewById(R.id.ratingBar)).setRating(((Recipe_Info) Recipe_Info.context_main).review_star_age);
        ((TextView) findViewById(R.id.tv_reviewcount)).setText("리뷰갯수 : " + ((Recipe_Info) Recipe_Info.context_main).review_count + "개");

        HashMap recipe_list_class = new HashMap();
        HashMap recipe_list_food = new HashMap();
        recipe_list_class = ((Get_Recipe_List)Get_Recipe_List.context_main).recipe_list_class;
        recipe_list_food = (HashMap)recipe_list_class.get(intent.getStringExtra("recipe_class"));
        ImageView iv = ((ImageView)findViewById(R.id.recipe_image_view));
        HashMap recipe_simple_info = new HashMap();
        recipe_simple_info = (HashMap)recipe_list_food.get(intent.getStringExtra("recipe_name"));
        String img_path = recipe_simple_info.get("이미지").toString();

        Get_Storage_Image get_img = new Get_Storage_Image();
        get_img.read_image(RecipeActivity.this,img_path,iv);

        Button btn_review = (Button) findViewById(R.id.btn_review);
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }


    public void showDialog() { //다이얼로그 생성 메서드
        dialog_review.show();

        RatingBar ratingBar = dialog_review.findViewById(R.id.ratingReview);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            }
        });

        Button btn_write = dialog_review.findViewById(R.id.reviewWrite);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //리뷰다이얼로그_작성버튼 온클릭
                HashMap<String, Object> review_dataMap = new HashMap<>();

                String user_name = ((Get_User_Info) Get_User_Info.context_main).user_name;
                String review_text = "";
                float star = ratingBar.getRating();
                review_text = ((EditText) ((Dialog) dialog_review).findViewById(R.id.et_review)).getText().toString();

                Review_data r_data = new Review_data();

                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.KOREA);
                String date = sdf.format(new Timestamp(System.currentTimeMillis()));
                String uid = Login_user.login_user;
                r_data.setdata(user_name, review_text, star, date,uid,intent.getStringExtra("recipe_name"),intent.getStringExtra("recipe_class"));
                review_dataMap = r_data.tomap();

                DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Recipe/"
                        + intent.getStringExtra("recipe_class") + "/" + intent.getStringExtra("recipe_name") + "/리뷰/");

                reviewRef.push().setValue(review_dataMap);

                String str_star_avg = (((Recipe_Info) Recipe_Info.context_main).review_star_age) + "";//별점 평균 문자열 변환
                DatabaseReference star_avg = FirebaseDatabase.getInstance().getReference("Recipe/"
                        + intent.getStringExtra("recipe_class") + "/" + intent.getStringExtra("recipe_name") + "/평균별점/");
                //database 경로지정
                star_avg.setValue(str_star_avg);//database 지정경로에 데이터삽임

                ((RecipeActivity) RecipeActivity.context).onResume();
                dialog_review.dismiss();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        Button btn_cancel = dialog_review.findViewById(R.id.reviewCancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //취소버튼 온클릭
                dialog_review.dismiss();
            }
        });
    }

    public void favoriteSet() {
        HashMap favorite_data = ((Get_User_Info) Get_User_Info.context_main).favorite_map;

        for (Object key : favorite_data.keySet()) {

            if (key.toString().equals(intent.getStringExtra("recipe_name"))) {
                ((CheckBox) findViewById(R.id.checkbox_recipe_favorite)).setChecked(true);
                break;
            } else {
                ((CheckBox) findViewById(R.id.checkbox_recipe_favorite)).setChecked(false);
            }
        }

        ((CheckBox) findViewById(R.id.checkbox_recipe_favorite)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference Db_ref;

                if (isChecked) {
                    Db_ref = FirebaseDatabase.getInstance().getReference("User/" + Login_user.login_user + "/즐겨찾기/" +
                            intent.getStringExtra("recipe_name"));
                    ((Get_User_Info) Get_User_Info.context_main).favorite_map.put(intent.getStringExtra("recipe_name")
                            , intent.getStringExtra("recipe_name"));
                    Db_ref.setValue("1");
                } else if (!isChecked) {
                    Db_ref = FirebaseDatabase.getInstance().getReference("User/" + Login_user.login_user + "/즐겨찾기/");
                    ((Get_User_Info) Get_User_Info.context_main).favorite_map.remove(intent.getStringExtra("recipe_name"));
                    Db_ref.child(intent.getStringExtra("recipe_name")).removeValue();
                }
            }
        });
    }
}
