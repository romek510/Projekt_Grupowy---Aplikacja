package com.example.romanrosiak.dietapp.ListViewHolder;

/**
 * Obiekt przechowuje informacje o posiłach.
 * @author Roman Rosiak
 */

public class MealHolder {

    public String mealHour;
    public String mealType;
    public String mealName;

    public MealHolder() {

    }

    public MealHolder(String mealHour, String mealType, String mealName) {
        this.mealHour = mealHour;
        this.mealType = mealType;
        this.mealName = mealName;
    }

    public String getMealHour() {
        return mealHour;
    }

    public void setMealHour(String mealHour) {
        this.mealHour = mealHour;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
}
