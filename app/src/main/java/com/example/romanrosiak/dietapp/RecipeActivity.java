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
import android.widget.TextView;

import com.example.romanrosiak.dietapp.Adapters.IngredientsAdapter;
import com.example.romanrosiak.dietapp.Adapters.MealAdapter;
import com.example.romanrosiak.dietapp.Adapters.WeekAdapter;
import com.example.romanrosiak.dietapp.ListViewHolder.IngredientHolder;
import com.example.romanrosiak.dietapp.ListViewHolder.RecyclerItemClickListener;
import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mealNameTV = (TextView) findViewById(R.id.mealNameTV_recipe);

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

        prepareIngredientListData();
        Log.d("Romek JSON file:", jsonFile);

    }

    private void prepareIngredientListData() {
        IngredientHolder ingredient = new IngredientHolder("makaron razowy", 70, "g");
        ingredientList.add(ingredient);

        ingredient = new IngredientHolder("brokuly", 150, "g");
        ingredientList.add(ingredient);

        ingredient = new IngredientHolder("piers z indyka", 100, "g");
        ingredientList.add(ingredient);

        ingredient = new IngredientHolder("czosnek", 1, "sztuka");
        ingredientList.add(ingredient);

        ingredient = new IngredientHolder("parmezan", 2, "łyżki");
        ingredientList.add(ingredient);

        ingredient = new IngredientHolder("masło", 0.5, "łyzki");
        ingredientList.add(ingredient);

        ingredient = new IngredientHolder("ser kanapkowy", 1, "łyżka");
        ingredientList.add(ingredient);

        ingredient = new IngredientHolder("natka pietruszki", 2, "łyżki");
        ingredientList.add(ingredient);


        ingredientAdapter.notifyDataSetChanged();
    }
}
