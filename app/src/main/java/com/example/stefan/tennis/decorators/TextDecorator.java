package com.example.stefan.tennis.decorators;

import android.util.Log;

import com.example.stefan.tennis.spans.TextSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Map;

public class TextDecorator implements DayViewDecorator {

    private Map<CalendarDay, String> dates;
    private String text = "";

    public TextDecorator(Map<CalendarDay, String> map) {
        this.dates = map;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (dates.containsKey(day)) {
            text = dates.get(day);
            Log.v("evenimente", "here " + day.getDate().toString() + " " + text);
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new TextSpan(text));
    }


}
