package com.mh.foodingredients.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.model.DateSetting;
import com.mh.foodingredients.R;
import com.mh.foodingredients.model.IngredientItem;
import com.mh.foodingredients.model.StorageItem;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    IngredientItem mCurrentItem;
    EditText mIngreEditText;
    EditText mBuyDtEditText;
    EditText mBuyQtyEditText;
    EditText mCloseDtEditText;
    EditText mSaveSpaceEditText;
    EditText mAddInputEditText;
    EditText mLastUseDtEditText;
    EditText mResidualEditText;
    EditText mLastUseCnt;
    EditText mRunningDays;
    TextView mUseUnitTextView;
    TextView mResidualUnitTextView;
    ImageView mImageView;
    Spinner mPurchaseUnitTextSpinner;
    String mPurchaseUnitType;
    DateSetting mDate = new DateSetting();
    IngredientItem item = new IngredientItem();
    String getIngreId;
    String ingreName;
    int index;
    String sStartDate;
    int remains;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        System.out.println("============================1.start======");

        mIngreEditText = findViewById(R.id.ingredntNmEditText);
        mBuyDtEditText = findViewById(R.id.buyDtEditText);
        mBuyQtyEditText = findViewById(R.id.buyQtyEditText);
        mCloseDtEditText = findViewById(R.id.closeDtEditText);
        mSaveSpaceEditText = findViewById(R.id.saveSpaceEditText);
        mAddInputEditText = findViewById(R.id.addInputEditText);
        mLastUseCnt = findViewById(R.id.useQtyEditText);
        mLastUseDtEditText = findViewById(R.id.lastUseDtEditText);
        mPurchaseUnitTextSpinner = findViewById(R.id.purchaseUnitTextSpinner);

        mUseUnitTextView = findViewById(R.id.useUnitTextView);
        mResidualUnitTextView = findViewById(R.id.residualUnitTextView);
        mResidualEditText = findViewById(R.id.residualQtyEditText);
        mRunningDays = findViewById(R.id.runningDaysEditText);
        mImageView = findViewById(R.id.imgPreview);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("재료정보") ;

        if (getIntent() != null) {

            //index = getIntent().getIntExtra("index", 0);
            //Main에서 선택한 재료의 고유번호(ingreId) 받기
            getIngreId = getIntent().getStringExtra("saveIngreId");

            if (FoodIngreInfoApplication.mUserItem.ingredientItems != null) {
                for (int i = 0; i < FoodIngreInfoApplication.mUserItem.ingredientItems.size(); i++) {
                    System.out.println("======i:" + i);
                    item = FoodIngreInfoApplication.mUserItem.ingredientItems.get(i);
                    if (item.ingreId.equals(getIngreId)) {
                        mCurrentItem = FoodIngreInfoApplication.mUserItem.ingredientItems.get(i);
                        index = i;

                    }
                }
            }
        }

            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            Date systemDate = Calendar.getInstance().getTime();
            final String endDt = dataFormat.format(systemDate);

            //전달된 데이터가 있을 경우 데이터 세팅
            if (mCurrentItem != null) {

                mIngreEditText.setText(mCurrentItem.ingredientName);
                mBuyDtEditText.setText(mCurrentItem.purchaseDate);
                mBuyQtyEditText.setText(String.valueOf(mCurrentItem.purchaseCount));
                mCloseDtEditText.setText(mCurrentItem.expireDate);
                mSaveSpaceEditText.setText(mCurrentItem.storage);
                mAddInputEditText.setText(mCurrentItem.additionalData);
                mLastUseDtEditText.setText(mCurrentItem.lastUseDate);
                mPurchaseUnitTextSpinner.setSelection(convertUnitType());
                mUseUnitTextView.setText(mCurrentItem.unitType);
                mResidualUnitTextView.setText(mCurrentItem.unitType);
                mRunningDays.setText(String.valueOf(mCurrentItem.runningDays));
                mResidualEditText.setText(String.valueOf(mCurrentItem.remains));
                mLastUseCnt.setText(String.valueOf(mCurrentItem.useCount));
                uri = Uri.parse(String.valueOf(mCurrentItem.img));
 //               mImageView.setImageURI(uri);

/*

                //URI를 bitmap으로 변환하여 imageView에 적용시켜주는 API-picasso
                Picasso.with(DetailActivity.this)
                        .load((String.valueOf(uri)))
                        .placeholder(R.drawable.no_image_white)
                        .resize(360, 420)
                        .centerCrop()
                        //.fit().centerInside()
                        //imageView에 사이즈 맞추기
                        //.transform(PicassoTransformations.resizeTransformation)
                        .into(mImageView);
*/

                //사용량 수정하면 마지막 사용일 변경되는 이벤트
                mLastUseCnt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //remains = Integer.parseInt(mBuyQtyEditText.getText().toString()) - Integer.parseInt(mLastUseCnt.getText().toString());
                        //mResidualEditText.setText(String.valueOf(remains));
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //remains = Integer.parseInt(mBuyQtyEditText.getText().toString()) - Integer.parseInt(mLastUseCnt.getText().toString());
                        //mResidualEditText.setText(String.valueOf(remains));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mLastUseDtEditText.setText(endDt);
                        //System.out.println("======================Integer.parseInt(mBuyQtyEditText.getText().toString()):"+Integer.parseInt(mBuyQtyEditText.getText().toString()));
                        //System.out.println("======================Integer.parseInt(mLastUseCnt.getText().toString()):"+Integer.parseInt(mLastUseCnt.getText().toString()));

                        //remains = Integer.parseInt(mBuyQtyEditText.getText().toString()) - Integer.parseInt(mLastUseCnt.getText().toString());
                        //mResidualEditText.setText(String.valueOf(remains));
                        //System.out.println("======================remains:"+remains);
                    }
                });


                mBuyDtEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog dialog = new DatePickerDialog(DetailActivity.this, dateSetListener, mDate.year, mDate.month, mDate.day);
                        dialog.show();

                    }

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String sPurchaseDate = String.format("%2d-%02d-%02d", year, month + 1, dayOfMonth);

                            mBuyDtEditText.setText(new StringBuilder(sPurchaseDate));
                            item.purchaseDate = mBuyDtEditText.getText().toString();
                        }
                    };


                });
                mCloseDtEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog dialog = new DatePickerDialog(DetailActivity.this, dateSetListener, mDate.year, mDate.month, mDate.day);
                        dialog.show();

                    }

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String sExpireDate = String.format("%2d-%02d-%02d", year, month + 1, dayOfMonth);

                            mCloseDtEditText.setText(new StringBuilder(sExpireDate));
                            item.expireDate = mCloseDtEditText.getText().toString();
                        }
                    };

                });
                mLastUseDtEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog dialog = new DatePickerDialog(DetailActivity.this, dateSetListener, mDate.year, mDate.month, mDate.day);
                        dialog.show();

                    }

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String sLastUseDate = String.format("%2d-%02d-%02d", year, month + 1, dayOfMonth);

                            mLastUseDtEditText.setText(new StringBuilder(sLastUseDate));
                            item.lastUseDate = mLastUseDtEditText.getText().toString();
                        }
                    };

                });

                mPurchaseUnitTextSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mPurchaseUnitType = (String) mPurchaseUnitTextSpinner.getItemAtPosition(position);
                        Log.d("", "mPurchaseUnitType" + mPurchaseUnitType);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<IngredientItem> items = FoodIngreInfoApplication.mUserItem.ingredientItems;
                    IngredientItem item = new IngredientItem();

                    //업데이트
                    item.ingredientName = mIngreEditText.getText().toString();
                    item.purchaseDate = mBuyDtEditText.getText().toString();
                    item.purchaseCount = Integer.parseInt(mBuyQtyEditText.getText().toString());
                    item.expireDate = mCloseDtEditText.getText().toString();
                    item.storage = mSaveSpaceEditText.getText().toString();
                    item.additionalData = mAddInputEditText.getText().toString();
                    item.lastUseDate = mLastUseDtEditText.getText().toString();
                    item.useCount = Integer.parseInt(mLastUseCnt.getText().toString());
                    item.remains = item.purchaseCount - Integer.parseInt(mLastUseCnt.getText().toString());
                    Log.d("", "item.remains         " + item.remains);
                    //spinner
                    item.unitType = mPurchaseUnitType;
                    item.residualUnitType = mResidualUnitTextView.getText().toString();
                    item.useUnitType = mUseUnitTextView.getText().toString();
                    item.ingreId = getIngreId;
                    item.img = String.valueOf(uri);

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

                    } catch (ParseException e) {
                        // 예외 처리
                    }


                    if(item.remains == 0){
                        //남은수량이 0일경우 쇼핑리스트에 담을지 여부확인
                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DetailActivity.this);
                        //"재료를 모두 소진하여 해당 항목이 삭제됩니다. \n쇼핑리스트에 추가하시겠습니까?"
                        alert_confirm.setMessage("재료를 모두 소진하였습니다. \n쇼핑리스트에 추가하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @SuppressLint("WrongConstant")
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 'YES'
                                        System.out.println("===========YES================");

                                        int j=1;

                                        Intent intent = new Intent(DetailActivity.this, ShoppingListAddActivity.class);
                                        intent.addFlags(j);
                                        intent.putExtra("index",index);
                                        startActivity(intent);

/*                                        ArrayList<IngredientItem> items = FoodIngreInfoApplication.mUserItem.ingredientItems;
                                        IngredientItem in = FoodIngreInfoApplication.mUserItem.ingredientItems.get(index);

                                        items.remove(in);
                                        FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);
*/
                                    }
                                }).setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 'No'
                                        System.out.println("===========No================");

                                        return;
                                    }
                                });

                        AlertDialog alert = alert_confirm.create();
                        alert.show();

                    }
                    else {

                        items.set(index,item);

                        //DB에 셋팅
                        FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);

                        finish();
                    }

                }


            });



        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
            // 삭제구현
            findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DetailActivity.this);
                    alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    //index값 가져오기(전역변수)
                                    IngredientItem in = FoodIngreInfoApplication.mUserItem.ingredientItems.get(index);
                                    ArrayList<IngredientItem> items = FoodIngreInfoApplication.mUserItem.ingredientItems;

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

        }

        private int convertUnitType () {
            if (TextUtils.isEmpty(mCurrentItem.unitType)) {
                return 0;
            }
            switch (mCurrentItem.unitType) {
                case "개":
                    return 0;

                case "통":
                    return 1;

                case "캔":
                    return 2;

                case "근":
                    return 3;

                case "cc":
                    return 4;

                case "g":
                    return 5;

                case "kg":
                    return 6;

                case "박스":
                    return 7;

                case "ℓ":
                    return 8;

                case "㎖":
                    return 9;

                case "포기":
                    return 10;

                case "봉지":
                    return 11;

                case "묶음":
                    return 12;

                case "망":
                    return 13;

                case "팩":
                    return 14;

                default:
                    return 0;
            }
        }
}