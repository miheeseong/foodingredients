package com.mh.foodingredients.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.mh.foodingredients.adapter.StorageAdapter;
import com.mh.foodingredients.model.StorageItem;

import java.util.ArrayList;

public class StorageActivity extends Fragment {

    LinearLayout mDefaultLayout;
    TextView mEmptyTextView;
    ListView mListView;
    StorageAdapter mStAdapter;
    Button mAddStorage;
    Button mDeleteStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_storage, container, false);

        mDefaultLayout = view.findViewById(R.id.defaultLayout);
        mEmptyTextView = view.findViewById(R.id.emptyTextView);
        mListView = view.findViewById(R.id.listView);
        mAddStorage = view.findViewById(R.id.addStorage);
        mDeleteStorage = view.findViewById(R.id.deleteButton);

        //저장공간추가 dialog
        view.findViewById(R.id.addStorage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("저장공간");// 제목 설정

                // EditText 삽입하기
                final EditText et = new EditText(getActivity());
                ad.setView(et);

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

                        for(int i=0; i<items.size(); i++){
                            if(value.equals(items.get(i).storage)){
                                Toast.makeText(getActivity(), "동일한 저장공간이 있습니다.", Toast.LENGTH_SHORT).show();
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
                });

                ad.show();
            }

        });

        // 데이터 유무체크
        if (FoodIngreInfoApplication.mUserItem.StorageItems == null) {
            mDefaultLayout.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {

            mDefaultLayout.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            mStAdapter = new StorageAdapter(getActivity(), FoodIngreInfoApplication.mUserItem.StorageItems);
            Log.d("mStAdapter", "" + mStAdapter);
            mListView.setAdapter(mStAdapter);

        }
        return view;
    }

}





