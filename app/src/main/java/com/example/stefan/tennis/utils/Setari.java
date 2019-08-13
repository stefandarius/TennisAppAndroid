package com.example.stefan.tennis.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Setari {

    public static final String JOB_ID = "jobId";
    public static final String ORA = "ora";
    public static final String MINUT = "minut";
    private static Setari singleInstance;
    private final SharedPreferences preferences;
    private int jobId;
    private int ora;
    private int minut;

    private Setari(Context context) {
        preferences = context.getSharedPreferences("appSettings", Context.MODE_PRIVATE);
        jobId = preferences.getInt(JOB_ID, 0);
        ora = preferences.getInt("ora", 19);
        minut = preferences.getInt("minut", 0);
    }

    public static Setari getInstance(Context context) {
        if (singleInstance == null) {
            return new Setari(context);
        }
        return singleInstance;
    }

    public int getMinut() {
        return minut;
    }

    public void setMinut(int minut) {
        this.minut = minut;
        preferences.edit().putInt(MINUT, minut).commit();
    }

    public int getOra() {
        return ora;
    }

    public void setOra(int ora) {
        this.ora = ora;
        preferences.edit().putInt(ORA, ora).commit();
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
        preferences.edit().putInt(JOB_ID, jobId).commit();
    }
}
