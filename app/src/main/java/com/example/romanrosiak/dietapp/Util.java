package com.example.romanrosiak.dietapp;

import android.util.Log;

import com.example.romanrosiak.dietapp.ListViewHolder.IngredientHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman.rosiak on 20.01.2017.
 */

public class Util {

    public static JSONObject findMealObject(JSONArray mealsArray, String mealName){
        String name;
        JSONObject searchObject = null;


        for (int i = 0; i < mealsArray.length(); i++) {
            JSONObject currObject = null;
            try {
                currObject = mealsArray.getJSONObject(i);
                name = currObject.getString("name");

                if(name.equalsIgnoreCase(mealName))
                {
                    searchObject = currObject;
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return searchObject;
    }

    public static List<IngredientHolder> prepareIngredientList(JSONArray ingredientArray){

        Log.d("Ingredients Array", ingredientArray.toString());
        List<IngredientHolder> ingList =  new ArrayList<>();


        for (int i = 0; i < ingredientArray.length(); i++) {
            JSONObject currObject = null;
            try {
                currObject = ingredientArray.getJSONObject(i);
                Log.d("Current Ingredient", currObject.toString());
                IngredientHolder holder = new IngredientHolder();
                holder.setIngredientName(currObject.getString("ingredient"));
                holder.setIngredientQuantity(currObject.getInt("amount"));
                holder.setQuantityType(currObject.getString("unit"));
                ingList.add(holder);

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ingList;
    }
}
