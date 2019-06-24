package com.mh.foodingredients.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.adapter.RecyclerViewAdapter;
import com.mh.foodingredients.model.IngredientItem;
import com.mh.foodingredients.model.StorageItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //recyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout mDefaultLayout;
    TextView mEmptyTextView;
    EditText mStorageEditTextSample;
    int index;
    StorageItem mStorageItem;

    // 선택된 storage와 동일한 값을 가진 data 저장공간
    IngredientItem mIgdtItem = new IngredientItem();
    ArrayList<IngredientItem> inItem = new ArrayList<IngredientItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageEditTextSample = findViewById(R.id.storageEditTextSample);

        //recyclerView
        mRecyclerView = findViewById(R.id.ingredntRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        //gridView 1라인에 배치item 수
        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (getIntent() != null) {

            index = getIntent().getIntExtra("index", 0);

            if (FoodIngreInfoApplication.mUserItem.StorageItems != null) {
                mStorageItem = FoodIngreInfoApplication.mUserItem.StorageItems.get(index);
            }
        }
        mStorageEditTextSample.setText(mStorageItem.storage);
        String mainText = mStorageEditTextSample.getText().toString();
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle(mainText) ;

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(MainActivity.this, AddIngredientActivity.class);
            intent.putExtra("index", index);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            }
        });

        mDefaultLayout = findViewById(R.id.defaultLayout);
        mEmptyTextView = findViewById(R.id.emptyTextView);

        // 데이터 유무체크
        if (FoodIngreInfoApplication.mUserItem.ingredientItems == null) {
            mDefaultLayout.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
            return;

        } else {
            mDefaultLayout.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);
            String storageFilter = mStorageItem.storage;

            // List에 따로 담기
            if (FoodIngreInfoApplication.mUserItem.ingredientItems != null) {

                for (int j = 0; j < FoodIngreInfoApplication.mUserItem.ingredientItems.size(); j++) {
                    mIgdtItem = FoodIngreInfoApplication.mUserItem.ingredientItems.get(j);
                    if (storageFilter.equals(mIgdtItem.storage)) {
                        inItem.add(mIgdtItem);
                    }
                }
            }

            // 선택된 storage 값을 가진 데이터만 따로 저장(inItem)하여 mAdapter로 보냄
            mRecyclerAdapter = new RecyclerViewAdapter(MainActivity.this, inItem);
            mRecyclerView.setAdapter(mRecyclerAdapter);

        }
    }
}

