package com.mh.foodingredients.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.adapter.ShoppingListAdapter;

public class ShoppingListActivity extends Fragment {

    LinearLayout mDefaultLayout;
    TextView mEmptyTextView;
    ShoppingListAdapter mShoppingAdapter;
    ListView mListView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_shopping_list, container, false);
        mDefaultLayout = view.findViewById(R.id.defaultLayout);
        mEmptyTextView = view.findViewById(R.id.emptyTextView);
        mListView = view.findViewById(R.id.listView);

        Button mShoppingListAdd = view.findViewById(R.id.addShoppingList);
        mShoppingListAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoppingListAddActivity.class);
                startActivity(intent);

                    }

                });

        if (FoodIngreInfoApplication.mUserItem.shoppingItems == null) {
            mDefaultLayout.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {

            mDefaultLayout.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            mShoppingAdapter = new ShoppingListAdapter(getActivity(), FoodIngreInfoApplication.mUserItem.shoppingItems);
            mListView.setAdapter(mShoppingAdapter);

        }

        return view;

    }
}