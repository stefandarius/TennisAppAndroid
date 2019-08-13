package com.example.stefan.tennis.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TenisDatabase.NAME, version = TenisDatabase.VERSION)
public class TenisDatabase {

    public static final String NAME = "tenis";

    public static final int VERSION = 3;
}