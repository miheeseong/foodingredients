package com.mh.foodingredients.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.activity.AddIngredientActivity;
import com.mh.foodingredients.model.ShoppingItem;

import java.util.ArrayList;

public class ShoppingListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ShoppingItem> shoppingItems;

    public ShoppingListAdapter(Context context, ArrayList<ShoppingItem> shoppingItems) {
        this.context = context;
        this.shoppingItems = shoppingItems;
    }

    @Override
    public int getCount() {
        return shoppingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView nameTextView = view.findViewById(R.id.tv_name);
        TextView purchaseCountTextView = view.findViewById(R.id.tv_purchaseCount);
        TextView unitTypeTextView = view.findViewById(R.id.tv_storage);

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        ShoppingItem item = (ShoppingItem) getItem(position);
        System.out.println("==============position : "+position);


        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(item.ingredientName);
        purchaseCountTextView.setText(String.valueOf(item.purchaseCount)+item.unitType);
        //unitTypeTextView.setText(item.unitType);


        Button mDeleteShoppingList = view.findViewById(R.id.deleteButton);
        Button mBuyShoppingList = view.findViewById(R.id.buyButton);

        mDeleteShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingItems.remove(position);
                FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);

            }
        });
        mBuyShoppingList.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                int i=1;

                Intent intent = new Intent(context, AddIngredientActivity.class);
                intent.putExtra("index", position);
                intent.addFlags(i);
                context.startActivity(intent);

            }
        });

        return view;
    }

}
