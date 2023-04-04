package com.example.courseregistrationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ListView lvWaitingList;
    Button btnAddStudent, btnEditStudent, btnRemoveStudent;
    int selectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        lvWaitingList = findViewById(R.id.lvWaitingList);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnEditStudent = findViewById(R.id.btnEditStudent);
        btnRemoveStudent = findViewById(R.id.btnRemoveStudent);

        loadWaitingList();

        lvWaitingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                selectedId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            }
        });

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId != -1) {
                    Cursor cursor = databaseHelper.getStudentById(selectedId);
                    if (cursor.moveToFirst()) {
                        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                        String course = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE));
                        int priority = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRIORITY));

                        Intent intent = new Intent(MainActivity.this, EditStudentActivity.class);
                        intent.putExtra("selectedId", selectedId);
                        intent.putExtra("name", name);
                        intent.putExtra("course", course);
                        intent.putExtra("priority", priority);
                        startActivityForResult(intent, 2);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please select a student first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRemoveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId != -1) {
                    Intent intent = new Intent(MainActivity.this, RemoveStudentActivity.class);
                    intent.putExtra("selectedId", selectedId);
                    startActivityForResult(intent, 3);
                } else {
                    Toast.makeText(MainActivity.this, "Please select a student first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 2 || requestCode == 3) {
            if (resultCode == RESULT_OK) {
                loadWaitingList();
            }
        }
    }

    private void loadWaitingList() {
        Cursor cursor = databaseHelper.getAllStudents();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.waiting_list_item,
                cursor,
                new String[]{DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_COURSE, DatabaseHelper.COLUMN_PRIORITY},
                new int[]{R.id.tvName, R.id.tvCourse, R.id.tvPriority},
                0);
        lvWaitingList.setAdapter(adapter);
    }
}
