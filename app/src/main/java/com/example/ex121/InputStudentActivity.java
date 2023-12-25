package com.example.ex121;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The student input activity:
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
    AlertDialog.Builder adb;
    AlertDialog ad;
    ContentValues cv;

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

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
    }

    /**
     * This function checks if all the fields are full, and asks the user if he wants to save
     * the student data or not(with alert dialog).
     * @param view The button that was clicked in order to save the student data.
     */
    public void saveStudent(View view) {
        if(!areEmptyFields()) {
            showAlertDialog();
        }
        else {
            Toast.makeText(this, "There is an empty field!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This function shows an alert dialog which asks the user if he wants to save the student data.
     * According to the user's response, it saves or not saves the data in the database.
     */
    public void showAlertDialog() {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Add New Student");
        adb.setMessage("Do you want to add the new student?");

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

    /**
     * This function saves the values of current student in the database students table.
     */
    public void saveFieldsToDb() {
        cv.clear();

        // Puts the fields to the cv
        cv.put(Students.ACTIVE, 1);
        cv.put(Students.STUDENT_NAME, etStudName.getText().toString());
        cv.put(Students.ADDRESS, etAddress.getText().toString());
        cv.put(Students.STUDENT_PHONE, etStudPhone.getText().toString());
        cv.put(Students.HOME_PHONE, etHomePhone.getText().toString());
        cv.put(Students.MOTHER_NAME, etMomName.getText().toString());
        cv.put(Students.MOTHER_PHONE, etMomPhone.getText().toString());
        cv.put(Students.FATHER_NAME, etDadName.getText().toString());
        cv.put(Students.FATHER_PHONE, etDadPhone.getText().toString());

        // Inserts the data to the table
        db = hlp.getWritableDatabase();
        db.insert(Students.TABLE_STUDENTS, null, cv);
        db.close();

        Toast.makeText(this, "Added new student!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This function checks if at least one of the edit texts values is empty.
     * @return Whether at least one of the edit texts values is empty, or not.
     */
    public boolean areEmptyFields() {
        return (etStudName.getText().toString().equals("")) ||
                (etAddress.getText().toString().equals("")) ||
                (etStudPhone.getText().toString().equals("")) ||
                (etHomePhone.getText().toString().equals("")) ||
                (etMomName.getText().toString().equals("")) ||
                (etMomPhone.getText().toString().equals("")) ||
                (etDadName.getText().toString().equals("")) ||
                (etDadPhone.getText().toString().equals(""));
    }
}