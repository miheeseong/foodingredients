package com.mh.foodingredients.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.activity.MainActivity;
import com.mh.foodingredients.model.IngredientItem;
import com.mh.foodingredients.model.StorageItem;

import java.util.ArrayList;

public class StorageAdapter extends BaseAdapter {

    Context context;
    ArrayList<StorageItem> items;
    StorageItem in;
    String storageTxt;
    String newStorage;
    int index;

    public StorageAdapter(Context context, ArrayList<StorageItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
       return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storage, parent, false);
        }
        TextView storageTextView = view.findViewById(R.id.iv_storageTitle);
        Button mDeleteStorage = view.findViewById(R.id.deleteButton);
        Button mUpdateStorage = view.findViewById(R.id.updateButton);
        StorageItem item = (StorageItem) getItem(position);
        storageTextView.setText(item.storage);

        view.setTag(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("index", position);
                context.startActivity(intent);

            }
        });

        //button position값 보내기 -> onClick
        mDeleteStorage.setTag(position);
        mDeleteStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int position = Integer.parseInt((v.getTag().toString()));

                //index에 position대입
                index = position;
                in = FoodIngreInfoApplication.mUserItem.StorageItems.get(index);

                //저장공간명 Text로 불러오기
                storageTxt = FoodIngreInfoApplication.mUserItem.StorageItems.get(index).storage;
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                alert_confirm.setMessage("["+storageTxt+"]에 저장된 재료들도 모두 삭제됩니다. \n삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                ArrayList<StorageItem> items = FoodIngreInfoApplication.mUserItem.StorageItems;
                                ArrayList<IngredientItem> ingreItem = FoodIngreInfoApplication.mUserItem.ingredientItems;

                                //저장공간안에 재료들 삭제
                                String sStorage;
                                for (int i=0; i<ingreItem.size(); i++) {

                                    sStorage = ingreItem.get(i).storage;

                                    if (storageTxt.equals(sStorage)) {

                                        ingreItem.remove(i);
                                        //삭제되면서 index크기가 줄어들기때문에 i값도 같이 줄여준다
                                        i--;

                                    }

                                }
                                items.remove(in);
                                FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        //수정버튼
        mUpdateStorage.setTag(position);
        mUpdateStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = Integer.parseInt((v.getTag().toString()));
                index = position;

                AlertDialog.Builder ad = new AlertDialog.Builder(context);

                ad.setTitle("저장공간 수정");// 제목 설정
                ad.setMessage("저장된 재료들도 모두 수정됩니다.");

                // EditText 삽입하기
                final EditText et = new EditText(context);
                ad.setView(et);
                et.setText(items.get(position).storage);
                newStorage = items.get(position).storage;

                // 확인 버튼 설정
                ad.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Text 값 받아서 로그 남기기
                        String value = et.getText().toString();

                        StorageItem item = new StorageItem();
                        item.storage = value;

                        if (FoodIngreInfoApplication.mUserItem.StorageItems == null) {
                            FoodIngreInfoApplication.mUserItem.StorageItems = new ArrayList<>();
                        }

                        ArrayList<StorageItem> items = FoodIngreInfoApplication.mUserItem.StorageItems;

                        for (int i=0; i<items.size(); i++) {

                            if (value.equals(items.get(i).storage)) {

                                Toast.makeText(context, "동일한 저장공간이 있습니다.", Toast.LENGTH_SHORT).show();
                                return;

                            }
                        }
                        String sStorage;
                        ArrayList<IngredientItem> ingreItem = FoodIngreInfoApplication.mUserItem.ingredientItems;

                        for (int i=0; i<ingreItem.size(); i++) {

                            sStorage = ingreItem.get(i).storage;

                            if (newStorage.equals(sStorage)) {

                                ingreItem.get(i).storage = value;

                            }

                        }

                        //items.add(item);
                        items.set(index, item);
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
                });

                ad.show();

            }
        });
        return view;
    }
}
