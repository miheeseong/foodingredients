package com.mh.foodingredients.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mh.foodingredients.R;
import com.mh.foodingredients.activity.DetailActivity;
import com.mh.foodingredients.model.IngredientItem;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflate;
    ViewHolder viewHolder;
    ArrayList<IngredientItem> items;
    int index;

    public SearchAdapter(Context context, ArrayList<IngredientItem> items) {
        this.context = context;
        this.items = items;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, null);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.label.setText(items.get(position).ingredientName);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("index", position);
                //flag 추가하기
                IngredientItem selectIngreId;

                selectIngreId = items.get(index);
                String saveIngreId = selectIngreId.ingreId;
                intent.putExtra("saveIngreId", saveIngreId);

                System.out.println("==================index:"+index);
                context.startActivity(intent);

            }
        });

        return convertView;
    }

    class ViewHolder{
        public TextView label;
    }
}
