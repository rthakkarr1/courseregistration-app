package com.example.courseregistrationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditStudentActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText etName, etCourse;
    Spinner spPriority;

    int selectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        databaseHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etCourse = findViewById(R.id.etCourse);
        spPriority = findViewById(R.id.spPriority);

        populatePrioritySpinner();

        selectedId = getIntent().getIntExtra("selectedId", -1);
        etName.setText(getIntent().getStringExtra("name"));
        etCourse.setText(getIntent().getStringExtra("course"));
        spPriority.setSelection(getIntent().getIntExtra("priority", 0) - 1);

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId != -1) {
                    String name = etName.getText().toString();
                    String course = etCourse.getText().toString();
                    int priority = (int) spPriority.getSelectedItem();
                    if (databaseHelper.updateStudent(selectedId, name, course, priority)) {
                        Toast.makeText(EditStudentActivity.this, "Student updated successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditStudentActivity.this, "Error updating student", Toast.LENGTH_SHORT).show();
                    }
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
