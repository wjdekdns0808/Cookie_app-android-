package com.example.team_project_cookie;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class GridViewer extends LinearLayout {
    TextView textView;
    TextView textView2;
    ImageView imageView;
    public GridViewer(Context context) {
        super(context);

        init(context);
    }

    public GridViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_grid,this,true);

        textView = (TextView)findViewById(R.id.tv_ingredient);
        textView2 = (TextView)findViewById(R.id.tv_count);
        imageView = (ImageView) findViewById(R.id.iv_grid);
    }

    public void setItem(Refrigerator refrigerator){
        textView.setText(refrigerator.getItem());
        textView2.setText(refrigerator.getCount());

        Get_Storage_Image get_img = new Get_Storage_Image();
        get_img.read_image(refrigerator.getContext(),refrigerator.getImg_path(),imageView);

    }

    public void setItem(Fragment1.MainMenu menu){
        textView.setText(menu.getMenu());
        textView2.setText("");
        imageView.setImageResource(menu.getIcon());
    }
}

class ListViewer extends LinearLayout {

    ImageView imageView;
    TextView tv_name;
    TextView tv_intro;
    TextView tv_time;
    TextView tv_rating;

    public ListViewer(Context context) {
        super(context);

        init(context);
    }

    public ListViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.customlist,this,true);

        imageView = (ImageView) findViewById(R.id.food_image);
        tv_name = (TextView)findViewById(R.id.recipeinfo_name);
        tv_intro = (TextView)findViewById(R.id.recipeinfo_intro);
        tv_time = (TextView)findViewById(R.id.recipeinfo_time);
        tv_rating = (TextView)findViewById(R.id.recipeinfo_rating);
    }

    public void setItem(Recipe_list_Info recipeListInfo){

        Get_Storage_Image get_img = new Get_Storage_Image();
        get_img.read_image(recipeListInfo.getContext(),recipeListInfo.getImg_path(),imageView);
        tv_name.setText(recipeListInfo.getName());
        tv_intro.setText(recipeListInfo.getIntro());
        tv_time.setText("평균 조리시간\n"+ recipeListInfo.getTime());
        tv_rating.setText(recipeListInfo.getRating()+"점");
    }
}
