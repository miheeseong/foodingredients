package com.mh.foodingredients;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.mh.foodingredients.model.DateSetting;
import com.mh.foodingredients.model.IngredientItem;
import com.mh.foodingredients.model.ShoppingItem;
import com.mh.foodingredients.model.StorageItem;
import com.mh.foodingredients.model.UserItem;

public class FoodIngreInfoApplication extends Application {

    public static FirebaseAuth mAuth;
    public static DatabaseReference mDatabase;
    public static UserItem mUserItem;
    public static StorageItem mStorageItem;
    public static DateSetting mDateSetting;
    public static IngredientItem mIngredientItem;
    public static FirebaseStorage mStorage;
    public static ShoppingItem mShoppingItem;


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //시작지점을 얻는다
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserItem = new UserItem();
        mDateSetting = new DateSetting();
        mStorageItem = new StorageItem();
        mIngredientItem = new IngredientItem();
        mStorage = FirebaseStorage.getInstance();
        mShoppingItem = new ShoppingItem();
    }
}