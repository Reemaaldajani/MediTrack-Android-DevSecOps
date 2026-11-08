package com.example.meditrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MediTrack.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_COMPLETED = "completed";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_RECORDS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_DATETIME + " TEXT, " +
                    COLUMN_COMPLETED + " INTEGER DEFAULT 0);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    // Insert new record
    public long insertRecord(MedicalRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, record.getName());
        values.put(COLUMN_TYPE, record.getType());
        values.put(COLUMN_DESCRIPTION, record.getDescription());
        values.put(COLUMN_DATETIME, record.getDateTime());
        values.put(COLUMN_COMPLETED, record.isCompleted() ? 1 : 0);
        long id = db.insert(TABLE_RECORDS, null, values);
        db.close();
        return id;
    }

    // Get all records
    public List<MedicalRecord> getAllRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECORDS + " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MedicalRecord record = new MedicalRecord();
                record.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                record.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                record.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                record.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                record.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATETIME)));
                record.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1);
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }

    // Delete a record
    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Search records by name
    public List<MedicalRecord> searchRecords(String query) {
        List<MedicalRecord> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECORDS, null, COLUMN_NAME + " LIKE ?",
                new String[]{"%" + query + "%"}, null, null, COLUMN_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                MedicalRecord record = new MedicalRecord();
                record.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                record.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                record.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                record.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                record.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATETIME)));
                record.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1);
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }
    
    // Update record completion status
    public void updateCompletionStatus(int id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED, isCompleted ? 1 : 0);
        db.update(TABLE_RECORDS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}