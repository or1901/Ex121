package com.example.ex121;

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

import java.util.ArrayList;

public class ShowGradesActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    Intent gi;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    Spinner spinNames;
    ArrayAdapter<String> spinnerAdp, lvAdp;
    ArrayList<String> namesTbl, gradesTbl;
    ArrayList<Integer> idsTbl;
    ContentValues cv;
    int selectedStudentId;
    ListView lvGrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_grades);

        gi = getIntent();
        initAll();
        readStudentsData();
    }

    /**
     * This function initializes the views and the database.
     */
    private void initAll() {
        spinNames = findViewById(R.id.gradesSpinnerNames);
        spinNames.setOnItemSelectedListener(this);

        lvGrades = findViewById(R.id.lvGrades);
        lvGrades.setOnItemClickListener(this);

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
        namesTbl = new ArrayList<String>();
        idsTbl = new ArrayList<Integer>();
        gradesTbl = new ArrayList<String>();

        spinnerAdp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, namesTbl);
        spinNames.setAdapter(spinnerAdp);

        lvAdp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gradesTbl);
        lvGrades.setAdapter(lvAdp);
    }

    /**
     * This function reads the students ids and names from the table and displays them in the
     * spinner.
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
        spinnerAdp.notifyDataSetChanged();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menuAddStudent){
            gi.setClass(this, InputStudentActivity.class);
            startActivity(gi);
        }
        else if(id == R.id.menuAddGrade)
        {
            gi.setClass(this, InputGradeActivity.class);
            startActivity(gi);
        }
        else if(id == R.id.menuShowStudents)
        {
            gi.setClass(this, ShowStudentsActivity.class);
            startActivity(gi);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedStudentId = idsTbl.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}