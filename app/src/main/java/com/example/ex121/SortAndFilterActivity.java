package com.example.ex121;

import static com.example.ex121.Grades.GRADE;
import static com.example.ex121.Grades.QUARTER;
import static com.example.ex121.Grades.STUDENT_ID;
import static com.example.ex121.Grades.SUBJECT;
import static com.example.ex121.Grades.TABLE_GRADES;
import static com.example.ex121.Grades.TYPE;
import static com.example.ex121.Students.ACTIVE;
import static com.example.ex121.Students.STUDENT_KEY_ID;
import static com.example.ex121.Students.STUDENT_NAME;
import static com.example.ex121.Students.TABLE_STUDENTS;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The activity of sorting and filtering grades:
 * gives the user 3 options of sorting and filtering. According to the user's choice it displays
 * the suitable grades in the chosen order.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 5/1/2023
 */
public class SortAndFilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Intent gi;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ContentValues cv;
    ArrayAdapter<String> adpFilterOptions, adpFilterParam, adpFilteredData;
    Spinner spinFilterOptions, spinFilterParam;
    String[] filterOptions = {"Grades of all students in a given quarter",
            "Grades of all students in a given subject",
            "All grades of a given student in a decreasing order"};
    ArrayList<String> filterParamsTbl, namesTbl, filteredDataTbl;
    ArrayList<Integer> idsTbl;
    int selectedFilterOption;
    String chosenParam;
    TextView tvParamType;
    ListView lvFilteredData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_and_filter);

        gi = getIntent();
        initAll();
        readStudentsData();
    }

    /**
     * This function initializes the views and the database.
     */
    private void initAll() {
        // Inits the views
        spinFilterOptions = findViewById(R.id.spinFilterOptions);
        spinFilterOptions.setOnItemSelectedListener(this);

        spinFilterParam = findViewById(R.id.spinFilterParam);
        spinFilterParam.setOnItemSelectedListener(this);

        lvFilteredData = findViewById(R.id.lvFilteredData);

        tvParamType = findViewById(R.id.tvParamType);

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
        filterParamsTbl = new ArrayList<String>();
        namesTbl = new ArrayList<String>();
        idsTbl = new ArrayList<Integer>();
        filteredDataTbl = new ArrayList<String>();

        selectedFilterOption = 0;

        // Inits the adapters
        adpFilterOptions = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filterOptions);
        spinFilterOptions.setAdapter(adpFilterOptions);

        adpFilterParam = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filterParamsTbl);
        spinFilterParam.setAdapter(adpFilterParam);

        adpFilteredData = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filteredDataTbl);
        lvFilteredData.setAdapter(adpFilteredData);
    }

    /**
     * This function reacts to the choice in each spinner.
     * @param adapterView The adapter of the spinner in which the item is selected
     * @param view The view object of the chosen item from the spinner
     * @param i The index of the selected item in the spinner
     * @param l The row of the selected item in the spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // First spinner
        if(adapterView.getId() == R.id.spinFilterOptions) {
            selectedFilterOption = i;
            filterParamsTbl.clear();

            switch(selectedFilterOption) {
                case 0:
                    tvParamType.setText("Choose a quarter:");
                    filterParamsTbl.add("1");
                    filterParamsTbl.add("2");
                    filterParamsTbl.add("3");
                    filterParamsTbl.add("4");
                    break;

                case 1:
                    tvParamType.setText("Choose a subject:");
                    readAllSubjects(filterParamsTbl);
                    break;

                case 2:
                    tvParamType.setText("Choose a student:");
                    readStudentsData();
                    filterParamsTbl.addAll(namesTbl);
                    break;
                }

                adpFilterParam.notifyDataSetChanged();
                chosenParam = filterParamsTbl.get(0);
            }

        // Second spinner
        else {
            chosenParam = filterParamsTbl.get(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This function reads the students ids and names from the table.
     */
    public void readStudentsData(){
        String[] columns = {STUDENT_KEY_ID, STUDENT_NAME};
        String selection = ACTIVE + "=?";
        String[] selectionArgs = {"1"};
        String groupBy = null;
        String having = null;
        String orderBy = null;

        int col1 = 0;
        int col2 = 0;

        int key = 0;
        String name = "";
        namesTbl.clear();
        idsTbl.clear();

        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE_STUDENTS, columns, selection, selectionArgs, groupBy, having, orderBy);

        col1 = crsr.getColumnIndex(STUDENT_KEY_ID);
        col2 = crsr.getColumnIndex(STUDENT_NAME);

        // Reads the names and ids
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            key = crsr.getInt(col1);
            name = crsr.getString(col2);

            idsTbl.add(key);
            namesTbl.add(name);

            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * This function reads all the subjects of the grades that belong to active students, into a
     * given array list.
     * @param arrayList The array list to save the subjects into
     */
    public void readAllSubjects(ArrayList<String> arrayList) {
        String[] columns = {SUBJECT};
        String selection = STUDENT_ID + "=?";
        String[] selectionArgs = {""};
        String groupBy = null;
        String having = null;
        String orderBy = null;

        int col1 = 0;
        String subject = "";

        db = hlp.getReadableDatabase();
        for(int i = 0; i < idsTbl.size(); i++)
        {
            selectionArgs[0] = "" + idsTbl.get(i);
            crsr = db.query(TABLE_GRADES, columns, selection, selectionArgs, groupBy, having, orderBy);

            col1 = crsr.getColumnIndex(SUBJECT);

            // Reads each subject and adds it only once
            crsr.moveToFirst();
            while (!crsr.isAfterLast()) {
                subject = crsr.getString(col1);

                if(!arrayList.contains(subject)) {
                    arrayList.add(subject);
                }

                crsr.moveToNext();
            }
        }
        crsr.close();
        db.close();
    }

    /**
     * This function reads the data of all the grades, filtered by given column, and ordered by
     * given condition. The data is saved into a given array list.
     * @param columnType The type of the column to filter by
     * @param columnValue The value of the column to filter by
     * @param arrayList The array list to save the filtered grades data into
     * @param orderBy The condition to order the grades by
     */
    public void filterGrades(String columnType, String columnValue, ArrayList<String> arrayList, String orderBy) {
        String[] columns = {GRADE, SUBJECT, TYPE, STUDENT_ID, QUARTER};
        String selection = columnType + "=?";
        String[] selectionArgs = {columnValue};
        String groupBy = null;
        String having = null;

        int col1 = 0;
        int col2 = 0;
        int col3 = 0;
        int col4 = 0;
        int col5 = 0;

        int grade = 0, studId = 0, quarter = 0;
        String subject = "", type = "", studName = "";
        String allGradeData = "";

        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE_GRADES, columns, selection, selectionArgs, groupBy, having, orderBy);

        col1 = crsr.getColumnIndex(GRADE);
        col2 = crsr.getColumnIndex(SUBJECT);
        col3 = crsr.getColumnIndex(TYPE);
        col4 = crsr.getColumnIndex(STUDENT_ID);
        col5 = crsr.getColumnIndex(QUARTER);

        // Reads the grade's data
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            grade = crsr.getInt(col1);
            subject = crsr.getString(col2);
            type = crsr.getString(col3);
            studId = crsr.getInt(col4);
            quarter = crsr.getInt(col5);

            studName = namesTbl.get(idsTbl.indexOf(studId));

            // Decides which grade data to save
            if(columnType.equals(QUARTER)) {
                allGradeData = subject + "(" + type + "): " + grade + " - " + studName;
            }
            else if(columnType.equals(SUBJECT)) {
                allGradeData = type + "(Quarter " + quarter + "): " + grade + " - " + studName;
            }
            else {
                allGradeData = subject + "(" + type + "): Quarter " + quarter + ", " + grade;
            }

            arrayList.add(allGradeData);

            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * This function displays the suitable grades in the list view, according to the chosen filter
     * and its chosen parameter.
     * @param view The button that was clicked in order to display the grades
     */
    public void applyFilter(View view) {
        int chosenStudId = 0;

        filteredDataTbl.clear();

        switch(selectedFilterOption) {
            case 0:
                filterGrades(QUARTER, chosenParam, filteredDataTbl, null);
                break;
            case 1:
                filterGrades(SUBJECT, chosenParam, filteredDataTbl, null);
                break;
            case 2:
                chosenStudId = idsTbl.get(namesTbl.indexOf(chosenParam));
                filterGrades(STUDENT_ID, "" + chosenStudId, filteredDataTbl, GRADE + " DESC");
                break;
        }

        adpFilteredData.notifyDataSetChanged();
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
        }
        else if (id == R.id.menuAddGrade) {
            gi.setClass(this, InputGradeActivity.class);
            startActivity(gi);
        }
        else if (id == R.id.menuShowStudents) {
            gi.setClass(this, ShowStudentsActivity.class);
            startActivity(gi);
        }
        else if (id == R.id.menuShowGrades) {
            gi.setClass(this, ShowGradesActivity.class);
            startActivity(gi);
        }
        else if(id == R.id.menuCredits)
        {
            gi.setClass(this, CreditsActivity.class);
            startActivity(gi);
        }

        return super.onOptionsItemSelected(item);
    }
}