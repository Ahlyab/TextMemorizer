package com.example.cbmtesting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM = "item";

    // SQL statement to create the items table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ITEM + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}
