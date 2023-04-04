package com.example.courseregistrationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "waiting_list.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_WAITING_LIST = "waiting_list";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_PRIORITY = "priority";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WAITING_LIST_TABLE = "CREATE TABLE " + TABLE_WAITING_LIST + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_COURSE + " TEXT, " +
                COLUMN_PRIORITY + " INTEGER" +
                ")";
        db.execSQL(CREATE_WAITING_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAITING_LIST);
        onCreate(db);
    }

    public boolean addStudent(String name, String course, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_COURSE, course);
        values.put(COLUMN_PRIORITY, priority);

        long result = db.insert(TABLE_WAITING_LIST, null, values);
        db.close();

        return result != -1;
    }

    public boolean updateStudent(int id, String name, String course, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_COURSE, course);
        values.put(COLUMN_PRIORITY, priority);

        int rowsAffected = db.update(TABLE_WAITING_LIST, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }

    public boolean removeStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_WAITING_LIST, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_WAITING_LIST, null, null, null, null, null, COLUMN_PRIORITY + " ASC");
    }

    public Cursor getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_COURSE, COLUMN_PRIORITY};
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        return db.query(TABLE_WAITING_LIST, columns, selection, selectionArgs, null, null, null);
    }
}