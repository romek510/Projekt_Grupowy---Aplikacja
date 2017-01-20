package com.example.romanrosiak.dietapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DietApp/receip.json";
        filePath = System.getenv("SECONDARY_STORAGE")+"/DietApp/receip.json";
        selectSnacks = getResources().getString(R.string.selectSnack);;

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
                        Log.d("Romek", String.valueOf(position));
                        Log.d("Romek ingredientName", weekList.get(position).getWeekName());
                        Log.d("Romek filepath: ", filePath.toString());

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

        prepareWeekListData();
        prepareDayListData();
        prepareMealListData();
        requestPermissionSet();

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

        meal = new MealHolder("10:30", "Przekaska",selectSnacks);
        mealList.add(meal);

        meal = new MealHolder("13:00", "Obiad", "Riggatoni z Brokułem");
        mealList.add(meal);

        meal = new MealHolder("15:30", "Przekaska", selectSnacks);
        mealList.add(meal);

        meal = new MealHolder("18:00", "Kolacja", "Roladki z indyka");
        mealList.add(meal);

        Log.d("Romek - Meal List: ", mealList.toString());

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




}
