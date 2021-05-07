package com.example.team_project_cookie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;


public class Fragment4 extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public static Context context_main;
    public Fragment4() {
        // Required empty public constructor

    }

    public static Fragment4 newInstance(String param1, String param2) {
        Fragment4 fragment = new Fragment4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Context mContext;
    ListView listView;
    RecipelistAdapter recipelistAdapter;
    HomeMenu activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        activity = (HomeMenu) getActivity();
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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_fragment4, container, false);
        listView = (ListView)rootView.findViewById(R.id.lv_bookmark);

        ArrayList<Recipe_list_Info> arrRecipe = new ArrayList<>();

        recipelistAdapter = new RecipelistAdapter();
        recipelistAdapter.foods = arrRecipe;
        recipelistAdapter.context = mContext;
        HashMap user_favorite =((Get_User_Info)Get_User_Info.context_main).favorite_map;
        HashMap recipe_data_all = ((Get_Recipe_List)Get_Recipe_List.context_main).recipe_list_class;


        for(Object category: recipe_data_all.keySet()){
            HashMap recipe_data = (HashMap) recipe_data_all.get(category);
            for(Object recipe_name :recipe_data.keySet()){
                for(Object user_favorite_key: user_favorite.keySet()) {
                    if (recipe_name.equals(user_favorite_key)) {
                        HashMap recipe_data_in = (HashMap)recipe_data.get(recipe_name);
                        recipelistAdapter.addItem(new Recipe_list_Info(mContext,recipe_data_in.get("이미지").toString(),recipe_name.toString(), recipe_data_in.get("음식소개").toString()
                                , recipe_data_in.get("조리시간").toString(),recipe_data_in.get("평균별점").toString()));
                    }
                }
            }

        }


        listView.setAdapter(recipelistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recipe_class= "";
                for(Object category: recipe_data_all.keySet()) {
                    HashMap recipe_data = (HashMap) recipe_data_all.get(category);
                    for (Object recipe_name : recipe_data.keySet()) {
                        if (recipe_name.equals(arrRecipe.get(position).getName())){
                            recipe_class = category.toString();
                        }
                    }
                }
                Recipe_list_Info recipe = arrRecipe.get(position);
                Intent intent = new Intent(activity, RecipeActivity.class);
                intent.putExtra("recipe_class",recipe_class);
                intent.putExtra("recipe_name",recipe.getName());

                new Recipe_Info(recipe_class,recipe.getName());
                startActivity(intent);

            }
        });

        return rootView;
    }
}