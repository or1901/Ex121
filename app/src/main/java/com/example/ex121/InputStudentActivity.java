package com.example.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * The main activity:
 * inputs the different data of the students and the grades to the tables.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 28/11/2023
 */
public class InputStudentActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HelperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_student);

        // Creates the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
    }
}