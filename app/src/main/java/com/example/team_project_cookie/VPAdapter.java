package com.example.team_project_cookie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class VPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<String>();

    public VPAdapter(FragmentManager fm) {
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new Fragment1());
        items.add(new Fragment2());
        items.add(new Fragment3());
        items.add(new Fragment4());
        items.add(new Fragment5());

        itext.add("홈");
        itext.add("검색");
        itext.add("냉장고");
        itext.add("즐겨찾기");
        itext.add("내정보");
    }

    public VPAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new RecipeFragment1());
        items.add(new RecipeFragment2());
        items.add(new RecipeFragment3());

        itext.add("레시피");
        itext.add("재료");
        itext.add("리뷰");
    }

    public VPAdapter(FragmentManager fm, int key) {
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new RecipelistFrag1());
        items.add(new RecipelistFrag2());
        items.add(new RecipelistFrag3());
        items.add(new RecipelistFrag4());
        items.add(new RecipelistFrag5());
        items.add(new RecipelistFrag6());
        items.add(new RecipelistFrag7());
        items.add(new RecipelistFrag8());

        itext.add("전체보기");
        itext.add("한식");
        itext.add("중식");
        itext.add("일식");
        itext.add("분식");
        itext.add("아시안,양식");
        itext.add("패스트푸드");
        itext.add("디저트");
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return itext.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}

class RecipelistAdapter extends BaseAdapter {

    Context context;
    ArrayList<Recipe_list_Info> foods;
    @Override
    public int getCount() {
        return foods.size();
    }

    public void addItem(Recipe_list_Info food){
        foods.add(food);
    }

    @Override
    public Recipe_list_Info getItem(int i) {
        return foods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListViewer listViewer = new ListViewer(context);
        listViewer.setItem(foods.get(i));
        return listViewer;
    }

