package com.example.ex121;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
    }

    public void saveStudent(View view) {
    }

    public void showAlertDialog() {
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Add New Student");

    }
}