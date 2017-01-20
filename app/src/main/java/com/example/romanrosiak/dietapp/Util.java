package com.example.romanrosiak.dietapp;

import android.util.Log;

import com.example.romanrosiak.dietapp.ListViewHolder.IngredientHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public static String returnStringFromFile(String filePath){

        //Read text from file
        StringBuilder text = new StringBuilder();
        String result = null;

        File file = new File(filePath);
        if(file.exists())   // check if file exist
        {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append("\n");
                }

                result = text.toString();
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
                Log.d("Romek file:", "File error" );
            }
        }
        else
        {
            Log.d("Romek file:", "File not found" );
        }

        return result;
    }
}