    public static Comparator<Recipe_list_Info> cmpName = new Comparator<Recipe_list_Info>() {
        @Override
        public int compare(Recipe_list_Info o1, Recipe_list_Info o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static Comparator<Recipe_list_Info> cmpRating = new Comparator<Recipe_list_Info>() {
        @Override
        public int compare(Recipe_list_Info o1, Recipe_list_Info o2) {
            return o1.getRating().compareTo(o2.getRating());
        }
    };

    public static Comparator<Recipe_list_Info> cmpTime = new Comparator<Recipe_list_Info>() {
        @Override
        public int compare(Recipe_list_Info o1, Recipe_list_Info o2) {
            return o1.getTime().compareTo(o2.getTime());
        }
    };
}

class BannerAdapter extends PagerAdapter {

    private Context context = null;

    HashMap recipe_list_class;
    HashMap recipe_list_food;
    HashMap recipe_simple_info;

    String[] arrRecipe = {"김치찌개", "갈비찜", "라자냐", "마카롱", "탕수육"};

    public void banner_recipe_list(String recipe){
        recipe_list_class = new HashMap();
        recipe_list_food = new HashMap();
        recipe_list_class = ((Get_Recipe_List)Get_Recipe_List.context_main).recipe_list_class;
        recipe_simple_info = new HashMap();

        for (Object category : recipe_list_class.keySet()) {
            recipe_list_food = (HashMap) recipe_list_class.get(category);
            for (Object food_name : recipe_list_food.keySet()) {

                if (recipe_list_food.containsKey(recipe)) {

                    recipe_simple_info = (HashMap) recipe_list_food.get(recipe);
                }
            }
        }
    }

    // Context 를 전달받아 context 에 저장하는 생성자 추가.
    public BannerAdapter(Context context) {
        this.context = context;
    }

    // public String getFoodname() {return recipe_simple_info.toString();}
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // position 값을 받아 주어진 위치에 페이지를 생성한다

        View view = null;

        if(context != null) {
            // LayoutInflater 를 통해 "/res/layout/page.xml" 을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.banner, container, false);

            Get_Storage_Image get_img = new Get_Storage_Image();

            ImageView imgBanner = view.findViewById(R.id.img_banner);
            TextView nameBanner = view.findViewById(R.id.foodname_banner);
            TextView timeBanner = view.findViewById(R.id.time_banner);
            TextView ratingBanner = view.findViewById(R.id.rating_banner);

            if(position == 0) {
                banner_recipe_list(arrRecipe[position]);
                get_img.read_image(context, recipe_simple_info.get("이미지").toString(), imgBanner);
                nameBanner.setText(arrRecipe[position]);
                timeBanner.setText("평균 조리시간\n"+recipe_simple_info.get("조리시간"));
                ratingBanner.setText(recipe_simple_info.get("평균별점")+"점");
            } else if(position == 1) {
                banner_recipe_list(arrRecipe[position]);
                get_img.read_image(context, recipe_simple_info.get("이미지").toString(), imgBanner);
                nameBanner.setText(arrRecipe[position]);
                timeBanner.setText("평균 조리시간\n"+recipe_simple_info.get("조리시간"));
                ratingBanner.setText(recipe_simple_info.get("평균별점")+"점");
            } else if(position == 2) {
                banner_recipe_list(arrRecipe[position]);
                get_img.read_image(context, recipe_simple_info.get("이미지").toString(), imgBanner);
                Log.i("kihoon", arrRecipe[position]);
                Log.i("kihoon", recipe_simple_info.get("이미지").toString());
                nameBanner.setText(arrRecipe[position]);
                timeBanner.setText("평균 조리시간\n"+recipe_simple_info.get("조리시간"));
                ratingBanner.setText(recipe_simple_info.get("평균별점")+"점");
            } else if(position == 3) {
                banner_recipe_list(arrRecipe[position]);
                get_img.read_image(context, recipe_simple_info.get("이미지").toString(), imgBanner);
                nameBanner.setText(arrRecipe[position]);
                timeBanner.setText("평균 조리시간\n"+recipe_simple_info.get("조리시간"));
                ratingBanner.setText(recipe_simple_info.get("평균별점")+"점");
            } else if(position == 4) {
                banner_recipe_list(arrRecipe[position]);
                get_img.read_image(context, recipe_simple_info.get("이미지").toString(), imgBanner);
                nameBanner.setText(arrRecipe[position]);
                timeBanner.setText("평균 조리시간\n"+recipe_simple_info.get("조리시간"));
                ratingBanner.setText(recipe_simple_info.get("평균별점")+"점");
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_banner = arrRecipe[position];
                Intent intent = new Intent(context, RecipeActivity.class);

                if(position == 0 || position == 1) {
                    intent.putExtra("recipe_class","한식");
                    intent.putExtra("recipe_name",name_banner);
                    new Recipe_Info("한식",name_banner);
                } else if(position == 2) {
                    intent.putExtra("recipe_class","아시안,양식");
                    intent.putExtra("recipe_name",name_banner);
                    new Recipe_Info("아시안,양식",name_banner);
                } else if(position == 3) {
                    intent.putExtra("recipe_class","디저트");
                    intent.putExtra("recipe_name",name_banner);
                    new Recipe_Info("디저트",name_banner);
                } else if(position == 4) {
                    intent.putExtra("recipe_class","중식");
                    intent.putExtra("recipe_name",name_banner);
                    new Recipe_Info("중식",name_banner);
                }
                context.startActivity(intent);
            }
        });

        // 뷰페이저에 추가
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // position 값을 받아 주어진 위치의 페이지를 삭제한다
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // 사용 가능한 뷰의 개수를 return 한다
        // 전체 페이지 수는 5개로 고정한다
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // 페이지 뷰가 생성된 페이지의 object key 와 같은지 확인한다
        // 해당 object key 는 instantiateItem 메소드에서 리턴시킨 오브젝트이다
        // 즉, 페이지의 뷰가 생성된 뷰인지 아닌지를 확인하는 것
        return view == object;
    }
}