package com.mh.foodingredients.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.adapter.ShoppingListAdapter;
import com.mh.foodingredients.model.ShoppingItem;
import com.mh.foodingredients.model.StorageItem;

import java.util.ArrayList;

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

                //저장공간추가 dialog
               // view.findViewById(R.id.addShoppingList).setOnClickListener(new View.OnClickListener() {
               /*     @Override
                    public void onClick(View v) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        ad.setTitle("재료명");// 제목 설정

                        // EditText 삽입하기
                        final EditText et = new EditText(getActivity());
                        ad.setView(et);

                        ad.setMessage("수량");//

                        final EditText it = new EditText(getActivity());
                        ad.setView(it);


                        // 확인 버튼 설정
                        ad.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Text 값 받아서 로그 남기기
                                String value = et.getText().toString();

                                ShoppingItem item = new ShoppingItem();
                                item.ingredientName = value;

                                if (FoodIngreInfoApplication.mUserItem.shoppingItems == null) {
                                    FoodIngreInfoApplication.mUserItem.shoppingItems = new ArrayList<>();
                                }

                                ArrayList<ShoppingItem> items = FoodIngreInfoApplication.mUserItem.shoppingItems;

                                for(int i=0; i<items.size(); i++){
                                    if(value.equals(items.get(i).ingredientName)){
                                        Toast.makeText(getActivity(), "동일한 재료가 구매항목에 있습니다.", Toast.LENGTH_SHORT).show();
                                        return;

                                    }
                                }

                                items.add(item);
                                FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);



                                dialog.dismiss();     //닫기

                            }
                        });
                        // 취소 버튼 설정
                        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //닫기

                            }
                        });*/

                        // 창 띄우기
   /*             if(!StorageActivity.this.isFinishing())
                {
                    ad.show();
                }*/

                  //      ad.show();


                        //Intent intent = new Intent(StorageActivity.this, StorageAddActivity.class);
/*                Intent intent = new Intent(getActivity(), StorageAddActivity.class);
                System.out.println("==============start=================");
                startActivity(intent);*/
                    }

                });
 /*           }
        });
*/

        if (FoodIngreInfoApplication.mUserItem.shoppingItems == null) {
            mDefaultLayout.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
            //return;
        } else {

            mDefaultLayout.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            //mStAdapter = new StorageAdapter(StorageActivity.this, FoodIngreInfoApplication.mUserItem.StorageItems);
            mShoppingAdapter = new ShoppingListAdapter(getActivity(), FoodIngreInfoApplication.mUserItem.shoppingItems);
            //Log.d("mStAdapter", "" + mStAdapter);
            mListView.setAdapter(mShoppingAdapter);

        }

        return view;

    }
}