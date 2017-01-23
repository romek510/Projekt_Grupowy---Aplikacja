package com.example.romanrosiak.dietapp.ListViewHolder;

/**
 * Klasa przechowywujaca dni tygodnia.
 * @author Roman Rosiak
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
