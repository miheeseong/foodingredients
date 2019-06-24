package com.mh.foodingredients.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.model.StorageItem;

import java.util.ArrayList;

public class StorageAddActivity extends AppCompatActivity {

    private final String TAG = "StorageAddActivity";

    String sStorageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_add);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("저장공간 추가") ;

        final EditText mStorageEditText;
        Button mSaveButton;

        mStorageEditText = findViewById(R.id.storageEditText);
        mSaveButton = findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                StorageItem item = new StorageItem();
                item.storage = mStorageEditText.getText().toString();

                sStorageEditText = mStorageEditText.getText().toString();

                if (FoodIngreInfoApplication.mUserItem.StorageItems == null) {
                    FoodIngreInfoApplication.mUserItem.StorageItems = new ArrayList<>();
                }

                ArrayList<StorageItem> items = FoodIngreInfoApplication.mUserItem.StorageItems;

                for (int i=0; i<items.size(); i++) {

                    if (sStorageEditText.equals(items.get(i).storage)) {

                        Toast.makeText(StorageAddActivity.this, "동일한 저장공간이 있습니다.", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

                items.add(item);
                FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);

/*              // StorageActivity로 변경할 수 있게 flag 넘겨주기
                int a=1;
                Intent intent = new Intent(StorageAddActivity.this, MainFragmentActivity.class);
                intent.addFlags(a);
                startActivity(intent);
*/
                finish();
            }
        });
    }
}
