package com.example.ex121;

import static com.example.ex121.Students.ACTIVE;
import static com.example.ex121.Students.ADDRESS;
import static com.example.ex121.Students.HOME_PHONE;
import static com.example.ex121.Students.KEY_ID;
import static com.example.ex121.Students.STUDENT_NAME;
import static com.example.ex121.Students.STUDENT_PHONE;
import static com.example.ex121.Students.TABLE_STUDENTS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dataBase.db";
    private static final int DATABASE_VERSION = 1;
    private String strCreate, strDelete;

    public HelperDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        strCreate = "CREATE TABLE "+TABLE_STUDENTS;
        strCreate += " ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+ACTIVE+" INTEGER,";
        strCreate += " "+STUDENT_NAME+" TEXT,";
        strCreate += " "+ADDRESS+" TEXT,";
        strCreate += " "+STUDENT_PHONE+" TEXT,";
        strCreate += " "+HOME_PHONE+" TEXT,";
        strCreate += " "+AGE+" INTEGER";
        strCreate += ");";
        db.execSQL(strCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
