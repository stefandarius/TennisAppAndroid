package com.example.stefan.tennis.core;

import android.content.Context;

public class ApplicationContext {

    private static Context context;

    public static void init(Context ctx) {
        context = ctx;
    }

    public static Context get() {
        return context;
    }
}
