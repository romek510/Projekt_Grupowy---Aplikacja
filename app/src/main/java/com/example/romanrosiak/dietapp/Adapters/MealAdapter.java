package com.example.romanrosiak.dietapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.romanrosiak.dietapp.ListViewHolder.MealHolder;
import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;
import com.example.romanrosiak.dietapp.R;

import java.util.List;

/**
 * Klasa Adapter używana do przechowywania listy potraw.
 * @author Roman Rosiak
 */
public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {

    private List<MealHolder> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mealName, mealType, mealHour;

        public MyViewHolder(View view) {
            super(view);
            mealName = (TextView) view.findViewById(R.id.mealNameTV);
            mealType = (TextView) view.findViewById(R.id.mealTypeTV);
            mealHour = (TextView) view.findViewById(R.id.mealHourTV);
        }
    }

    public MealAdapter(List<MealHolder> mealList) {
        this.mealList = mealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_element, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MealHolder meal = mealList.get(position);
        holder.mealName.setText(meal.getMealName());
        holder.mealType.setText(meal.getMealType());
        holder.mealHour.setText(meal.getMealHour());
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}

