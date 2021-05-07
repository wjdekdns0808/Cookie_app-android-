package com.example.team_project_cookie;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeMenu extends AppCompatActivity {
    int home_toobar_ids[] = {R.drawable.home,R.drawable.search,R.drawable.refrigerator,R.drawable.star,R.drawable.user};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        ViewPager vp = (ViewPager)findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        // 연동
        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

        ArrayList<Integer> images = new ArrayList<Integer>();
        for (int i = 0 ; i<home_toobar_ids.length;i++) {
            images.add(home_toobar_ids[i]);
        }
        for(int i=0; i<5; i++) tab.getTabAt(i).setIcon(images.get(i));
    }
}