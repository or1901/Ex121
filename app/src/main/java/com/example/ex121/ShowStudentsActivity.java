package com.example.ex121;

import static com.example.ex121.Students.ACTIVE;
import static com.example.ex121.Students.STUDENT_KEY_ID;
import static com.example.ex121.Students.STUDENT_NAME;
import static com.example.ex121.Students.TABLE_STUDENTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowStudentsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ContentValues cv;
    ListView lvStudents;
    ArrayAdapter<String> adp;
    ArrayList<String> namesTbl;
    Intent gi;
    int selectedStudentId;
    AlertDialog.Builder adb;
    AlertDialog ad;
    Context activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        gi = getIntent();
        activityContext = this;
        initAll();
        readStudentsData();
    }

    /**
     * This function initializes the views and the database.
     */
    private void initAll() {
        // Inits the views
        lvStudents = findViewById(R.id.lvStudents);
        lvStudents.setOnItemClickListener(this);

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
        namesTbl = new ArrayList<>();
        adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, namesTbl);
        lvStudents.setAdapter(adp);
    }

    /**
     * This function reads the students names from the table and displays them in the list view.
     */
    public void readStudentsData(){
        String[] columns = {STUDENT_NAME};
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        int col1 = 0;

        String name = "";
        namesTbl.clear();

        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE_STUDENTS, columns, selection, selectionArgs, groupBy, having, orderBy);

        col1 = crsr.getColumnIndex(STUDENT_NAME);

        // Reads the names and ids
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            name = crsr.getString(col1);

            namesTbl.add(name);

            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp.notifyDataSetChanged();
    }

    public void showAlertDialog(int chosenStudentIndex) {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Show & Edit student");
        adb.setMessage("Do you want to show & edit the data of " + namesTbl.get(chosenStudentIndex) + "?");

        // Saves in the database
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gi.setClass(activityContext, InputStudentActivity.class);
                gi.putExtra("StudentId", selectedStudentId);
                startActivity(gi);
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
        else if(id == R.id.menuShowGrades)
        {
            gi.setClass(this, ShowGradesActivity.class);
            startActivity(gi);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedStudentId = i + 1;
        showAlertDialog(i);
    }
}