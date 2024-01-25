package com.example.clipboardmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseUtility extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "CopiedItems.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "ITEMS";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEXT = "item";
    public DatabaseUtility(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT);", TABLE_NAME, COLUMN_ID, COLUMN_TEXT);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public synchronized long addItem(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TEXT, item);
        long result = db.insert(TABLE_NAME, null, cv);
//        if(result == -1) {
//            Log.d("RESXYZ", "addItem: Failed to Add");
////            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
//        }else{
//            Log.d("RESXYZ", "addItem: Added Successfully!");
////            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
//        }
        return result;
    }

    Cursor readAllData() {
        String query = "SELECT item FROM ITEMS GROUP BY item;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }


}
