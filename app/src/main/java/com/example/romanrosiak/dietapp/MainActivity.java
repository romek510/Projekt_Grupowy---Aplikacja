package com.example.romanrosiak.dietapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    public String selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DietApp/receip.json";
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

                        Log.d("WeekKey", weekKey);
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

    public void createSnackDialog(View view, final int snackPosition){
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.snack_dialog);
        dialog.setTitle("Test");

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

    private void prepareDayListData(String weekName) {

        dayList.clear();
        HashMap<String, List<MealHolder>> dayMap = dietList.get(weekName);
        List<String> days = new ArrayList<>();
        for(String key : dayMap.keySet()){
           days.add(key);
        }
        DayNameComparator myComparator = new DayNameComparator();
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
//
//        MealHolder meal = new MealHolder("8:00", "Sniadanie", "Twarozek z kiełkami");
//        mealList.add(meal);
//
//        meal = new MealHolder("10:30", "Przekaska",selectSnacks);
//        mealList.add(meal);
//
//        meal = new MealHolder("13:00", "Obiad", "Riggatoni z Brokułem");
//        mealList.add(meal);
//
//        meal = new MealHolder("15:30", "Przekaska", selectSnacks);
//        mealList.add(meal);
//
//        meal = new MealHolder("18:00", "Kolacja", "Roladki z indyka");
//        mealList.add(meal);
//
//        Log.d("Romek - Meal List: ", mealList.toString());

        mealAdapter.notifyDataSetChanged();
        Log.d("Romek - Meal Adapter: ", mealAdapter.toString());
    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(System.getenv("SECONDARY_STORAGE"));
        Log.d("Romek files: ", fl.toString());
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d("Romek", "External Storage is writable");
            return true;
        }
        Log.d("Romek", "External Storage is not writable");
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("Romek", "External Storage is readable");
            return true;
        }
        Log.d("Romek", "External Storage is not readable");
        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public void requestPermissionSet(){
        MainActivity.super.requestAppPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
    }

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
                        mealHld.setMealType(meal.getString("mealType"));
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

    public final class DayNameComparator implements Comparator<String>
    {
        private String[] items ={
                "Poniedziałek",
                "Wtorek",
                "Środa",
                "Czwartek",
                "Piątek",
                "Sobota",
                "Niedziela"
        };
        @Override
        public int compare(String a, String b)
        {
            int ai = items.length, bi=items.length;
            for(int i = 0; i<items.length; i++)
            {
                if(items[i].equalsIgnoreCase(a))
                    ai=i;
                if(items[i].equalsIgnoreCase(b))
                    bi=i;
            }
            return ai-bi;
        }
    }





}
