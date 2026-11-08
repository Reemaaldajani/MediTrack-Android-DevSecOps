package com.example.meditrack;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * RecordDetailsActivity: Shows full details of a specific record.
 * This demonstrates passing data between activities using Intent extras.
 */
public class RecordDetailsActivity extends AppCompatActivity {

    private TextView tvDetailName, tvDetailType, tvDetailDesc, tvDetailDate, tvDetailStatus;
    private MedicalRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);

        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailType = findViewById(R.id.tvDetailType);
        tvDetailDesc = findViewById(R.id.tvDetailDesc);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        Button btnShare = findViewById(R.id.btnShare);

        // Get the record object from the intent using the non-deprecated method for API 33+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            record = getIntent().getSerializableExtra("RECORD_DATA", MedicalRecord.class);
        } else {
            //noinspection deprecation
            record = (MedicalRecord) getIntent().getSerializableExtra("RECORD_DATA");
        }

        if (record != null) {
            displayDetails();
        } else {
            Toast.makeText(this, "Error loading details", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Additional feature: Share record details
        btnShare.setOnClickListener(v -> shareRecord());
    }

    private void displayDetails() {
        tvDetailName.setText(record.getName());
        tvDetailType.setText("Type: " + record.getType());
        tvDetailDesc.setText("Description: " + record.getDescription());
        tvDetailDate.setText("Scheduled: " + record.getDateTime());
        tvDetailStatus.setText("Status: " + (record.isCompleted() ? "Completed" : "Pending"));
    }

    private void shareRecord() {
        String shareText = "Medical Record Details:\n" +
                "Name: " + record.getName() + "\n" +
                "Type: " + record.getType() + "\n" +
                "Time: " + record.getDateTime() + "\n" +
                "Details: " + record.getDescription();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share record via");
        startActivity(shareIntent);
    }
}