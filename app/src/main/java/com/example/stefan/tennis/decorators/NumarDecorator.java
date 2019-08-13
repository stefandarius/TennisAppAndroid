package com.example.stefan.tennis.decorators;

import com.example.stefan.tennis.models.NrEvenimente;
import com.example.stefan.tennis.spans.TextSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class NumarDecorator implements DayViewDecorator {

    private NrEvenimente nrEvenimente;
    private String text;

    public NumarDecorator(NrEvenimente nrEvenimente) {
        this.nrEvenimente = nrEvenimente;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return nrEvenimente.getCalendarDay().equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new TextSpan(String.valueOf(nrEvenimente.getNumar())));
    }


}
