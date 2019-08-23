package com.example.stefan.tennis.fragments;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.IstoricAntrenamenteAdapter;
import com.example.stefan.tennis.decorators.NumarDecorator;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.models.NrEvenimente;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;


public class CalendarFragment extends BaseFragment {

    public static final String TAG = "CalendarFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    private IstoricAntrenamenteAdapter istoricAntrenamenteAdapter;
    private LinearLayoutManager mLayoutManager;
    private View fab;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.calendar_view_layout;
    }

    @Override
    public void initUI(View view) {

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        istoricAntrenamenteAdapter = new IstoricAntrenamenteAdapter();

        istoricAntrenamenteAdapter.updateList(IstoricAntrenamente.getAntrenamenteByDate(new Date()));
        recyclerView.setAdapter(istoricAntrenamenteAdapter);

        // TODO: 8/23/2019 set first day of week 

        //calendarView.state().edit().setFirstDayOfWeek(DayOfWeek.of(Calendar.MONDAY)).commit();
        calendarView.addDecorator(new TodayDecorator());

        decoreazaLuna(CalendarDay.today());

        calendarView.setOnMonthChangedListener((widget, date) -> {
            decoreazaLuna(date);
        });

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                Date dataCurenta = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay()).getTime();
                List<IstoricAntrenamente> lista = IstoricAntrenamente.getAntrenamenteByDate(dataCurenta);
                istoricAntrenamenteAdapter.updateList(lista);
                istoricAntrenamenteAdapter.notifyDataSetChanged();
            }
        });
    }

    private void decoreazaLuna(CalendarDay date) {
        List<NrEvenimente> dates = NrEvenimente.getAllByMonth(String.valueOf(date.getMonth()), String.valueOf(date.getYear()));
        for (NrEvenimente nr : dates) {
            calendarView.addDecorator(new NumarDecorator(nr));
        }
        calendarView.invalidateDecorators();
    }

    @Override
    public void setupTollbar() {
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        toolbar.setTitle("Calendar");
    }

    private class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator() {
            today = CalendarDay.today();
            backgroundDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.today_circle_background);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(backgroundDrawable);
        }
    }
}
