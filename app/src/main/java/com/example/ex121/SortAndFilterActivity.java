package com.example.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SortAndFilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Intent gi;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ContentValues cv;
    ArrayAdapter<String> adpFilterOptions;
    Spinner spinFilterOptions;
    String[] filterOptions = {"Grades of all students in a given quarter",
            "Grades of all students in a given subject",
            "All grades of a given student in a decreasing order"};
    int selectedFilterOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_and_filter);

        gi = getIntent();
        initAll();
    }

    /**
     * This function initializes the views and the database.
     */
    private void initAll() {
        // Inits the views
        spinFilterOptions = findViewById(R.id.spinFilterOptions);
        spinFilterOptions.setOnItemSelectedListener(this);

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();

        adpFilterOptions = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filterOptions);
        spinFilterOptions.setAdapter(adpFilterOptions);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This function presents the options menu for moving between activities.
     * @param menu the options menu in which you place your items.
     * @return true in order to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function reacts to the user choice in the options menu - it moves to the chosen
     * activity from the menu.
     * @param item the menu item that was selected.
     * @return must return true for the menu to react.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAddStudent) {
            gi.setClass(this, InputStudentActivity.class);
            startActivity(gi);
        } else if (id == R.id.menuAddGrade) {
            gi.setClass(this, InputGradeActivity.class);
            startActivity(gi);
        } else if (id == R.id.menuShowStudents) {
            gi.setClass(this, ShowStudentsActivity.class);
            startActivity(gi);
        } else if (id == R.id.menuShowGrades) {
            gi.setClass(this, ShowGradesActivity.class);
            startActivity(gi);
        }

        return super.onOptionsItemSelected(item);
    }
}