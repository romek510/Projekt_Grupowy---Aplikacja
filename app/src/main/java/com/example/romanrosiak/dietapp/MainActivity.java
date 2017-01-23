package com.example.romanrosiak.dietapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.romanrosiak.dietapp.Adapters.MealAdapter;
import com.example.romanrosiak.dietapp.Adapters.WeekAdapter;
import com.example.romanrosiak.dietapp.ListViewHolder.MealHolder;
import com.example.romanrosiak.dietapp.ListViewHolder.RecyclerItemClickListener;
import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Głowne activity, które rozpoczyna działanie całej aplikacji.
 * @author Roman Rosiak
 */
public class MainActivity extends RuntimePermissionsActivity {

    private List<WeekListHolder> weekList = new ArrayList<>();
    private RecyclerView weekRV;
    private WeekAdapter weekAdapter;

    private List<WeekListHolder> dayList = new ArrayList<>();
    private RecyclerView dayRV;
    private WeekAdapter dayAdapter;

    private List<MealHolder> mealList = new ArrayList<>();
    private RecyclerView mealRV;
    private MealAdapter mealAdapter;

    private TextView pageTabTV;

    public String filePath;
    private static final int REQUEST_PERMISSIONS = 20;

    private String selectSnacks;
    public HashMap<String, HashMap<String, List<MealHolder>>> dietList;
    public HashMap<String, List<String>> weekRageMap;
    public String selectedWeek = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePath = System.getenv("SECONDARY_STORAGE")+"/DietApp/receip.json";
        selectSnacks = getResources().getString(R.string.selectSnack);
        dietList = new HashMap<>();
        weekRageMap = new HashMap<>();


        pageTabTV = (TextView) findViewById(R.id.dietPageTabTV);
        pageTabTV.setBackgroundResource(R.drawable.page_selector);


        weekRV = (RecyclerView) findViewById(R.id.weekRV);
        dayRV = (RecyclerView) findViewById(R.id.dayRV);
        mealRV = (RecyclerView) findViewById(R.id.dietRV);

        weekAdapter = new WeekAdapter(weekList, "week");
        dayAdapter = new WeekAdapter(dayList, "day");
        mealAdapter = new MealAdapter(mealList);

        LinearLayoutManager horizontalweekRVlayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontaldayRVlayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager verticalMealRVlayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        weekRV.setLayoutManager(horizontalweekRVlayout);
        weekRV.setItemAnimator(new DefaultItemAnimator());
        weekRV.setAdapter(weekAdapter);

