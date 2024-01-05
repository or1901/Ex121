package com.example.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The credits activity:
 * displays the credits of the application.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 5/1/2023
 */
public class CreditsActivity extends AppCompatActivity {
    Intent gi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        gi = getIntent();
    }

    /**
     * This function presents the options menu for moving between activities.
     *
     * @param menu the options menu in which you place your items.
     * @return true in order to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function reacts to the user choice in the options menu - it moves to the chosen
     * activity from the menu.
     *
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
        } else if (id == R.id.menuSortAndFilter) {
            gi.setClass(this, SortAndFilterActivity.class);
            startActivity(gi);
        }

        return super.onOptionsItemSelected(item);
    }
}