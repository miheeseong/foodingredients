package com.mh.foodingredients.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mh.foodingredients.R;

public class SearchActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_search, container, false);
        //View view = inflater.inflate(R.layout.activity_search, container, false);

/*

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("ActionBar Title by setTitle()") ;
*/


    }

}
