package com.example.romanrosiak.dietapp;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.romanrosiak.dietapp.Adapters.IngredientsAdapter;
import com.example.romanrosiak.dietapp.Adapters.MealAdapter;
import com.example.romanrosiak.dietapp.Adapters.WeekAdapter;
import com.example.romanrosiak.dietapp.ListViewHolder.IngredientHolder;
import com.example.romanrosiak.dietapp.ListViewHolder.RecyclerItemClickListener;
import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;

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
 * Activity odpowiadające za wyświetlanie skłądników i przepisu dla konkretnej potrawy.
 * @author Roman Rosiak
 */
public class RecipeActivity extends AppCompatActivity{

    public TextView mealNameTV;
    public String mealNameBundle;
    private List<IngredientHolder> ingredientList = new ArrayList<>();
    private RecyclerView ingredientsRV;
    private IngredientsAdapter ingredientAdapter;

    private ScrollView recipe_sv;
    private TextView recipe_not_found;
    private TextView preparationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mealNameTV = (TextView) findViewById(R.id.mealNameTV_recipe);
        recipe_sv = (ScrollView) findViewById(R.id.recipe_scrollView);
        recipe_not_found = (TextView) findViewById(R.id.recipe_not_foundTV);
        preparationTV = (TextView) findViewById(R.id.preparationTV);

        Intent activityIntent = getIntent();
        Bundle activityBundle = activityIntent.getExtras();
        if(!activityBundle.isEmpty()){
            mealNameBundle = activityBundle.getString("mealName");
            mealNameTV.setText(mealNameBundle);
        }

        ingredientsRV = (RecyclerView) findViewById(R.id.ingredientsRV);
        ingredientAdapter = new IngredientsAdapter(ingredientList);

        LinearLayoutManager horizontalIngredientRVlayout = new LinearLayoutManager(RecipeActivity.this, LinearLayoutManager.VERTICAL, false);

        ingredientsRV.setLayoutManager(horizontalIngredientRVlayout);
        ingredientsRV.setItemAnimator(new DefaultItemAnimator());
        ingredientsRV.setAdapter(ingredientAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        ingredientsRV.addItemDecoration(itemDecoration);

        String extStorage = System.getenv("EXTERNAL_SDCARD_STORAGE");
        String extStorage2 = System.getenv("SECONDARY_STORAGE");
        if(extStorage != null){
            Log.d("Storage1", extStorage);
        }
        if(extStorage2 != null){
            Log.d("Storage2", extStorage2);
        }


        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/receip.txt";
        String jsonString = Util.returnStringFromFile(filePath);

        JSONObject json;
        JSONArray jsonArray = null;
        try {
            json = new JSONObject(jsonString);
            jsonArray = json.getJSONArray("mainMealsRecipes");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject meal = Util.findMealObject(jsonArray, mealNameBundle);
        if(meal != null){
            Log.d("Romek selected meal:", meal.toString());
            try {
                ingredientList.addAll(Util.prepareIngredientList(meal.getJSONArray("ingredients")));
                ingredientAdapter.notifyDataSetChanged();
                preparationTV.setText(meal.getString("preparationInstructions"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            recipe_not_found.setVisibility(View.VISIBLE);
            recipe_sv.setVisibility(View.GONE);
            Log.d("Romek selected meal:", "meal not found");
        }
    }
}
