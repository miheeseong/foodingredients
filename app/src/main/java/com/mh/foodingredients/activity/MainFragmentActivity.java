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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        //첫화면 Home 고정
        Fragment defaultMain = new HomeActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, defaultMain).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch(item.getItemId()) {
                            case R.id.action_home:
                                selectedFragment = new HomeActivity();
                                //textView.setText("Home");
                                break;
                            case R.id.acton_list:
                                selectedFragment = new StorageActivity();
                                //textView.setText("List");
                                break;
                            case R.id.action_search:
                                selectedFragment = new SearchActivity();
                                //textView.setText("Search");
                                break;
                            case R.id.action_shopping:
                                selectedFragment = new ShoppingListActivity();
                                //textView.setText("Shopping");
                                break;

                        }
                        //return true;
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
        //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
        editor.clear();
        editor.commit();
        finish();

        return true;
    }



}
