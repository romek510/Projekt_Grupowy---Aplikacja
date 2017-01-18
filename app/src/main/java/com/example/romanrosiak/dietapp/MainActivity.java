package com.example.romanrosiak.dietapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.example.romanrosiak.dietapp.Adapters.MealAdapter;
import com.example.romanrosiak.dietapp.Adapters.WeekAdapter;
import com.example.romanrosiak.dietapp.ListViewHolder.MealHolder;
import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<WeekListHolder> weekList = new ArrayList<>();
    private RecyclerView weekRV;
    private WeekAdapter weekAdapter;

    private List<WeekListHolder> dayList = new ArrayList<>();
    private RecyclerView dayRV;
    private WeekAdapter dayAdapter;

    private List<MealHolder> mealList = new ArrayList<>();
    private RecyclerView mealRV;
    private MealAdapter mealAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weekRV = (RecyclerView) findViewById(R.id.weekRV);
        dayRV = (RecyclerView) findViewById(R.id.dayRV);
        mealRV = (RecyclerView) findViewById(R.id.dietRV);

        weekAdapter = new WeekAdapter(weekList);
        dayAdapter = new WeekAdapter(dayList);
        mealAdapter = new MealAdapter(mealList);

        LinearLayoutManager horizontalweekRVlayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontaldayRVlayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager verticalMealRVlayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        weekRV.setLayoutManager(horizontalweekRVlayout);
        weekRV.setItemAnimator(new DefaultItemAnimator());
        weekRV.setAdapter(weekAdapter);

        dayRV.setLayoutManager(horizontaldayRVlayout);
        dayRV.setItemAnimator(new DefaultItemAnimator());
        dayRV.setAdapter(dayAdapter);

        mealRV.setLayoutManager(verticalMealRVlayout);
        mealRV.setItemAnimator(new DefaultItemAnimator());
        mealRV.setAdapter(mealAdapter);

        prepareWeekListData();
        prepareDayListData();
        prepareMealListData();
    }

    private void prepareWeekListData() {
        WeekListHolder week = new WeekListHolder("Tydzień 1", "18.01-23.01");
        weekList.add(week);

        week = new WeekListHolder("Tydzień 2", "24.01-27.01");
        weekList.add(week);

        week = new WeekListHolder("Tydzień 3", "28.01-03.02");
        weekList.add(week);

        week = new WeekListHolder("Tydzień 4", "04.02-10.02");
        weekList.add(week);

        week = new WeekListHolder("Tydzień 5", "11.02-23.02");
        weekList.add(week);

        weekAdapter.notifyDataSetChanged();
    }

    private void prepareDayListData() {
        WeekListHolder day = new WeekListHolder("Poniedziałek", "01.01");
        dayList.add(day);

        day = new WeekListHolder("Wtorek", "02.01");
        dayList.add(day);

        day = new WeekListHolder("Środa", "03.01");
        dayList.add(day);

        day = new WeekListHolder("Czwartek", "04.01");
        dayList.add(day);

        day = new WeekListHolder("Piątek", "05.01");
        dayList.add(day);

        day = new WeekListHolder("Sobota", "06.01");
        dayList.add(day);

        day = new WeekListHolder("Niedziela", "07.01");
        dayList.add(day);

        dayAdapter.notifyDataSetChanged();
    }


    private void prepareMealListData() {
        MealHolder meal = new MealHolder("8:00", "Sniadanie", "Twarozek z kiełkami");
        mealList.add(meal);

        meal = new MealHolder("10:30", "Przekaska", "Przekaska 1");
        mealList.add(meal);

        meal = new MealHolder("13:00", "Obiad", "Riggatoni z Brokułem");
        mealList.add(meal);

        meal = new MealHolder("15:30", "Przekaska", "Przekaska 2");
        mealList.add(meal);

        meal = new MealHolder("18:00", "Kolacja", "Roladki z indyka");
        mealList.add(meal);

        Log.d("Romek - Meal List: ", mealList.toString());

        mealAdapter.notifyDataSetChanged();
        Log.d("Romek - Meal Adapter: ", mealAdapter.toString());
    }
}
