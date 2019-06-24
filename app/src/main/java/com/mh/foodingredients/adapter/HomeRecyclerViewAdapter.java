package com.mh.foodingredients.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mh.foodingredients.R;
import com.mh.foodingredients.activity.DetailActivity;
import com.mh.foodingredients.model.IngredientItem;
import com.mh.foodingredients.model.ItemClickListener;
import com.mh.foodingredients.model.PicassoTransformations;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<IngredientItem> items;
    long calExDateDays;
    long changeCalExDateDays;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_home_recycler, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemClickListener itemClickListener;

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());

        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        public ImageView mImageView;
        public TextView mIngredientName;
        public TextView mRemains;
        public TextView mRunningDays;
        public TextView mExpireDate;

        ViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.ingredientImages);
            mIngredientName = v.findViewById(R.id.tv_ingredntNm);
            mRemains = v.findViewById(R.id.tv_remains);
            mRunningDays = v.findViewById(R.id.tv_running_days);
            mExpireDate = v.findViewById(R.id.tv_expireDate);
            v.setOnClickListener(this);

        }

    }

    //생성자
    public HomeRecyclerViewAdapter(Context context, ArrayList<IngredientItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //경과일 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        String sStartDate = items.get(position).purchaseDate;
        String sExpireDate = items.get(position).expireDate;
        String sEndDate = getTime;

        try {
            Date dStartDate = sdf.parse(sStartDate);
            Date dExpireDate = sdf.parse(sExpireDate);
            Date dEndDate = sdf.parse(sEndDate);

            long calDate = dEndDate.getTime() - dStartDate.getTime();
            long calDateDays = calDate / (24 * 60 * 60 * 1000);

            long exSetDate = dEndDate.getTime() - dExpireDate.getTime();
            changeCalExDateDays = exSetDate / (24 * 60 * 60 * 1000);


            //경과일
            calDateDays = Math.abs(calDateDays);
            //유통기한
            calExDateDays = Math.abs(changeCalExDateDays);
            items.get(position).runningDays = (calDateDays + 1);

        } catch (ParseException e) { }

        Uri uri = Uri.parse(String.valueOf(items.get(position).img));

        //URI를 bitmap으로 변환하여 imageView에 적용시켜주는 API-picasso
        Picasso.with(context)
               .load((String.valueOf(uri)))
               .placeholder(R.drawable.no_image_white)
               //.resize(360, 420)
               //.resize(1000, 1000)
               //.centerCrop()
               .fit().centerInside()
               //.fit()
                //imageView에 사이즈 맞추기
               .transform(PicassoTransformations.resizeTransformation)
               .into(holder.mImageView);

        holder.mIngredientName.setText(items.get(position).ingredientName);
        holder.mRemains.setText("남은 수량 " + String.valueOf(items.get(position).remains)+items.get(position).unitType);
        holder.mRunningDays.setText(String.valueOf(items.get(position).runningDays)+"일 경과");

        if(!items.get(position).expireDate.equals("")){
            holder.mExpireDate.setVisibility(View.VISIBLE);

            if(changeCalExDateDays > 0){
                holder.mExpireDate.setText("유통기한 "+String.valueOf(calExDateDays)+"일 후");
            }
            if(changeCalExDateDays < 0){
                holder.mExpireDate.setText("유통기한 "+String.valueOf(calExDateDays)+"일 전");
            }
            if(changeCalExDateDays == 0){
                holder.mExpireDate.setText("유통기한 당일");
            }

        } else {
            holder.mExpireDate.setVisibility(View.GONE);

        }

        holder.setItemClickListener(new ItemClickListener() {

            @Override
            public void onItemClick(View v, int pos) {

                if (pos >= 0) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    int index = pos;
                    //현재 position(index)의 재료고유번호(ingreId)를 담을 공간
                    IngredientItem selectIngreId;

                    selectIngreId = items.get(index);
                    String saveIngreId = selectIngreId.ingreId;
                    intent.putExtra("saveIngreId", saveIngreId);
                    context.startActivity(intent);

                }else { }

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

