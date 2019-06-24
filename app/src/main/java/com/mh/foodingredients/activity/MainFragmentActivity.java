package com.mh.foodingredients.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;

public class MainFragmentActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    //int action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        Fragment defaultMain = new HomeActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, defaultMain).commit();

    /*  // 각 화면에서 넘겨주는 flag로 default 화면 변경
        action = getIntent().getFlags();

        if (action==0) {

            //첫화면 Home 고정
            Fragment defaultMain = new HomeActivity();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, defaultMain).commit();

        } else if (action==1) {

            System.out.println("==========acton_list========action:"+action);
            Fragment defaultMain = new StorageActivity();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, defaultMain).commit();

        } else if (action==3) {

        System.out.println("=================action:"+action);
        Fragment defaultMain = new ShoppingListActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, defaultMain).commit();

    }
    */

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch(item.getItemId()) {

                            // home
                            case R.id.action_home:
                                selectedFragment = new HomeActivity();
                                break;

                            //저장공간
                            case R.id.acton_list:
                                selectedFragment = new StorageActivity();
                                break;

                            // 검색
                            case R.id.action_search:
                                selectedFragment = new SearchActivity();
                                break;

                            // 쇼핑리스트
                            case R.id.action_shopping:
                                selectedFragment = new ShoppingListActivity();
                                break;

                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, selectedFragment).commit();
                        return true;
                    }
        });
    }

    //메뉴바 로그아웃 구현
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //로그아웃
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FoodIngreInfoApplication.mAuth.signOut();

        Intent intent = new Intent(MainFragmentActivity.this, LoginActivity.class);

        startActivity(intent);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = auto.edit();
        //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지움
        editor.clear();
        editor.commit();
        finish();

        return true;
    }
}
