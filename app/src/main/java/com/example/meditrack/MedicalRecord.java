package com.example.meditrack;

import java.io.Serializable;

/**
 * Model class representing a medical record (Medicine or Appointment).
 */
public class MedicalRecord implements Serializable {
    private int id;
    private String name;
    private String type; // "Medicine" or "Appointment"
    private String description;
    private String dateTime;
    private boolean isCompleted;

    public MedicalRecord() {}

    public MedicalRecord(int id, String name, String type, String description, String dateTime, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.dateTime = dateTime;
        this.isCompleted = isCompleted;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}