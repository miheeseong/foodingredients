package com.mh.foodingredients.model;

public class ShoppingItem {

    public String ingredientName;
    public int purchaseCount;
    public String unitType;

    public ShoppingItem() {

    }

    public ShoppingItem(String ingredientName, int purchaseCount, String unitType) {
        this.ingredientName = ingredientName;
        this.purchaseCount = purchaseCount;
        this.unitType = unitType;
    }
}
