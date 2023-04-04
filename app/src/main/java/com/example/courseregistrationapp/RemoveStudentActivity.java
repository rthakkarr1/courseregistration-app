package com.example.courseregistrationapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RemoveStudentActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    Button btnRemove;
    ListView lvWaitingList;

    int selectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_student);
        databaseHelper = new DatabaseHelper(this);

        btnRemove = findViewById(R.id.btnRemove);
        lvWaitingList = findViewById(R.id.lvWaitingList);

        loadWaitingList();

        lvWaitingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                selectedId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId != -1) {
                    if (databaseHelper.removeStudent(selectedId)) {
                        Toast.makeText(RemoveStudentActivity.this, "Student removed successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(RemoveStudentActivity.this, "Error removing student", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RemoveStudentActivity.this, "Please select a student first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadWaitingList() {
        Cursor cursor = databaseHelper.getAllStudents();
        SimpleCursorAdapter
                adapter = new SimpleCursorAdapter(
                this,
                R.layout.waiting_list_item,
                cursor,
                new String[]{DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_COURSE, DatabaseHelper.COLUMN_PRIORITY},
                new int[]{R.id.tvName, R.id.tvCourse, R.id.tvPriority},
                0);

        lvWaitingList.setAdapter(adapter);
    }}
