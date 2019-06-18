package com.mh.foodingredients.model;

import android.net.Uri;

import java.util.ArrayList;

public class IngredientItem extends StorageItem {

    public String ingredientName;
    public String purchaseDate;
    public int purchaseCount;
    public String unitType;
    public String expireDate;
    public String storage;
    public String additionalData;
    public int remains;
    public long runningDays;
    public int useCount;
    public String todaySetting;
    public String runningDaysText = "일";
    public String img;
    //public Uri getUri;

    public String ingreId;
    public String lastUseDate;
    public String useUnitType;
    public String residualUnitType;
    public ArrayList<UnitItem> unitItems;

    //기본 생성자는 firebase 사용시 필수 제약사항
    public IngredientItem() {
    }

    public IngredientItem(String ingredientName, String purchaseDate, String storage, String ingreId) {
        this.ingredientName = ingredientName;
        this.purchaseDate = purchaseDate;
        this.storage = storage;
        this.ingreId = ingreId;

    }


}
