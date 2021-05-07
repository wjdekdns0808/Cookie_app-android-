package com.example.team_project_cookie;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class RecipelistActivity extends AppCompatActivity {

    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        manager = getSupportFragmentManager();

        ViewPager vp = (ViewPager)findViewById(R.id.recipelist_viewpager);
        VPAdapter adapter = new VPAdapter(manager, 0);
        vp.setAdapter(adapter);

        // 연동
        TabLayout tab = findViewById(R.id.tab_recipelist);
        tab.setupWithViewPager(vp);

        Intent intent = getIntent();
        int clickPosition = intent.getIntExtra("clickMenu",0);
        vp.setCurrentItem(clickPosition);

    }
}