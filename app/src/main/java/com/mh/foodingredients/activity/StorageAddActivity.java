package com.mh.foodingredients.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.model.StorageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageAddActivity extends AppCompatActivity {

    Map mStorage = new HashMap();
    String sStorageEditText;


    public ExpandableListView expandableListView; // ExpandableListView 변수 선언
    //public CustomExpandableListViewAdapter mCustomExpListViewAdapter; // 위 ExpandableListView를 받을 CustomAdapter(2번 class에 해당)를 선언
    //public ArrayList<String> parentList; // ExpandableListView의 Parent 항목이 될 List 변수 선언
    //public ArrayList<ChildListData> fruit; // ExpandableListView의 Child 항목이 될 List를 각각 선언
    //public ArrayList<ChildListData> vegetables;
    //public ArrayList<ChildListData> etc;
    //public HashMap<String, ArrayList<ChildListData>> childList; // 위 ParentList와 ChildList를 연결할 HashMap 변수 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_add);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("저장공간 추가") ;

        //mStorage = FoodIngreInfoApplication.mUserItem.mStorage;
        final EditText mStorageEditText;
        Button mSaveButton;

        mStorageEditText = findViewById(R.id.storageEditText);
        mSaveButton = findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               StorageItem item = new StorageItem();
                item.storage = mStorageEditText.getText().toString();

                sStorageEditText = mStorageEditText.getText().toString();



                if (FoodIngreInfoApplication.mUserItem.StorageItems == null) {
                    FoodIngreInfoApplication.mUserItem.StorageItems = new ArrayList<>();
                }

           /*     if (FoodIngreInfoApplication.mUserItem.StorageItems == null) {
                    FoodIngreInfoApplication.mUserItem.StorageItems = new ArrayList<>();
                }*/

                ArrayList<StorageItem> items = FoodIngreInfoApplication.mUserItem.StorageItems;
                //ArrayList items = FoodIngreInfoApplication.mUserItem.StorageItems;

                //mStorage.put("storage", sStorageEditText );
                for(int i=0; i<items.size(); i++){
                    if(sStorageEditText.equals(items.get(i).storage)){
                        Toast.makeText(StorageAddActivity.this, "동일한 저장공간이 있습니다.", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }


                items.add(item);
                FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);


                Intent intent = new Intent(StorageAddActivity.this, StorageActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

}
