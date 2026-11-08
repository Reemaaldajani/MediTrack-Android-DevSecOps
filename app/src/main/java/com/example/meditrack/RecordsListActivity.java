package com.example.meditrack;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * RecordsListActivity: Displays all saved records using RecyclerView.
 */
public class RecordsListActivity extends AppCompatActivity implements RecordsAdapter.OnRecordClickListener {

    private RecyclerView recyclerView;
    private RecordsAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<MedicalRecord> recordList;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_list);

        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        dbHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        loadRecords();

        // Search functionality - Real-time filtering
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadRecords() {
        recordList = dbHelper.getAllRecords();
        adapter = new RecordsAdapter(this, recordList, this);
        recyclerView.setAdapter(adapter);
    }

    private void filterRecords(String query) {
        List<MedicalRecord> filteredList = dbHelper.searchRecords(query);
        adapter.updateList(filteredList);
        
        if (filteredList.isEmpty() && !query.isEmpty()) {
            // Optional: Toast for not found
        }
    }

    @Override
    public void onDeleteClick(int position) {
        MedicalRecord record = recordList.get(position);
        showDeleteConfirmation(record.getId());
    }

    @Override
    public void onStatusToggle(int position, boolean isCompleted) {
        MedicalRecord record = recordList.get(position);
        dbHelper.updateCompletionStatus(record.getId(), isCompleted);
        // We don't necessarily need to reload everything, but it ensures data consistency
    }

    @Override
    public void onItemClick(int position) {
        // Requirement: Pass data between activities using Intent extras
        MedicalRecord record = recordList.get(position);
        Intent intent = new Intent(this, RecordDetailsActivity.class);
        intent.putExtra("RECORD_DATA", record);
        startActivity(intent);
    }

    private void showDeleteConfirmation(final int recordId) {
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(new DeleteConfirmationDialog.OnDeleteConfirmListener() {
            @Override
            public void onConfirm() {
                dbHelper.deleteRecord(recordId);
                loadRecords();
                Toast.makeText(RecordsListActivity.this, "Record deleted", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "DeleteConfirmationDialog");
    }
}