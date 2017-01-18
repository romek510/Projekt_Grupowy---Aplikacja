package com.example.romanrosiak.dietapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;
import com.example.romanrosiak.dietapp.R;

import java.util.List;

/**
 * Created by roman.rosiak on 18.01.2017.
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyViewHolder> {

    private List<WeekListHolder> weeksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView weekName, weekDate;

        public MyViewHolder(View view) {
            super(view);
            weekName = (TextView) view.findViewById(R.id.weekName);
            weekDate = (TextView) view.findViewById(R.id.weekDate);
        }
    }


    public WeekAdapter(List<WeekListHolder> weekList) {
        this.weeksList = weekList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_week_list_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WeekListHolder week = weeksList.get(position);
        holder.weekName.setText(week.getWeekName());
        holder.weekDate.setText(week.getWeekDate());

    }

    @Override
    public int getItemCount() {
        return weeksList.size();
    }
}