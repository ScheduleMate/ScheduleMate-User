package com.schedulemate.schedulemate_user.ui.calendar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import androidx.lifecycle.LiveData;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarDecorator {
    static class Saturday implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            return weekday == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    static class Sunday implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            return weekday == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    static class Today implements DayViewDecorator{
        public CalendarDay date;

        public Today(){
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
        }
    }

    static class Schedule implements DayViewDecorator{
        private HashMap<String, ArrayList> hashMap;
        private int month;

        public Schedule(HashMap<String, ArrayList> hashMap, int month){
            this.hashMap = hashMap;
            this.month = month;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return hashMap.containsKey(String.valueOf(day.getDay())) && day.getMonth() == month;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, Color.GREEN));
        }
    }
}
