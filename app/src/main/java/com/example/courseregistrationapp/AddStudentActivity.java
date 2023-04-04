package com.example.courseregistrationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText etName, etCourse;
    Spinner spPriority;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        databaseHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etCourse = findViewById(R.id.etCourse);
        spPriority = findViewById(R.id.spPriority);
        btnSave = findViewById(R.id.btnSave);

        populatePrioritySpinner();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String course = etCourse.getText().toString();
                int priority = (int) spPriority.getSelectedItem();

                if (databaseHelper.addStudent(name, course, priority)) {
                    Toast.makeText(AddStudentActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddStudentActivity.this, "Error adding student", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populatePrioritySpinner() {
        Integer[] priorities = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(priorityAdapter);
    }
}