        weekRV.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), weekRV, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        String weekKey = weekList.get(position).getWeekName().substring(weekList.get(position).getWeekName().length()-1,weekList.get(position).getWeekName().length());
                        prepareDayListData(weekKey);
                        selectedWeek = weekKey;

                    }
                    @Override
                    public void onItemLongClick(View view, int position){

                    }
                })
        );

        dayRV.setLayoutManager(horizontaldayRVlayout);
        dayRV.setItemAnimator(new DefaultItemAnimator());
        dayRV.setAdapter(dayAdapter);

        dayRV.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), dayRV, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Log.d("Romek", String.valueOf(position));
                        Log.d("Romek dayName", dayList.get(position).getWeekName());
                        prepareMealListData(selectedWeek, dayList.get(position).getWeekName());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );

        mealRV.setLayoutManager(verticalMealRVlayout);
        mealRV.setItemAnimator(new DefaultItemAnimator());
        mealRV.setAdapter(mealAdapter);

        mealRV.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mealRV, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Log.d("Romek", String.valueOf(position));
                        Log.d("Romek mealName", mealList.get(position).getMealName());
                        if(mealList.get(position).getMealName().equalsIgnoreCase(selectSnacks)){

                            createSnackDialog(view, position);

                        }else{
                            Intent intentBundle = new Intent(getApplicationContext(), RecipeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("mealName", mealList.get(position).getMealName());

                            intentBundle.putExtras(bundle);
                            startActivity(intentBundle);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        if(position == 1 || position == 3) {

                            createSnackDialog(view, position);
                        }
                    }
                })
        );
        requestPermissionSet();
        preapareDiet();

        prepareWeekListData();
        prepareDayListData("1");
        prepareMealListData("1", "Poniedziałek");
    }


    /**
     * Metoda generuje i wyświetla popup w którym można wybrać przekąski
     * @param  view  Referencja do widoku w którym ma być wyświetlony popup.
     * @param  snackPosition  Referencja do klikniętej pozycji.
     */
    public void createSnackDialog(View view, final int snackPosition){
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.snack_dialog);
        dialog.setTitle(selectSnacks);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/snacks.txt";
        String jsonString = Util.returnStringFromFile(filePath);
        Log.d("Snacks", jsonString);

        JSONObject json;
        JSONArray jsonArray = null;

        List<String> items = new ArrayList<>();
        try {
            json = new JSONObject(jsonString);
            jsonArray = json.getJSONArray("snacks");
            items.addAll(Util.prepareSnackList(jsonArray));
            Log.d("ListItmes", items.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        ListView listView = (ListView) dialog.findViewById(R.id.snacksListView);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked element", (String)parent.getItemAtPosition(position));
                mealList.get(snackPosition).setMealName((String)parent.getItemAtPosition(position));
                mealAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * @author Roman Rosiak
     * @description
     * @date 23.01.2017
     */

    /**
     * Metoda generuje tygodnie i wyświetla je w aplikacji.
     */
    private void prepareWeekListData() {

        for (String key : dietList.keySet() ) {
            String weekName = "Tydzień " + key;
            List <String> weekDateList = weekRageMap.get(key);
            String weekRange = weekDateList.get(0).substring(0,5)+ " - " + weekDateList.get(1).substring(0,5);
            WeekListHolder week = new WeekListHolder(weekName, weekRange);
            weekList.add(week);
        }
        weekAdapter.notifyDataSetChanged();
    }


    /**
     * Metoda generuje dni tygodnia i wyświetla w aplikacji
     * @param  weekName  Numer wybranego tygodnia.
     */
    private void prepareDayListData(String weekName) {

        dayList.clear();
        HashMap<String, List<MealHolder>> dayMap = dietList.get(weekName);
        List<String> days = new ArrayList<>();
        for(String key : dayMap.keySet()){
           days.add(key);
        }
        Util.DayNameComparator myComparator = new Util.DayNameComparator();
        Collections.sort(days, myComparator);

        String dtStart = weekRageMap.get(weekName).get(0);


        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date=new Date();
        try {
            date = (Date) formatter.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd.MM");

        int k = 0;
        for(String day : days){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, k);
            WeekListHolder d = new WeekListHolder(day,formatter2.format(c.getTime()));
            dayList.add(d);
            k++;
        }

        dayAdapter.notifyDataSetChanged();
        Log.d("WeekDay", "***UPDATE");
    }


    /**
     * @author Roman Rosiak
     * @description Metoda wyświetla akutalne posiłki dla danego dnia
     * @date 23.01.2017
     */
    /**
     * Metoda generuje i wyświetla posiłki dla danego dnia
     * @param  weekName  Numer wybranego tygodnia.
     * @param  dayName  Nazwa wybranego dnia.
     */
    private void prepareMealListData(String weekName, String dayName) {

        mealList.clear();
        mealList.addAll(dietList.get(weekName).get(dayName));
        Collections.sort(mealList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                MealHolder p1 = (MealHolder) o1;
                MealHolder p2 = (MealHolder) o2;
                return p1.getMealHour().compareToIgnoreCase(p2.getMealHour());
            }
        });
        Log.d("Romek - Meal List: ", mealList.toString());

        mealAdapter.notifyDataSetChanged();
        Log.d("Romek - Meal Adapter: ", mealAdapter.toString());
    }


    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }


    /**
     * Metoda sprawdza czy posiadamy permission set do odczytu danych z karty SD i jeśli nie to nadaje takie pozwolenie.
     */
    public void requestPermissionSet(){
        MainActivity.super.requestAppPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
    }


    /**
     * Metoda generuje HashMape w ktorej przechowywane są informacje na temat diety w poszczególne dni.
     */
    public void preapareDiet(){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/customer_diet.txt";
        String jsonString = Util.returnStringFromFile(filePath);

        JSONObject json;
        JSONArray weekArray = null;
        try {
            json = new JSONObject(jsonString);
            weekArray = json.getJSONArray("diets");
            for(int i=0; i<weekArray.length(); i++){

                HashMap<String, List<MealHolder>> mealsMap = new HashMap<String, List<MealHolder>>();

                JSONObject weekObj = weekArray.getJSONObject(i);
                Log.d("JSONobj", weekObj.toString());
                JSONArray daysArray = weekObj.getJSONArray("days");

                for(int j=0; j<daysArray.length(); j++){

                    List<MealHolder> mealList = new ArrayList<>();

                    JSONObject mealObj = daysArray.getJSONObject(j);
                    JSONArray mealArray = mealObj.getJSONArray("meals");
                    for(int k=0; k<mealArray.length(); k++){

                        JSONObject meal = mealArray.getJSONObject(k);

                        MealHolder mealHld =  new MealHolder();
                        if(meal.getString("mealName").isEmpty()){
                            mealHld.setMealName(selectSnacks);
                        }else{
                            mealHld.setMealName(meal.getString("mealName"));
                        }

                        mealHld.setMealHour(meal.getString("mealTime"));
                        if(meal.getString("mealType").equalsIgnoreCase("snack")){
                            mealHld.setMealType("Przekąska");
                        }else{
                            mealHld.setMealType(meal.getString("mealType"));
                        }

                        mealList.add(mealHld);
                        Log.d("DayMeals List", mealList.toString());
                    }

                    mealsMap.put(mealObj.getString("dayName"), mealList);
                    Log.d("DayMeals Map", mealsMap.toString());

                }
                List<String> weekRangeList = new ArrayList<>();
                weekRangeList.add(weekObj.getString("weekDateStart"));
                weekRangeList.add(weekObj.getString("weekDateEnd"));

                weekRageMap.put(weekObj.getString("weekNumber"), weekRangeList);
                dietList.put(weekObj.getString("weekNumber"), mealsMap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("WEEK List", dietList.toString());

    }


}
