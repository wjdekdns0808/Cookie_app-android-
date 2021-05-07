package com.example.team_project_cookie;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment3 extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListAdapter listAdapter;
    ListView review_view;
    public RecipeFragment3() {
        // Required empty public constructor
    }

    public static RecipeFragment3 newInstance(String param1, String param2) {
        RecipeFragment3 fragment = new RecipeFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe3, container, false);
        ArrayList<Review_data> review_list = new ArrayList<>();
        review_list.clear();

        review_view = (ListView)rootView.findViewById(R.id.recipe_review_list);

        HashMap re_map= ((Recipe_Info)Recipe_Info.context_main).review_map_key;
        HashMap re_map_data;

        for(Object key: re_map.keySet()){
            re_map_data = (HashMap)re_map.get(key);

            Review_data review_data = new Review_data();
            review_data.setdata(re_map_data.get("name").toString(),(String)re_map_data.get("text").toString(),
                    Float.parseFloat(String.valueOf(re_map_data.get("star"))),re_map_data.get("date").toString());

            review_list.add(review_data);

        }

        listAdapter = new ListAdapter(getActivity(),R.layout.review_layout,review_list);

        review_view.setAdapter(listAdapter);

        return rootView;
    }
}
class ListAdapter extends ArrayAdapter {
    ArrayList reviewList ;
    Context context;
    int resc;
    public ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        reviewList = objects;
        resc = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(resc,null);
        }
        Review_data data = (Review_data)reviewList.get(position);
        ((TextView)convertView.findViewById(R.id.username_review)).setText(data.name);
        ((TextView)convertView.findViewById(R.id.user_review)).setText(data.review_text);
        ((TextView)convertView.findViewById(R.id.date_review)).setText(data.review_date);
        ((RatingBar)convertView.findViewById(R.id.userrating_review)).setRating(data.star);

        return convertView;


    }
}
