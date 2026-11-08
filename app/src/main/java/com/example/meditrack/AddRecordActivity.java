package com.example.meditrack;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * AddRecordActivity: Activity to add new medicine or appointment records.
 */
public class AddRecordActivity extends AppCompatActivity {

    private EditText etName, etDescription, etDateTime;
    private Spinner spinnerType;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etDateTime = findViewById(R.id.etDateTime);
        spinnerType = findViewById(R.id.spinnerType);
        Button btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        // Setup Spinner for Type selection
        String[] types = {"Medicine", "Appointment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();
            }
        });
    }

    private void saveRecord() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        // Empty input warning
        if (name.isEmpty() || description.isEmpty() || dateTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        MedicalRecord record = new MedicalRecord();
        record.setName(name);
        record.setType(type);
        record.setDescription(description);
        record.setDateTime(dateTime);
        record.setCompleted(false);

        long id = dbHelper.insertRecord(record);

        if (id > 0) {
            Toast.makeText(this, "Record added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to previous screen
        } else {
            Toast.makeText(this, "Failed to add record", Toast.LENGTH_SHORT).show();
        }
    }
}