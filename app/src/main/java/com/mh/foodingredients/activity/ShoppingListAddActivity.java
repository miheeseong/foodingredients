package com.mh.foodingredients.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.model.IngredientItem;
import com.mh.foodingredients.model.ShoppingItem;

import java.util.ArrayList;

public class ShoppingListAddActivity extends AppCompatActivity {

    Button mCancelButton;
    Button mSaveButton;
    EditText mIngredientName;
    EditText mPurchaseCount;
    Spinner mUnitText;
    String sUnitText;
    int flag;
    int index;
    IngredientItem mRemainItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_add);

        mCancelButton = findViewById(R.id.cancelButton);
        mSaveButton = findViewById(R.id.saveButton);
        mIngredientName = findViewById(R.id.ingredntNmEditText);
        mPurchaseCount = findViewById(R.id.buyQtyEditText);
        mUnitText = findViewById(R.id.purchaseUnitTextSpinner);

        flag = getIntent().getFlags();

        if(flag ==1){

            index = getIntent().getIntExtra("index",index);

            try{

                mRemainItem = FoodIngreInfoApplication.mUserItem.ingredientItems.get(index);
                mIngredientName.setText(mRemainItem.ingredientName);

            }catch (Exception e) { }

        }

        mUnitText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sUnitText = (String) mUnitText.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 저장
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                String ingredientName = mIngredientName.getText().toString();
                int purchaseCount = Integer.parseInt(mPurchaseCount.getText().toString());
                ShoppingItem item = new ShoppingItem(ingredientName, purchaseCount, sUnitText);

                if (TextUtils.isEmpty(ingredientName)) {

                    Toast.makeText(ShoppingListAddActivity.this, getString(R.string.msg_empty_input_ingredient), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (purchaseCount == 0) {
                    Toast.makeText(ShoppingListAddActivity.this, getString(R.string.msg_empty_input_buyCount), Toast.LENGTH_SHORT).show();
                    return;
                }

                item.ingredientName = ingredientName;
                item.purchaseCount = purchaseCount;
                item.unitType = sUnitText;

                if (FoodIngreInfoApplication.mUserItem.shoppingItems == null) {
                    FoodIngreInfoApplication.mUserItem.shoppingItems = new ArrayList<>();
                }

                // 쇼핑리스트 저장과 동시에 재료항목에서 삭제
                ArrayList<ShoppingItem> items = FoodIngreInfoApplication.mUserItem.shoppingItems;
                ArrayList<IngredientItem> igreItems = FoodIngreInfoApplication.mUserItem.ingredientItems;
                IngredientItem in = FoodIngreInfoApplication.mUserItem.ingredientItems.get(index);

                items.add(item);
                igreItems.remove(in);

                //DB에 셋팅
                FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);

/*              // shoppingListActivity로 변경할 수 있게 flag 넘겨주기
                int a=3;
                Intent intent = new Intent(ShoppingListAddActivity.this, MainFragmentActivity.class);
                intent.addFlags(a);
                startActivity(intent);
*/

                finish();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
