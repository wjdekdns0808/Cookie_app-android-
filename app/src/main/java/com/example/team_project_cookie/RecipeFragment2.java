package com.example.team_project_cookie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment2 extends Fragment {

    HashMap <String, String> recipe_material;
    HashMap <String, String> my_material;
    HashMap <String, HashMap<String,String>> my_material_group;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment2 newInstance(String param1, String param2) {
        RecipeFragment2 fragment = new RecipeFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Context mContext;
    GridView gridView;
    RefrigeratorAdapter refrigeratorAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
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

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_recipe2, container, false);
        gridView = (GridView)rootView.findViewById(R.id.list_grid);
        refrigeratorAdapter = new RefrigeratorAdapter();
        recipe_material =  new HashMap<String, String>();
        recipe_material = ((Recipe_Info)Recipe_Info.context_main).recipe_material;

        my_material = new HashMap<String, String>();
        my_material = ((Get_User_Info)Get_User_Info.context_main).my_material_all_map;

        my_material_group = new HashMap<String, HashMap<String,String>>();
        my_material_group = ((Get_User_Info)Get_User_Info.context_main).my_material_map;

        HashMap<String,String> material_img_path= new HashMap<String,String>();
        material_img_path = ((Get_Sever_Data)Get_Sever_Data.context_main).material_all;


        gridView.setAdapter(refrigeratorAdapter);
        Iterator<String> recipe_mapIter = recipe_material.keySet().iterator();
        while (recipe_mapIter.hasNext()) {
            String recipe_key = recipe_mapIter.next();
            String recipe_value = recipe_material.get(recipe_key);
            String img_path = material_img_path.get(recipe_key);

            String intStr = recipe_value.replaceAll("[^0-9]", "");//숫자제외 지우기
            int recipe_value_int = Integer.parseInt(intStr);
            String my_value="";
            int my_value_int = -1;

            if (my_material.containsKey(recipe_key)){// 나의 냉장고재료와 레시피재료 비교
               my_value = my_material.get(recipe_key);
                 my_value_int = Integer.parseInt(my_value);

            }
            if (img_path == null)
            {
                img_path = "test.jpg";
            }

                if (my_value_int == 0)
                    my_value_int = recipe_value_int;
                if (recipe_value_int <= my_value_int)
                    refrigeratorAdapter.addItem(new Refrigerator(rootView.getContext(),recipe_key, recipe_value, img_path));//컬러사진
                else {
                    img_path = img_path.replaceAll("color","black");
                    refrigeratorAdapter.addItem(new Refrigerator(rootView.getContext(),recipe_key, recipe_value, img_path));//흑백사진

                }
        }
        ArrayList<Refrigerator> arrayList = refrigeratorAdapter.items;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_name = refrigeratorAdapter.items.get(position).getItem();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com/np/search?component=&q="+item_name+"&channel=user"));
                startActivity(intent);
            }
        });


        return rootView;
    }

    class RefrigeratorAdapter extends BaseAdapter {
        ArrayList<Refrigerator> items = new ArrayList<Refrigerator>();
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Refrigerator foodItem){
            items.add(foodItem);
        }

        @Override
        public Refrigerator getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            GridViewer refrigeratorViewer = new GridViewer(mContext);
            refrigeratorViewer.setItem(items.get(i));
            return refrigeratorViewer;
        }

    }

}