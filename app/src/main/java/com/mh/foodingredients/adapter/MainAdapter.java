package com.mh.foodingredients.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mh.foodingredients.R;
import com.mh.foodingredients.model.IngredientItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainAdapter extends BaseAdapter {

    String sStartDate;
    Context context;
    ArrayList<IngredientItem> items;

    //생성자
    public MainAdapter(Context context, ArrayList<IngredientItem> items) {
        this.context = context;
        this.items = items;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return items.size();
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public IngredientItem getItem(int position) {
        return items.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = view.findViewById(R.id.tv_name);
        TextView storageTextView = view.findViewById(R.id.tv_storage);
        TextView runningDaysTextView = view.findViewById(R.id.tv_running_days);
        TextView remainsTextView = view.findViewById(R.id.tv_remains);

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        IngredientItem item = getItem(position);
        System.out.println("==============position : "+position);

        //경과일 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        sStartDate = item.purchaseDate;
        String sEndDate = getTime;

        try {
            Date dStartDate = sdf.parse(sStartDate);
            Date dEndDate = sdf.parse(sEndDate);

            long calDate = dEndDate.getTime() - dStartDate.getTime();
            long calDateDays = calDate / (24 * 60 * 60 * 1000);

            calDateDays = Math.abs(calDateDays);

            item.runningDays = (calDateDays + 1);

        } catch (ParseException e) { }

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(item.ingredientName);
        storageTextView.setText(item.storage);
        runningDaysTextView.setText(String.valueOf(item.runningDays + item.runningDaysText));
        remainsTextView.setText(String.valueOf(item.remains + item.unitType));

        return view;

    }

}
