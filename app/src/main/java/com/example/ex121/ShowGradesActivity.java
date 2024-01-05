package com.example.ex121;

import static com.example.ex121.Grades.GRADE;
import static com.example.ex121.Grades.GRADE_KEY_ID;
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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * The activity of showing grades:
 * displays all the existing grades of a chosen student, and gives the option to see the grades'
 * details, edit and delete them.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 4/1/2023
 */
public class ShowGradesActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnCreateContextMenuListener,
        AdapterView.OnItemLongClickListener {
    Intent gi;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    Spinner spinNames;
    ArrayAdapter<String> spinnerAdp, lvAdp;
    ArrayList<String> namesTbl, gradesTbl;
    ArrayList<Integer> studentsIdsTbl, gradesIdsTbl;
    ContentValues cv;
    int selectedStudentId, selectedStudentIndex, selectedGradeId;
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
        // Inits the views
        spinNames = findViewById(R.id.gradesSpinnerNames);
        spinNames.setOnItemSelectedListener(this);

        lvGrades = findViewById(R.id.lvGrades);
        lvGrades.setOnItemLongClickListener(this);
        lvGrades.setOnCreateContextMenuListener(this);

        // Inits the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
        namesTbl = new ArrayList<String>();
        studentsIdsTbl = new ArrayList<Integer>();
        gradesIdsTbl = new ArrayList<Integer>();
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
     * names spinner.
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
        studentsIdsTbl.clear();

        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE_STUDENTS, columns, selection, selectionArgs, groupBy, having, orderBy);

        col1 = crsr.getColumnIndex(STUDENT_KEY_ID);
        col2 = crsr.getColumnIndex(STUDENT_NAME);

        // Reads the names and ids
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            key = crsr.getInt(col1);
            name = crsr.getString(col2);

            studentsIdsTbl.add(key);
            namesTbl.add(name);

            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        spinnerAdp.notifyDataSetChanged();
    }

    /**
     * This function reads the grades data of a given student, and displays them in the grades
     * list view.
     * @param studentId The id of the student to display its grades.
     */
    public void readSGradesData(int studentId){
        String[] columns = {GRADE_KEY_ID, GRADE, SUBJECT, TYPE};
        String selection = STUDENT_ID + "=?";
        String[] selectionArgs = {"" + selectedStudentId};
        String groupBy = null;
        String having = null;
        String orderBy = null;

        int col1 = 0;
        int col2 = 0;
        int col3 = 0;
        int col4 = 0;

        int key = 0, grade = 0;
        String subject = "", type = "";
        gradesTbl.clear();
        gradesIdsTbl.clear();

        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE_GRADES, columns, selection, selectionArgs, groupBy, having, orderBy);

        col1 = crsr.getColumnIndex(GRADE_KEY_ID);
        col2 = crsr.getColumnIndex(GRADE);
        col3 = crsr.getColumnIndex(SUBJECT);
        col4 = crsr.getColumnIndex(TYPE);

        // Reads the grade's data
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            key = crsr.getInt(col1);
            grade = crsr.getInt(col2);
            subject = crsr.getString(col3);
            type = crsr.getString(col4);

            gradesIdsTbl.add(key);
            gradesTbl.add(subject + "(" + type + "), " + grade);

            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        lvAdp.notifyDataSetChanged();
    }

    /**
     * This function creates the context menu of actions to perform with the chosen grade from the
     * list view.
     * @param menu The menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Grade Actions");
        menu.add("Show & Edit");
        menu.add("Delete Grade");
    }

    /**
     * This function reacts to the choice from the context menu of actions on the chosen grade.
     * @param item The menu
     * @return true for the menu to react, false otherwise.
     */
    public boolean onContextItemSelected(MenuItem item) {
        String action = item.getTitle().toString();

        if(action.equals("Show & Edit"))
        {
            // Goes to the input grade activity and displays there
            gi.setClass(this, InputGradeActivity.class);
            gi.putExtra("StudentIndex", selectedStudentIndex);
            gi.putExtra("GradeId", selectedGradeId);
            startActivity(gi);
        }
        else
        {
            // Deletes the chosen grade
            deleteGrade(selectedGradeId);
            gradesTbl.remove(selectedGradeId - 1);
            lvAdp.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item);
    }

    /**
     * This function deletes a record of a given grade from the database.
     * @param gradeId The key id of the grade to delete.
     */
    public void deleteGrade(int gradeId) {
        db = hlp.getWritableDatabase();
        db.delete(TABLE_GRADES, GRADE_KEY_ID+"=?", new String[]{"" + gradeId});
        db.close();
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
     * This function saves the id and the index of the chosen student from the spinner, and displays
     * all the grades of the chosen student in the list view.
     * @param adapterView The adapter view of the spinner
     * @param view
     * @param i The position of the chosen student in the spinner
     * @param l The row of the chosen student in the spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedStudentId = studentsIdsTbl.get(i);
        selectedStudentIndex = i;
        readSGradesData(selectedStudentId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This function saves the id of the chosen grade from the list view(when long clicked).
     * @param adapterView The adapter view of the grades list view
     * @param view
     * @param i The position of the chosen grade in the list view
     * @param l The row of the chosen grade in the list view
     * @return false
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedGradeId = gradesIdsTbl.get(i);

        return false;
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
}