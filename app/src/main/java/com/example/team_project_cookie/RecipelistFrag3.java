package com.example.team_project_cookie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.example.team_project_cookie.RecipelistAdapter.cmpName;
import static com.example.team_project_cookie.RecipelistAdapter.cmpRating;
import static com.example.team_project_cookie.RecipelistAdapter.cmpTime;


public class RecipelistFrag3 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    HashMap recipe_list_class;
    HashMap recipe_list_food;
    HashMap recipe_simple_info;

    public RecipelistFrag3() {
        // Required empty public constructor
    }

    public static RecipelistFrag3 newInstance(String param1, String param2) {
        RecipelistFrag3 fragment = new RecipelistFrag3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Context mContext;
    ListView listView;
    RecipelistAdapter recipelistAdapter;
    RecipelistActivity activity;
    String[] sortCategory = {"기본순", "별점높은순", "조리시간 짧은순"};
    Spinner spinner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        activity = (RecipelistActivity) getActivity();
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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_recipelist3, container, false);
        listView = (ListView)rootView.findViewById(R.id.listview_recipe3);

        recipe_list_class = new HashMap();
        recipe_list_food = new HashMap();
        recipe_list_class = ((Get_Recipe_List)Get_Recipe_List.context_main).recipe_list_class;
        recipe_list_food = (HashMap)recipe_list_class.get("중식");
        ArrayList<Recipe_list_Info> arrRecipe = new ArrayList<>();

        recipelistAdapter = new RecipelistAdapter();
        recipelistAdapter.foods = arrRecipe;
        recipelistAdapter.context = mContext;

        for (Object food_name : recipe_list_food.keySet()){
            recipe_simple_info = new HashMap();
            recipe_simple_info = (HashMap)recipe_list_food.get(food_name);
            String img_path = recipe_simple_info.get("이미지").toString();
            recipelistAdapter.addItem(new Recipe_list_Info(mContext,img_path, food_name.toString(),
                    recipe_simple_info.get("음식소개").toString(),
                    recipe_simple_info.get("조리시간").toString(),
                    recipe_simple_info.get("평균별점").toString()));
        }

        listView.setAdapter(recipelistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe_list_Info recipe = arrRecipe.get(position);
                Intent intent = new Intent(activity, RecipeActivity.class);
                intent.putExtra("recipe_class","중식");
                intent.putExtra("recipe_name",recipe.getName());
                new Recipe_Info("중식",recipe.getName());
                Log.i("kihoon","중식클릭"+recipe.getName());
                startActivity(intent);
            }
        });

        spinner = (Spinner)rootView.findViewById(R.id.spinner3);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_item, sortCategory);
        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : Collections.sort(arrRecipe, cmpName);
                        break;
                    case 1 : Collections.sort(arrRecipe, cmpRating);
                        Collections.reverse(arrRecipe);
                        break;
                    case 2 : Collections.sort(arrRecipe, cmpTime);
                        break;
                }

                recipelistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }
}