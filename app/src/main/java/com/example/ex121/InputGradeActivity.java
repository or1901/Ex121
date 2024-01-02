package com.example.ex121;

import static com.example.ex121.Students.ACTIVE;
import static com.example.ex121.Students.STUDENT_KEY_ID;
import static com.example.ex121.Students.STUDENT_NAME;
import static com.example.ex121.Students.TABLE_STUDENTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class InputGradeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    Spinner spinNames;
    ArrayAdapter adp;
    ArrayList<String> namesTbl;
    ArrayList<Integer> idsTbl;
    int selectedStudentId;
    EditText etGrade, etSubject, etWorkType, etQuarter;
    ContentValues cv;
    AlertDialog.Builder adb;
    AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_grade);

        initAll();
        readStudentsData();
    }

    /**
     * This function initializes the views and the database.
     */
    private void initAll() {
        spinNames = findViewById(R.id.spinnerNames);
        spinNames.setOnItemSelectedListener(this);

        etGrade = findViewById(R.id.etGrade);
        etSubject = findViewById(R.id.etSubject);
        etWorkType = findViewById(R.id.etWorkType);
        etQuarter = findViewById(R.id.etQuarter);

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
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
            finish();
        }

        return super.onOptionsItemSelected(item);
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
        namesTbl = new ArrayList<>();
        idsTbl = new ArrayList<>();

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
        adp = new ArrayAdapter(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, namesTbl);
        spinNames.setAdapter(adp);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedStudentId = idsTbl.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void saveGrade(View view) {
        showAlertDialog();
    }

    /**
     * This function saves the values of current grade in the database grades table.
     */
    public void saveFieldsToDb() {
        cv.clear();

        // Puts the fields to the cv
        cv.put(Grades.GRADE_KEY_ID, selectedStudentId);
        cv.put(Grades.GRADE, Double.parseDouble(etGrade.getText().toString()));
        cv.put(Grades.SUBJECT, etSubject.getText().toString());
        cv.put(Grades.TYPE, etWorkType.getText().toString());
        cv.put(Grades.QUARTER, Integer.parseInt(etQuarter.getText().toString()));

        // Inserts the data to the table
        db = hlp.getWritableDatabase();
        db.insert(Grades.TABLE_GRADES, null, cv);
        db.close();

        Toast.makeText(this, "Added new grade!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This function shows an alert dialog which asks the user if he wants to save the grade data.
     * According to the user's response, it saves or not saves the data in the database.
     */
    public void showAlertDialog() {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Add New Grade");
        adb.setMessage("Do you want to add the new grade?");

        // Saves in the database
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveFieldsToDb();
            }
        });

        // Doesn't save
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ad = adb.create();
        ad.show();
    }
}