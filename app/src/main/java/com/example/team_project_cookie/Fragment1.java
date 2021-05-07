package com.example.team_project_cookie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment1 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor
    }

    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment1 newInstance() {
        return new Fragment1();
    }

    Context mContext;
    GridView gridView;
    MainMenuAdapter mainMenuAdapter;
    HomeMenu activity;

    ViewPager vp_banner;
    BannerAdapter bannerAdapter;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        activity = (HomeMenu)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_fragment1, container, false);
        gridView = (GridView)rootView.findViewById(R.id.main_grid);
        mainMenuAdapter = new MainMenuAdapter();
        mainMenuAdapter.addItem(new MainMenu("전체보기", R.drawable.main_recipebook));
        mainMenuAdapter.addItem(new MainMenu("한식", R.drawable.main_rice));
        mainMenuAdapter.addItem(new MainMenu("중식", R.drawable.main_jajang));
        mainMenuAdapter.addItem(new MainMenu("일식", R.drawable.main_sushi));
        mainMenuAdapter.addItem(new MainMenu("분식", R.drawable.main_tteokbokki));
        mainMenuAdapter.addItem(new MainMenu("양식", R.drawable.main_pasta));
        mainMenuAdapter.addItem(new MainMenu("패스트푸드", R.drawable.main_hamburger));
        mainMenuAdapter.addItem(new MainMenu("디저트", R.drawable.main_dessert));

        gridView.setAdapter(mainMenuAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecipelistActivity.class);
                intent.putExtra("clickMenu", position);
                startActivity(intent);
            }
        });

        vp_banner = (ViewPager)rootView.findViewById(R.id.vp_banner);
        bannerAdapter = new BannerAdapter(mContext);
        vp_banner.setAdapter(bannerAdapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == 4) {
                    currentPage = 0;
                }
                vp_banner.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

//        ArrayList<Recipe_list_Info> arrRecipe = new ArrayList<>();
//        String food_name = "김치찌개";
//
//        HashMap recipe_list_class = new HashMap();
//        HashMap recipe_list_food = new HashMap();
//        recipe_list_class = ((Get_Recipe_List)Get_Recipe_List.context_main).recipe_list_class;
//        recipe_list_food = (HashMap)recipe_list_class.get("한식");
//        HashMap recipe_simple_info = new HashMap();
//        recipe_simple_info = (HashMap)recipe_list_food.get(food_name);
//        String img_path = recipe_simple_info.get("이미지").toString();
//        recipelistAdapter.addItem(new Recipe_list_Info(mContext,img_path, food_name.toString(),
//                recipe_simple_info.get("음식소개").toString(),
//                recipe_simple_info.get("조리시간").toString(),
//                recipe_simple_info.get("평균별점").toString()));
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Recipe_list_Info recipe = arrRecipe.get(position);
//                Intent intent = new Intent(activity, RecipeActivity.class);
//                intent.putExtra("recipe_class","한식");
//                intent.putExtra("recipe_name",recipe.getName());
//                new Recipe_Info("한식",recipe.getName());
//                startActivity(intent);
//            }
//        });

        return rootView;
    }

    class MainMenu {

        private String menu;
        private int icon;

        public MainMenu(String menu, int icon) {
            this.menu = menu;
            this.icon = icon;
        }

        public String getMenu() {
            return menu;
        }

        public void setItem(String menu) {
            this.menu = menu;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

    class MainMenuAdapter extends BaseAdapter {
        ArrayList<MainMenu> menus = new ArrayList<MainMenu>();
        @Override
        public int getCount() {
            return menus.size();
        }

        public void addItem(MainMenu menu){
            menus.add(menu);
        }

        @Override
        public MainMenu getItem(int i) {
            return menus.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            GridViewer mainmenuViewer = new GridViewer(mContext);
            mainmenuViewer.setItem(menus.get(i));
            return mainmenuViewer;
        }
    }
}