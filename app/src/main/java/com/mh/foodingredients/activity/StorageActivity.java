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
    int index;

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
        //mListView = findViewById(R.id.listView);
        mListView = view.findViewById(R.id.listView);
        mAddStorage = view.findViewById(R.id.addStorage);
        mDeleteStorage = view.findViewById(R.id.deleteButton);


        //저장공간추가 dialog
        view.findViewById(R.id.addStorage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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

                // 창 띄우기
   /*             if(!StorageActivity.this.isFinishing())
                {
                    ad.show();
                }*/

                ad.show();


                //Intent intent = new Intent(StorageActivity.this, StorageAddActivity.class);
/*                Intent intent = new Intent(getActivity(), StorageAddActivity.class);
                System.out.println("==============start=================");
                startActivity(intent);*/
            }

        });


        // 데이터 유무체크
        if (FoodIngreInfoApplication.mUserItem.StorageItems == null) {
            mDefaultLayout.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
            //return;
        } else {

            mDefaultLayout.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            //mStAdapter = new StorageAdapter(StorageActivity.this, FoodIngreInfoApplication.mUserItem.StorageItems);
            mStAdapter = new StorageAdapter(getActivity(), FoodIngreInfoApplication.mUserItem.StorageItems);
            Log.d("mStAdapter", "" + mStAdapter);
            mListView.setAdapter(mStAdapter);

/*            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Intent intent = new Intent(StorageActivity.this, MainActivity.class);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("index", position);
                    index = position;
                    System.out.println("==================index:"+index);
                    startActivity(intent);
                }
            });*/

           /* mDeleteStorage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                    alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    //index값 가져오기(전역변수)
                                    StorageItem in = FoodIngreInfoApplication.mUserItem.StorageItems.get(index);
                                    System.out.println("===================index:"+index);
                                    ArrayList<StorageItem> items = FoodIngreInfoApplication.mUserItem.StorageItems;

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
            });*/



      /*      mListView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //AlertDialog.Builder alert_confirm = new AlertDialog.Builder(StorageActivity.this);
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                    alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    //index값 가져오기(전역변수)
                                    StorageItem in = FoodIngreInfoApplication.mUserItem.StorageItems.get(index);
                                    System.out.println("===================index:"+index);
                                    ArrayList<StorageItem> items = FoodIngreInfoApplication.mUserItem.StorageItems;

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

                    return true;
                }
            });

            */


        }
     //   return inflater.inflate(R.layout.activity_storage, container, false);
        return view;
    }

/*    private boolean isFinishing() {
        return false;
    }*/
}





