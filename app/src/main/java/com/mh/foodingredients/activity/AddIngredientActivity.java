package com.mh.foodingredients.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.model.DateSetting;
import com.mh.foodingredients.model.IngredientItem;
import com.mh.foodingredients.model.ShoppingItem;
import com.mh.foodingredients.model.StorageItem;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AddIngredientActivity extends AppCompatActivity {

    private static final String TAG = "AddIngredientActivity";

    private Uri filePath;
    Uri downloadUrl;

    EditText mIngreEditText;
    EditText mBuyDtEditText;
    EditText mBuyQtyEditText;
    EditText mCloseDtEditText;
    EditText mSaveSpaceEditText;
    EditText mAddInputEditText;
    Spinner mPurchaseUnitTextSpinner;
    Spinner mStorageSpinner;
    String mPurchaseUnitType;
    String sStorageSpinner;
    String ingreId;
    DateSetting mDate = new DateSetting();
    IngredientItem item = new IngredientItem();
    StorageItem mStorageItem;
    ShoppingItem mShoppingItem;
    ArrayList<StorageItem> storageItem = new ArrayList<>();
    ArrayList<StorageItem> arrayStorageItem = FoodIngreInfoApplication.mUserItem.StorageItems;

    int index;
    String sStartDate;
    String getTime;
    Button mImgButton;
    ImageView mImgPreview;
    String imagePath;
    int flag;

    //spinner
    String[] sUnit = new String[arrayStorageItem.size()];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        //상단Title
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("재료추가") ;

        mIngreEditText = findViewById(R.id.ingredntNmEditText);
        mBuyDtEditText = findViewById(R.id.buyDtEditText);
        mBuyQtyEditText = findViewById(R.id.buyQtyEditText);
        mCloseDtEditText = findViewById(R.id.closeDtEditText);
        mSaveSpaceEditText = findViewById(R.id.saveSpaceEditText);
        mStorageSpinner = findViewById(R.id.storageSpinner);
        mAddInputEditText = findViewById(R.id.addInputEditText);
        mPurchaseUnitTextSpinner = findViewById(R.id.purchaseUnitTextSpinner);
        mImgButton = findViewById(R.id.imgButton);
        mImgPreview = findViewById(R.id.imgPreview);

        //오늘날짜 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        getTime = sdf.format(date);
        item.todaySetting = getTime;

        if (getIntent() != null) {

            index = getIntent().getIntExtra("index", 0);

            if (FoodIngreInfoApplication.mUserItem.StorageItems != null) {
                mStorageItem = FoodIngreInfoApplication.mUserItem.StorageItems.get(index);
            }
        }

        //image선택
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        //shoppingList에서 구매선택 시 넘겨진 값(flag =1)
        flag = getIntent().getFlags();

        if(flag == 1) {

            mSaveSpaceEditText.setVisibility(View.GONE);
            mStorageSpinner.setVisibility(View.VISIBLE);

            index = getIntent().getIntExtra("index", 0);
            if (FoodIngreInfoApplication.mUserItem.shoppingItems != null) {
                mShoppingItem = FoodIngreInfoApplication.mUserItem.shoppingItems.get(index);
            }
                mBuyDtEditText.setText(item.todaySetting);
                mIngreEditText.setText(mShoppingItem.ingredientName);
                mBuyQtyEditText.setText(String.valueOf(mShoppingItem.purchaseCount));
                mPurchaseUnitTextSpinner.setSelection(convertUnitType());

                //저장공간 Spinner
                for(int i=0; i<arrayStorageItem.size(); i++){
                    String sStorage = arrayStorageItem.get(i).storage;
                    sUnit[i] = sStorage;
                }
                try{

                  mStorageSpinner.setAdapter(new MyCustomAdapter(AddIngredientActivity.this, R.layout.spinner_row, sUnit));

                } catch (Exception e) { }

                mStorageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sStorageSpinner = (String) mStorageSpinner.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } else {

                mStorageSpinner.setVisibility(View.GONE);
                mSaveSpaceEditText.setVisibility(View.VISIBLE);

                // 저장공간, 오늘날짜 자동셋팅
                mSaveSpaceEditText.setText(mStorageItem.storage);

                mBuyDtEditText.setText(item.todaySetting);

                sStorageSpinner = mSaveSpaceEditText.getText().toString();

            }

            //수량단위spinner
            mPurchaseUnitTextSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPurchaseUnitType = (String) mPurchaseUnitTextSpinner.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //구입일자
        mBuyDtEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(AddIngredientActivity.this, dateSetListener, mDate.year, mDate.month, mDate.day);
                dialog.show();

            }
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String sPurchaseDate = String.format("%2d-%02d-%02d", year, month+1, dayOfMonth);
                    sStartDate = String.format("%2d%02d%02d", year, month+1, dayOfMonth);

                    mBuyDtEditText.setText(new StringBuilder(sPurchaseDate));
                    item.purchaseDate = mBuyDtEditText.getText().toString();
                }
            };
        });

        //유통기한
        mCloseDtEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(AddIngredientActivity.this, dateSetListener, mDate.year, mDate.month, mDate.day);
                dialog.show();

            }

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String sExpireDate = String.format("%2d-%02d-%02d", year, month+1, dayOfMonth);

                    mCloseDtEditText.setText(new StringBuilder(sExpireDate));
                    item.expireDate = mCloseDtEditText.getText().toString();

                }
            };

        });

        //저장
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //데이터베이스 저장
                String materialName = mIngreEditText.getText().toString();
                String purchaseDate = mBuyDtEditText.getText().toString();
                String buyCount = mBuyQtyEditText.getText().toString();
                String storage = sStorageSpinner;

                if (TextUtils.isEmpty(materialName)) {
                    Toast.makeText(AddIngredientActivity.this, getString(R.string.msg_empty_input_ingredient), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(buyCount)) {
                    Toast.makeText(AddIngredientActivity.this, getString(R.string.msg_empty_input_buyCount), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(storage)) {
                    Toast.makeText(AddIngredientActivity.this, getString(R.string.msg_empty_input_storage), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> stItem = new ArrayList<String>();
                IngredientItem item = new IngredientItem(materialName, purchaseDate, storage, ingreId);

                if (!TextUtils.isEmpty(buyCount)) {
                    item.purchaseCount = Integer.parseInt(buyCount);
                    item.unitType = mPurchaseUnitType;

                }
                item.ingreId = UUID.randomUUID().toString();
                item.expireDate = mCloseDtEditText.getText().toString();
                item.storage = sStorageSpinner;
                item.additionalData = mAddInputEditText.getText().toString();
                item.useCount = 0;
                item.remains = item.purchaseCount - item.useCount;

                if(downloadUrl != null){
                    item.img = downloadUrl.toString();
                }

                stItem.add(sStorageSpinner);
                //storage 별도 저장
                storageItem.add(item);

                // 경과일 구하기
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sEndDate = getTime;

                try { // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
                    Date dStartDate = sdf.parse(purchaseDate);
                    Date dEndDate = sdf.parse(sEndDate);

                    // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
                    // 연산결과 -950400000. long type 으로 return 된다.
                    long calDate = dEndDate.getTime() - dStartDate.getTime();

                    // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
                    // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
                    long calDateDays = calDate / ( 24*60*60*1000);

                    calDateDays = Math.abs(calDateDays);

                    item.runningDays = (calDateDays+1);

                } catch(ParseException e) { }

                if (FoodIngreInfoApplication.mUserItem.ingredientItems == null) {
                    FoodIngreInfoApplication.mUserItem.ingredientItems = new ArrayList<>();
                }

                ArrayList<IngredientItem> items = FoodIngreInfoApplication.mUserItem.ingredientItems;
                ArrayList<ShoppingItem> shoppintItems = FoodIngreInfoApplication.mUserItem.shoppingItems;
                items.add(item);

                try{

                    //저장과 함께 shoppingList에서 삭제하기
                    shoppintItems.remove(mShoppingItem);

                } catch (Exception e) { }
                FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);
            }
        });

        //취소
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {

            filePath = data.getData();

            Log.d(TAG, "uri:" + String.valueOf(filePath));

            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imagePath = String.valueOf(filePath);

                Picasso.with(AddIngredientActivity.this)
                        .load(String.valueOf(imagePath))
                        .placeholder(R.drawable.no_image_white)
                        //.resize(360, 420)
                        //.fit().centerInside()
                        //.transform(PicassoTransformations.resizeTransformation)
                        //.rotate(rotateImage(degree,imagePath))
                        .into(mImgPreview);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        uploadFile();
    }

    //파일 업로드
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            //String filename = formatter.format(now) + ".png";
            String filename = formatter.format(now) + ".jpg";

            //storage 주소와 폴더 파일명을 지정해 준다.
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://foodingredients-d9075.appspot.com").child("images/" + filename);

            storageRef.putFile(filePath)
                    //성공
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri;

                                }
                            });

                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();

                        }
                    })

                    //실패
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다.
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }

    }

    public class MyCustomAdapter extends ArrayAdapter<String> {


        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater = getLayoutInflater();
            View row=inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.storageItem);
            label.setText(sUnit[position]);

            return row;
        }
    }

    //구입수량단위 spinner
    private int convertUnitType () {
        if (TextUtils.isEmpty(mShoppingItem.unitType)) {
            return 0;
        }
        switch (mShoppingItem.unitType) {
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


