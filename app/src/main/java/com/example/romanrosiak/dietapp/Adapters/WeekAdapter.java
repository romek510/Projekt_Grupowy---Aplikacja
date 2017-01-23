package com.example.romanrosiak.dietapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;
import com.example.romanrosiak.dietapp.R;

import java.util.List;

/**
 * Klasa Adapter używana do przechowywania listy dni oraz tygodni.
 * @author Roman Rosiak
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyViewHolder> {

    private List<WeekListHolder> weeksList;
    public View oldView;
    public String addapterType;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView weekName, weekDate;

        public int oldPos;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            weekName = (TextView) view.findViewById(R.id.weekName);
            weekDate = (TextView) view.findViewById(R.id.weekDate);
        }

        @Override
        public void onClick(View view) {
//            if (selectedItems.get(getAdapterPosition(), false)) {
//                selectedItems.delete(getAdapterPosition());
//                view.setSelected(false);
//            }
//            else {
                if(oldView != null){
                    selectedItems.delete(getOldPosition());
                    oldView.setSelected(false);
                }
                selectedItems.put(getAdapterPosition(), true);
                view.setSelected(true);
                oldView = view;

//            }
        }
    }


    public WeekAdapter(List<WeekListHolder> weekList, String addapterType) {
        this.weeksList = weekList;
        this.addapterType = addapterType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(addapterType.equalsIgnoreCase("day")){
           itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.select_day_lv, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.select_week_list_view, parent, false);
        }

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