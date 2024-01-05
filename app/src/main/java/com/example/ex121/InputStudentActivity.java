package com.example.ex121;

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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * The students input activity:
 * inputs the data of the students to the table.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 28/11/2023
 */
public class InputStudentActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HelperDB hlp;
    EditText etStudName, etAddress, etStudPhone, etHomePhone, etMomName, etMomPhone, etDadName,
            etDadPhone;
    Switch switchActive;
    AlertDialog.Builder adb;
    AlertDialog ad;
    ContentValues cv;
    Intent si, gi;
    int editStudentId;
    Cursor crsr;
    boolean saveStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_student);

        initAll();
    }

    /**
     * This function initializes the views and the database.
     */
    private void initAll() {
        // Inits views
        etStudName = findViewById(R.id.etStudName);
        etAddress = findViewById(R.id.etAddress);
        etStudPhone = findViewById(R.id.etStudPhone);
        etHomePhone = findViewById(R.id.etHomePhone);
        etMomName = findViewById(R.id.etMomName);
        etMomPhone = findViewById(R.id.etMomPhone);
        etDadName = findViewById(R.id.etDadName);
        etDadPhone = findViewById(R.id.etDadPhone);
        switchActive = findViewById(R.id.switchActive);

        saveStudent = true;

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
        si = new Intent();
    }

    /**
     * This function checks if the field of the student name is full, and asks the user if he wants
     * to save the student data or not(with alert dialog).
     * @param view The button that was clicked in order to save the student data.
     */
    public void saveStudent(View view) {
        if(!isEmptyName()) {
            showAlertDialog();
        }
        else {
            Toast.makeText(this, "You must enter student name!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This function shows an alert dialog which asks the user if he wants to save the student data.
     * According to the user's response, it saves or not saves the data in the database.
     */
    public void showAlertDialog() {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Save Student");
        adb.setMessage("Do you want to save the student data?");

        // Saves in the database
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveFieldsToDb(saveStudent);
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

    /**
     * This function saves the values of current student in the database students table -
     * according to the parameter it knows if to add a new record, or edit an existing one.
     * @param saveNeed Whether to add a new record of student, or to edit an existing one.
     */
    public void saveFieldsToDb(boolean saveNeed) {
        cv.clear();

        // Puts the fields to the cv
        cv.put(Students.ACTIVE, getUserActive());
        cv.put(Students.STUDENT_NAME, etStudName.getText().toString());
        cv.put(ADDRESS, etAddress.getText().toString());
        cv.put(Students.STUDENT_PHONE, etStudPhone.getText().toString());
        cv.put(Students.HOME_PHONE, etHomePhone.getText().toString());
        cv.put(Students.MOTHER_NAME, etMomName.getText().toString());
        cv.put(Students.MOTHER_PHONE, etMomPhone.getText().toString());
        cv.put(Students.FATHER_NAME, etDadName.getText().toString());
        cv.put(Students.FATHER_PHONE, etDadPhone.getText().toString());

        if(saveNeed)
        {
            // Adds new student
            db = hlp.getWritableDatabase();
            db.insert(Students.TABLE_STUDENTS, null, cv);
            db.close();

            Toast.makeText(this, "Added new student!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Edits existing student
            db = hlp.getWritableDatabase();
            db.update(TABLE_STUDENTS, cv, STUDENT_KEY_ID+"=?", new String[]{"" + editStudentId});
            db.close();

            Toast.makeText(this, "Edited student data!", Toast.LENGTH_SHORT).show();
            saveStudent = true;
        }
    }

    /**
     * This function checks if the edit text of the student name is empty.
     * @return Whether the edit text of the student name is empty, or not.
     */
    public boolean isEmptyName() {
        return (etStudName.getText().toString().equals(""));
    }


    /**
     * This function checks if the switch of the user's activity is selected or not.
     * @return 1 if the user's activity is selected or 0 otherwise.
     */
    public int getUserActive()
    {
        if(switchActive.isChecked())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * This function resets the edit texts of the student fields.
     */
    public void resetStudentFields() {
        etStudName.setText("");
        etAddress.setText("");
        etStudPhone.setText("");
        etHomePhone.setText("");
        etMomName.setText("");
        etMomPhone.setText("");
        etDadName.setText("");
        etDadPhone.setText("");
        switchActive.setChecked(true);
    }

    /**
     * This function displays the data of a given student in the edit texts.
     * @param studentId The id of the student to display.
     */
    public void displayStudentFields(int studentId){
        String[] columns = {STUDENT_NAME, ADDRESS, STUDENT_PHONE, HOME_PHONE, MOTHER_NAME,
                MOTHER_PHONE, FATHER_NAME, FATHER_PHONE, ACTIVE};
        String selection = STUDENT_KEY_ID + "=?";
        String[] selectionArgs = {"" + studentId};
        String groupBy = null;
        String having = null;
        String orderBy = null;

        int col1 = 0;
        int col2 = 0;
        int col3 = 0;
        int col4 = 0;
        int col5 = 0;
        int col6 = 0;
        int col7 = 0;
        int col8 = 0;
        int col9 = 0;
        int isActive = 0;

        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE_STUDENTS, columns, selection, selectionArgs, groupBy, having, orderBy);

        // Saves the cols index
        col1 = crsr.getColumnIndex(STUDENT_NAME);
        col2 = crsr.getColumnIndex(ADDRESS);
        col3 = crsr.getColumnIndex(STUDENT_PHONE);
        col4 = crsr.getColumnIndex(HOME_PHONE);
        col5 = crsr.getColumnIndex(MOTHER_NAME);
        col6 = crsr.getColumnIndex(MOTHER_PHONE);
        col7 = crsr.getColumnIndex(FATHER_NAME);
        col8 = crsr.getColumnIndex(FATHER_PHONE);
        col9 = crsr.getColumnIndex(ACTIVE);

        // Reads and displays the student's data
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            etStudName.setText(crsr.getString(col1));
            etAddress.setText(crsr.getString(col2));
            etStudPhone.setText(crsr.getString(col3));
            etHomePhone.setText(crsr.getString(col4));
            etMomName.setText(crsr.getString(col5));
            etMomPhone.setText(crsr.getString(col6));
            etDadName.setText(crsr.getString(col7));
            etDadPhone.setText(crsr.getString(col8));
            isActive = crsr.getInt(col9);

            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        switchActive.setChecked(isActive == 1);
    }

    /**
     * This function checks if the intent has returned with a student id. If so -
     * it displays the data of the given student on the screen.
     */
    @Override
    protected void onStart() {
        super.onStart();

        gi = getIntent();
        editStudentId = gi.getIntExtra("StudentId", -1);

        if(editStudentId != -1)
        {
            saveStudent = false;
            displayStudentFields(editStudentId);
        }
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
     * activity from the menu, or resets the current one.
     * @param item the menu item that was selected.
     * @return must return true for the menu to react.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menuAddGrade){
            si.setClass(this, InputGradeActivity.class);
            startActivity(si);
        }
        else if(id == R.id.menuShowStudents)
        {
            si.setClass(this, ShowStudentsActivity.class);
            startActivity(si);
        }
        else if(id == R.id.menuShowGrades)
        {
            si.setClass(this, ShowGradesActivity.class);
            startActivity(si);
        }
        else
        {
            resetStudentFields();
        }

        return super.onOptionsItemSelected(item);
    }
}