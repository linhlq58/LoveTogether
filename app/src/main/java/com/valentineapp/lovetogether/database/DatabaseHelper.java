package com.valentineapp.lovetogether.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.valentineapp.lovetogether.models.Memory;

import java.util.ArrayList;

/**
 * Created by lequy on 2/6/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LoveDB";

    private static final String TABLE_MEMORY = "memory";
    private static final String KEY_MEMORY_ID = "memory_id";
    private static final String KEY_MEMORY_TIME = "memory_time";
    private static final String KEY_MEMORY_NAME = "memory_name";
    private static final String KEY_MEMORY_DAYS = "memory_days";
    private static final String KEY_MEMORY_IMAGE = "memory_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEMORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMORY + " ("
                + KEY_MEMORY_ID + " INTEGER PRIMARY KEY, "
                + KEY_MEMORY_TIME + " TEXT, "
                + KEY_MEMORY_NAME + " TEXT, "
                + KEY_MEMORY_DAYS + " BIGINT, "
                + KEY_MEMORY_IMAGE + " TEXT)";

        db.execSQL(CREATE_MEMORY_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORY);

        this.onCreate(db);
    }

    public void addMemory(Memory memory) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_MEMORY_TIME, memory.getTime());
        values.put(KEY_MEMORY_NAME, memory.getMemoryName());
        values.put(KEY_MEMORY_DAYS, memory.getDays());
        values.put(KEY_MEMORY_IMAGE, memory.getImage64());

        db.insert(TABLE_MEMORY, null, values);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Memory> getAllMemory() {
        ArrayList<Memory> listMemory = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_MEMORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Memory memory = new Memory();
                memory.setId(cursor.getInt(0));
                memory.setTime(cursor.getString(1));
                memory.setMemoryName(cursor.getString(2));
                memory.setDays(cursor.getLong(3));
                memory.setImage64(cursor.getString(4));

                listMemory.add(memory);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listMemory;
    }

    public void deleteMemory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_MEMORY, KEY_MEMORY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
}
