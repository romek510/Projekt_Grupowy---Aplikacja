package com.example.romanrosiak.dietapp;

import android.content.Intent;
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

import com.example.romanrosiak.dietapp.Adapters.IngredientsAdapter;
import com.example.romanrosiak.dietapp.Adapters.MealAdapter;
import com.example.romanrosiak.dietapp.Adapters.WeekAdapter;
import com.example.romanrosiak.dietapp.ListViewHolder.IngredientHolder;
import com.example.romanrosiak.dietapp.ListViewHolder.RecyclerItemClickListener;
import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    public TextView mealNameTV;
    public String mealNameBundle;

    public static String jsonFile = "{\n" +
            "\t\"mainMealsRecipes\": [\n" +
            "\t\t{\n" +
            "\t\t  \"name\": \"Twarożek z Kiełkami\",\n" +
            "\t\t  \"calories\": \"300\",\n" +
            "\t\t  \"ingredients\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t  \"amount\": \"200\",\n" +
            "\t\t\t  \"ingredient\": \"Twarożek\",\n" +
            "\t\t\t  \"unit\": \"g\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t  \"amount\": \"10\",\n" +
            "\t\t\t  \"ingredient\": \"Kiełki\",\n" +
            "\t\t\t  \"unit\": \"g\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t  \"amount\": \"2\",\n" +
            "\t\t\t  \"ingredient\": \"Chleb\",\n" +
            "\t\t\t  \"unit\": \"kromki\"\n" +
            "\t\t\t}\n" +
            "\t\t  ],\n" +
            "\t\t  \"preparationInstructions\": \"Dodać kiełki do twarożku i dokładnie wymieszać. Zjeść z chlebem\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t  \"name\": \"Riggatoni z brokułem\",\n" +
            "\t\t  \"calories\": \"300\",\n" +
            "\t\t  \"ingredients\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t  \"amount\": \"70\",\n" +
            "\t\t\t  \"ingredient\": \"Makaron Razowy\",\n" +
            "\t\t\t  \"unit\": \"g\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t  \"amount\": \"150\",\n" +
            "\t\t\t  \"ingredient\": \"brokuł\",\n" +
            "\t\t\t  \"unit\": \"g\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t  \"amount\": \"100\",\n" +
            "\t\t\t  \"ingredient\": \"pierś z indyka\",\n" +
            "\t\t\t  \"unit\": \"g\"\n" +
            "\t\t\t}\n" +
            "\t\t  ],\n" +
            "\t\t  \"preparationInstructions\": \"Wstawić wodę na makaron na ogień oraz drugi garnek z wodą do zblanszowania brokułów. Obrać czosnek. Pokroić go w plasterki, badź zmiażdzyć, włożyć na patelenie, dodać masło lub oliwę. Delikatnie podsmażyć na małym ogniu, a w miedzyczasie oczyścic mieso i pokroić na małe kawałki, a następnie doprawić solą i pieprzem. Wrzucić na patalnię i podsmażyć razem z czosnkiem. Następnie odciąć różyczki od głąba brokuła i przekroić na mniejsze fragmenty. Gdy woda zaczyna wrzeć wsypać do jednego garnka makaron, a do drugiego brokuły.....\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t  ]\n" +
            "}";

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

        JSONObject json;
        JSONArray jsonArray = null;
        try {
            json = new JSONObject(jsonFile);
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
