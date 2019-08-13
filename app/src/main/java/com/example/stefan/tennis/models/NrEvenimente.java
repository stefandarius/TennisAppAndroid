package com.example.stefan.tennis.models;


import android.util.Log;

import com.example.stefan.tennis.database.TenisDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.PropertyFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@QueryModel(database = TenisDatabase.class)
public class NrEvenimente {

    @Column
    int numar;

    @Column
    Date dataEveniment;

    public static List<NrEvenimente> getAllByMonth(String month, String year) {
        Method dateEveniment = new Method("DATE", IstoricAntrenamente_Table.data_antrenament.div(PropertyFactory.from(1000)), PropertyFactory.from("unixepoch"));
        Method strTime = strftime("%m", IstoricAntrenamente_Table.data_antrenament.div(PropertyFactory.from(1000)), "unixepoch");
        Method strYear = strftime("%Y", IstoricAntrenamente_Table.data_antrenament.div(PropertyFactory.from(1000)), "unixepoch");

        Where where = SQLite.select(IstoricAntrenamente_Table.data_antrenament.as("dataEveniment"), Method.count(IstoricAntrenamente_Table.id).as("numar")).from(IstoricAntrenamente.class)
                .where(strTime.eq(String.format("%02d", Integer.parseInt(month)))).and(strYear.eq(year)).groupBy(dateEveniment);
        Log.v("Evenimente Where", where.getQuery());
        return where.queryCustomList(NrEvenimente.class);

    }

    public static Map<CalendarDay, String> getMapByMonth(String month, String year) {
        Method dateEveniment = new Method("DATE", IstoricAntrenamente_Table.data_antrenament.div(PropertyFactory.from(1000)), PropertyFactory.from("unixepoch"));
        Method strTime = strftime("%m", IstoricAntrenamente_Table.data_antrenament.div(PropertyFactory.from(1000)), "unixepoch");
        Method strYear = strftime("%Y", IstoricAntrenamente_Table.data_antrenament.div(PropertyFactory.from(1000)), "unixepoch");

        Where where = SQLite.select(IstoricAntrenamente_Table.data_antrenament.as("dataEveniment"), Method.count(IstoricAntrenamente_Table.id).as("numar")).from(IstoricAntrenamente.class)
                .where(strTime.eq(String.format("%02d", Integer.parseInt(month)))).and(strYear.eq(year)).groupBy(dateEveniment);
        Log.v("Evenimente Where", where.getQuery());
        List<NrEvenimente> date = where.queryCustomList(NrEvenimente.class);
        Map<CalendarDay, String> map = new HashMap<>();
        for (NrEvenimente nd : date) {
            map.put(nd.getCalendarDay(), String.valueOf(nd.getNumar()));
            Log.v("Evenimente", nd.dataEveniment + " " + nd.getNumar());
        }
        return map;
    }

    public static Method strftime(String formatString, IProperty time, String... modifiers) {
        List<IProperty> propertyList = new ArrayList<>();
        propertyList.add(PropertyFactory.from(formatString));
        propertyList.add(time);
        for (String modifier : modifiers) {
            propertyList.add(PropertyFactory.from(modifier));
        }
        return new Method("strftime", propertyList.toArray(new IProperty[propertyList.size()]));
    }

    public int getNumar() {
        return numar;
    }

    public Date getDataEveniment() {
        return dataEveniment;
    }

    public CalendarDay getCalendarDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataEveniment);
        return CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }
}