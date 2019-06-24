package com.mh.foodingredients.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    int index;
    String sStartDate;
    Uri uri;
    Button mImgButton;
    String imagePath;
    private Uri filePath;
    Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        mImgButton = findViewById(R.id.imgButton);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("재료정보") ;

        if (getIntent() != null) {

            //Main에서 선택한 재료의 고유번호(ingreId) 받기
            getIngreId = getIntent().getStringExtra("saveIngreId");

            if (FoodIngreInfoApplication.mUserItem.ingredientItems != null) {

                for (int i = 0; i < FoodIngreInfoApplication.mUserItem.ingredientItems.size(); i++) {
                    item = FoodIngreInfoApplication.mUserItem.ingredientItems.get(i);

                    if (item.ingreId.equals(getIngreId)) {
                        mCurrentItem = FoodIngreInfoApplication.mUserItem.ingredientItems.get(i);
                        index = i;

                    }
                }
            }
        }

        //이미지 선택
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }
        });

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

            //URI를 bitmap으로 변환하여 imageView에 적용시켜주는 API-picasso
            Picasso.with(DetailActivity.this)
                    .load((String.valueOf(uri)))
                    .placeholder(R.drawable.no_image_white)
                    .resize(360, 420)
                    .centerCrop()
                    .into(mImageView);

            //사용량 수정하면 마지막 사용일 변경되는 이벤트
            mLastUseCnt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mLastUseDtEditText.setText(endDt);

                }
            });

            //구입일
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

            //유통기한
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

            //마지막사용일
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

            //구입수량단위 spinner
            mPurchaseUnitTextSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mPurchaseUnitType = (String) mPurchaseUnitTextSpinner.getItemAtPosition(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        //저장
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
                //spinner
                item.unitType = mPurchaseUnitType;
                item.residualUnitType = mResidualUnitTextView.getText().toString();
                item.useUnitType = mUseUnitTextView.getText().toString();
                item.ingreId = getIngreId;

                if(downloadUrl != null){
                    item.img = downloadUrl.toString();
                }else {
                    item.img = String.valueOf(uri);
                }

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

                if(item.remains == 0){
                    //남은수량이 0일경우 쇼핑리스트에 담을지 여부확인
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DetailActivity.this);
                    alert_confirm.setMessage("재료를 모두 소진하여 해당 항목이 삭제됩니다. \n쇼핑리스트에 추가하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    int j=1;

                                    Intent intent = new Intent(DetailActivity.this, ShoppingListAddActivity.class);
                                    intent.addFlags(j);
                                    intent.putExtra("index",index);
                                    startActivity(intent);

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

                }else {

                    items.set(index,item);

                    //DB에 셋팅
                    FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).setValue(FoodIngreInfoApplication.mUserItem);

                    finish();
                }

            }

        });

        //취소
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        // 삭제
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
    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();

            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePath = String.valueOf(filePath);

                Picasso.with(DetailActivity.this)
                        .load(String.valueOf(imagePath))
                        .placeholder(R.drawable.no_image_white)
                        .into(mImageView);

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

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            //String filename = formatter.format(now) + ".png";
            String filename = formatter.format(now) + ".jpg";
            //storage 주소와 폴더 파일명을 지정해 준다.
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://foodingredients-d9075.appspot.com").child("images/" + filename);
            storageRef.putFile(filePath)
                    //성공시
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

            //실패시
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
                    @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                    //dialog에 진행률을 퍼센트로 출력해 준다
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }

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