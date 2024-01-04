package com.example.ex121;

import static com.example.ex121.Grades.GRADE;
import static com.example.ex121.Grades.GRADE_KEY_ID;
import static com.example.ex121.Grades.QUARTER;
import static com.example.ex121.Grades.STUDENT_ID;
import static com.example.ex121.Grades.SUBJECT;
import static com.example.ex121.Grades.TABLE_GRADES;
import static com.example.ex121.Grades.TYPE;
import static com.example.ex121.Students.ACTIVE;
import static com.example.ex121.Students.ADDRESS;
import static com.example.ex121.Students.FATHER_NAME;
import static com.example.ex121.Students.FATHER_PHONE;
import static com.example.ex121.Students.HOME_PHONE;
import static com.example.ex121.Students.MOTHER_NAME;
import static com.example.ex121.Students.MOTHER_PHONE;
import static com.example.ex121.Students.STUDENT_KEY_ID;
import static com.example.ex121.Students.STUDENT_NAME;
import static com.example.ex121.Students.STUDENT_PHONE;
import static com.example.ex121.Students.TABLE_STUDENTS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for building the database.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 04/12/2023
 */
public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dataBase.db";
    private static final int DATABASE_VERSION = 1;
    private String strCreate, strDelete;

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This function creates the new tables of Students and Grades in a given database.
     * @param db The database to create the new tables in.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the students table
        strCreate = "CREATE TABLE "+TABLE_STUDENTS;
        strCreate += " ("+STUDENT_KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+ACTIVE+" INTEGER,";
        strCreate += " "+STUDENT_NAME+" TEXT,";
        strCreate += " "+ADDRESS+" TEXT,";
        strCreate += " "+STUDENT_PHONE+" TEXT,";
        strCreate += " "+HOME_PHONE+" TEXT,";
        strCreate += " "+MOTHER_NAME+" TEXT,";
        strCreate += " "+MOTHER_PHONE+" TEXT,";
        strCreate += " "+FATHER_NAME+" TEXT,";
        strCreate += " "+FATHER_PHONE+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        // Creates the grades table
        strCreate = "CREATE TABLE "+TABLE_GRADES;
        strCreate += " ("+GRADE_KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+STUDENT_ID+" INTEGER,";
        strCreate += " "+GRADE+" INTEGER,";
        strCreate += " "+SUBJECT+" TEXT,";
        strCreate += " "+TYPE+" TEXT,";
        strCreate += " "+QUARTER+" INTEGER";
        strCreate += ");";
        db.execSQL(strCreate);

    }

    /**
     * This function deletes the existing tables of a given database, and recreates them.
     * @param db The database to recreate its tables.
     * @param i The old version of the database.
     * @param i1 The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Deletes both of the tables
        strDelete = "DROP TABLE IF EXISTS "+TABLE_STUDENTS;
        db.execSQL(strDelete);

        strDelete = "DROP TABLE IF EXISTS "+TABLE_GRADES;
        db.execSQL(strDelete);

        onCreate(db); // Creates them again
    }
}
