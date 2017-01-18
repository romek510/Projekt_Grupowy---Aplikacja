package com.example.romanrosiak.dietapp.ListViewHolder;

/**
 * Created by roman.rosiak on 18.01.2017.
 */

public class WeekListHolder {

        public String weekName;
        public String weekDate;

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(String weekDate) {
        this.weekDate = weekDate;
    }

    public WeekListHolder(){

        }

        public WeekListHolder(String weekName, String weekDate) {

            this.weekName = weekName;
            this.weekDate = weekDate;
        }

}
