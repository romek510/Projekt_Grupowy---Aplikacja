package com.example.romanrosiak.dietapp.ListViewHolder;

/**
 * Created by roman.rosiak on 19.01.2017.
 */

public class IngredientHolder {

    public String ingredientName;
    public double ingredientQuantity;
    public String quantityType;

    public IngredientHolder() {

    }

    public IngredientHolder(String ingredientName, double ingredientQuantity, String quantityType) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.quantityType = quantityType;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }
}
